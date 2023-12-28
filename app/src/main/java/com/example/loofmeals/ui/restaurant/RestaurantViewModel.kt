package com.example.loofmeals.ui.restaurant

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.loofmeals.LoofMealsApplication
import com.example.loofmeals.data.RestaurantRepository
import com.example.loofmeals.data.model.Restaurant
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okio.IOException

class RestaurantViewModel(private val restaurantRepository: RestaurantRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(RestaurantOverviewState())
    val uiState: StateFlow<RestaurantOverviewState> = _uiState.asStateFlow()

    var restaurantApiState: RestaurantApiState by mutableStateOf(RestaurantApiState.Loading)
        private set

    private var currentQuery: String = ""

//    private var searchJob: Job? = null

    init {
        getRestaurants()
    }

    private fun getRestaurants() {
        viewModelScope.launch {
            restaurantApiState = RestaurantApiState.Loading
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
            } catch (e: Exception) {
                Log.d("RestaurantViewModel", "getRestaurants: ${e.message}")
                restaurantApiState = RestaurantApiState.Error
                if (e is IOException) {
                    restaurantApiState = RestaurantApiState.NetworkError
                }
            }
        }
    }

    fun refreshRestaurants() {
        viewModelScope.launch {
            restaurantApiState = RestaurantApiState.Loading
            restaurantApiState = try {
                restaurantRepository.refreshRestaurantList()
                RestaurantApiState.Success
            } catch (e: IOException) {
                Log.d("RestaurantViewModel", "getRestaurants: ${e.message}")
                RestaurantApiState.NetworkError
            }
        }
    }


    fun filterRestaurants(query: String) {

        currentQuery = query

        /*If we want to change the ux to change, less bouncy and less frequent updates, we can use debouncing*/
//        // Cancel the previous debounced job
//        searchJob?.cancel()
//
//        // Start a new debounced job
//        searchJob = viewModelScope.launch {
//            restaurantRepository.getFilteredRestaurants(query)
//                .debounce(300) // Debounce with a delay of 300 milliseconds
//                .collect { restaurants ->
//                    _uiState.update {
//                        it.copy(restaurants = restaurants)
//                    }
//                }
//        }

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

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as LoofMealsApplication)
                val restaurantRepository = application.container.restaurantRepository
                RestaurantViewModel(restaurantRepository)
            }
        }
    }
}