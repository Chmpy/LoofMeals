package com.example.loofmeals.ui.restaurant

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.loofmeals.LoofMealsApplication
import com.example.loofmeals.data.IRestaurantRepository
import com.example.loofmeals.data.model.Restaurant
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okio.IOException

/**
 * ViewModel for the RestaurantOverview screen.
 *
 * This ViewModel fetches the restaurants from the repository and updates the UI state.
 * If the API call is successful,
 * it updates the UI state with the fetched restaurants and sets the API call state to Success.
 * If the API call fails, it sets the API call state to Error.
 *
 * @property restaurantRepository The repository to fetch the restaurants from.
 */
class RestaurantViewModel(private val restaurantRepository: IRestaurantRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(RestaurantOverviewState())
    val uiState: StateFlow<RestaurantOverviewState> = _uiState.asStateFlow()

    // The state of the API call to fetch the restaurants.
    // This is a mutable state that is updated every time the API call state changes.
    var restaurantApiState: RestaurantApiState by mutableStateOf(RestaurantApiState.Loading)
        private set

    // The current search query. This is updated every time the user enters a new search query.
    private var currentQuery: String = ""

    // Fetch the restaurants when the ViewModel is initialized.
    init {
        getRestaurants()
    }

    /**
     * Fetch the restaurants from the repository.
     *
     * This function fetches the restaurants from the repository and updates the UI state.
     * If the API call is successful,
     * it updates the UI state with the fetched restaurants and sets the API call state to Success.
     * If the API call fails, it sets the API call state to Error.
     */
    fun getRestaurants() {
        viewModelScope.launch {
            restaurantApiState= RestaurantApiState.Loading
            try {
                //Simulate network delay
                kotlinx.coroutines.delay(2000)
                restaurantRepository.getRestaurantList().collect { restaurants ->
                    if (currentQuery.isNotEmpty()) {
                        filterRestaurants(currentQuery)
                        return@collect
                    }
                    Log.d("RestaurantViewModel", "getRestaurants: ${restaurants.size}")
                    _uiState.update {
                        it.copy(restaurants = restaurants)
                    }
                    restaurantApiState = RestaurantApiState.Success
                }
                restaurantApiState = RestaurantApiState.Success
            } catch (e: Exception) {
                Log.d("RestaurantViewModel", "getRestaurants: ${e.message}")
                restaurantApiState = RestaurantApiState.Error
                if (e is IOException) {
                    restaurantApiState = RestaurantApiState.NetworkError
                }
            }
        }
    }

    /**
     * Refresh the restaurants from the repository.
     *
     * This function fetches the restaurants from the api and updates the UI state.
     * If the API call is successful, it sets the API call state to Success.
     * If the API call fails due to a network error, it sets the API call state to NetworkError.
     */
    fun refreshRestaurants() {
        viewModelScope.launch {
            restaurantApiState = RestaurantApiState.Loading
            restaurantApiState = try {
                restaurantRepository.refreshRestaurantList()
                getRestaurants()
                RestaurantApiState.Success
            } catch (e: IOException) {
                Log.d("RestaurantViewModel", "getRestaurants: ${e.message}")
                RestaurantApiState.NetworkError
            }
        }
    }

    /**
     * Filter the restaurants by a search query.
     *
     * This function fetches the restaurants that match the given search query from the repository
     * and updates the UI state.
     * If the API call is successful, it updates the UI state with the fetched restaurants.
     */
    fun filterRestaurants(query: String) {

        currentQuery = query

        viewModelScope.launch {
            restaurantRepository.getFilteredRestaurants(currentQuery).collect { restaurants ->
                Log.d("RestaurantViewModel", "filterRestaurants: ${restaurants.size}")
                _uiState.update {
                    it.copy(restaurants = restaurants)
                }
                //We cancel the job to avoid unnecessary updates from shared flow
                cancel()
            }
        }
    }

    /**
     * Update the favorite status of a restaurant.
     *
     * This function toggles the favorite status of the given restaurant
     * and updates the restaurant in the repository.
     * It also updates the UI state with the updated restaurant.
     *
     * @param restaurant The restaurant to update.
     */
    fun updateFavorite(restaurant: Restaurant) {
        viewModelScope.launch {
            restaurant.isFavorite = !restaurant.isFavorite
            restaurantRepository.updateRestaurant(restaurant)
            _uiState.update {
                it.copy(restaurants = it.restaurants.map { currentRestaurant ->
                    if (currentRestaurant.id == restaurant.id) {
                        currentRestaurant.copy(isFavorite = !currentRestaurant.isFavorite)
                    } else {
                        currentRestaurant
                    }
                })
            }
        }
    }

    /**
     * Factory for creating RestaurantViewModel instances.
     *
     * This factory uses the application container to get the restaurant repository
     * and creates a new RestaurantViewModel instance with it.
     */
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as LoofMealsApplication)
                val restaurantRepository = application.container.IRestaurantRepository
                RestaurantViewModel(restaurantRepository)
            }
        }
    }
}