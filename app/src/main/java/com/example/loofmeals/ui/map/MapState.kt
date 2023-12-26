package com.example.loofmeals.ui.map

data class RestaurantMarker(val lat: Double, val long: Double, val title: String, val id : Int)
data class MapState(
    val markers: List<RestaurantMarker> = emptyList(),
)

sealed interface MapApiState {

    data object Success : MapApiState
    data object Loading : MapApiState
    data object Error : MapApiState

}