package com.example.loofmeals.ui.restaurant

import com.example.loofmeals.data.model.Restaurant

data class RestaurantOverviewState(
    val restaurants: List<Restaurant> = emptyList()
)

sealed interface RestaurantApiState {

    data object Success : RestaurantApiState
    data object Loading : RestaurantApiState
    data object Error : RestaurantApiState
    data object NetworkError : RestaurantApiState

}