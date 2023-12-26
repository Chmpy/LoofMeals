package com.example.loofmeals.ui.favorite

import com.example.loofmeals.data.model.Restaurant

data class FavoriteState(
    val restaurants: List<Restaurant> = emptyList()
)

sealed interface FavoriteApiState {

    data object Success : FavoriteApiState
    data object Loading : FavoriteApiState
    data object Error : FavoriteApiState
}