package com.example.loofmeals.ui.favorite

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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for the Favorite screen.
 *
 * This ViewModel fetches the favorite restaurants from the repository and updates the UI state.
 *
 * @property restaurantRepository The repository to fetch the favorite restaurants from.
 */
class FavoriteViewModel(private val restaurantRepository: IRestaurantRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoriteState())
    val uiState: StateFlow<FavoriteState> = _uiState.asStateFlow()

    // The state of the API call to fetch the favorite restaurants.
    // This is a mutable state that is updated every time the API call state changes.
    var favoriteApiState: FavoriteApiState by mutableStateOf(FavoriteApiState.Loading)
        private set

    // Fetch the favorite restaurants when the ViewModel is initialized.
    init {
        getFavoriteRestaurants()
    }

    /**
     * Fetch the favorite restaurants from the repository.
     *
     * This function fetches the favorite restaurants from the repository and updates the UI state.
     * If the API call is successful,
     * it updates the UI state with the fetched restaurants and sets the API call state to Success.
     * If the API call fails, it sets the API call state to Error.
     */
    fun getFavoriteRestaurants() {
        viewModelScope.launch {
            favoriteApiState = FavoriteApiState.Loading
            try {
                restaurantRepository.getFavoriteRestaurants().collect { restaurants ->
                    Log.d("FavoriteViewModel", "getFavoriteRestaurants: ${restaurants.size}")
                    _uiState.value = FavoriteState(restaurants)
                    favoriteApiState = FavoriteApiState.Success
                }
            } catch (e: Exception) {
                favoriteApiState = FavoriteApiState.Error
            }
        }
    }

    /**
     * Update the favorite status of a restaurant.
     *
     * This function toggles the favorite status of the given restaurant
     * and updates the restaurant in the repository.
     *
     * @param restaurant The restaurant to update.
     */
    fun updateFavorite(restaurant: Restaurant) {
        viewModelScope.launch {
            restaurant.isFavorite = !restaurant.isFavorite
            restaurantRepository.updateRestaurant(restaurant)
        }
    }

    /**
     * Factory for creating FavoriteViewModel instances.
     *
     * This factory uses the application container to get the restaurant repository
     * and creates a new FavoriteViewModel instance with it.
     */
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as LoofMealsApplication)
                val restaurantRepository = application.container.IRestaurantRepository
                FavoriteViewModel(restaurantRepository)
            }
        }
    }
}