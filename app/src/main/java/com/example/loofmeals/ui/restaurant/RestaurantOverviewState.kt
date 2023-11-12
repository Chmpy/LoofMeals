package com.example.loofmeals.ui.restaurant

import com.example.loofmeals.data.model.Restaurant

data class RestaurantOverviewState(
    val restaurants: List<Restaurant> = emptyList(),
)

sealed interface RestaurantApiState {
    data class Success(val restaurants: List<Restaurant>) : RestaurantApiState
    object Loading : RestaurantApiState
    data class Error(val message: String) : RestaurantApiState

}