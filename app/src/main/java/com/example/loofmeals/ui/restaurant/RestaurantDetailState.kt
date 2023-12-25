package com.example.loofmeals.ui.restaurant

import com.example.loofmeals.data.model.Restaurant

data class RestaurantDetailState(
    val restaurant: Restaurant = Restaurant(0)
)

sealed interface RestaurantDetailApiState {

    object Success : RestaurantDetailApiState
    object Loading : RestaurantDetailApiState
    object Error : RestaurantDetailApiState
}