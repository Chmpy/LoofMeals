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
import com.example.loofmeals.R
import com.example.loofmeals.data.RestaurantRepository
import com.example.loofmeals.data.model.Restaurant
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RestaurantViewModel(private val restaurantRepository: RestaurantRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(RestaurantOverviewState())
    val uiState: StateFlow<RestaurantOverviewState> = _uiState.asStateFlow()

    var restaurantApiState: RestaurantApiState by mutableStateOf(RestaurantApiState.Loading)
        private set

    private var originalRestaurants: List<Restaurant> = emptyList()

    init {
        getRestaurants()
    }

    private fun getRestaurants() {
        viewModelScope.launch {
            restaurantApiState = try {
                val restaurants = restaurantRepository.getRestaurantList()
                originalRestaurants = restaurants
                _uiState.update {
                    it.copy(restaurants = restaurants)
                }
                RestaurantApiState.Success(restaurants)
            } catch (e: Exception) {
                Log.d("RestaurantViewModel", "getRestaurants: ${e.message}")
                RestaurantApiState.Error(R.string.restaurants_get_error.toString())
            }
        }
    }

    fun filterRestaurants(query: String) {
        val filteredRestaurants = originalRestaurants.filter { restaurant ->
            listOf(
                restaurant.name?.contains(query, ignoreCase = true) ?: false,
                restaurant.mainCityName?.contains(query, ignoreCase = true) ?: false,
                restaurant.postalCode?.contains(query, ignoreCase = true) ?: false
            ).any { it }
        }
        _uiState.update {
            it.copy(restaurants = filteredRestaurants)
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