package com.example.loofmeals.ui.restaurant

import com.example.loofmeals.data.model.Restaurant

data class RestaurantOverviewState(
    val restaurants: List<Restaurant> = emptyList(),
)

sealed interface RestaurantApiState {

    object Success : RestaurantApiState
    object Loading : RestaurantApiState
    object Error : RestaurantApiState

}