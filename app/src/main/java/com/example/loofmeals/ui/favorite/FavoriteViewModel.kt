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
import com.example.loofmeals.data.RestaurantRepository
import com.example.loofmeals.data.model.Restaurant
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoriteViewModel(private val restaurantRepository: RestaurantRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoriteState())
    val uiState: StateFlow<FavoriteState> = _uiState.asStateFlow()

    var favoriteApiState: FavoriteApiState by mutableStateOf(FavoriteApiState.Loading)
        private set

    init {
        getFavoriteRestaurants()
    }

    private fun getFavoriteRestaurants() {
        viewModelScope.launch {
            favoriteApiState = FavoriteApiState.Loading
            try {
                //Simulate network delay
                //kotlinx.coroutines.delay(2000)

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

    fun updateFavorite(restaurant: Restaurant) {
        viewModelScope.launch {
            restaurant.isFavorite = !restaurant.isFavorite
            restaurantRepository.updateRestaurant(restaurant)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as LoofMealsApplication)
                val restaurantRepository = application.container.restaurantRepository
                FavoriteViewModel(restaurantRepository)
            }
        }
    }
}
