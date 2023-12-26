package com.example.loofmeals.ui.map

data class RestaurantMarker(val lat: Double, val long: Double, val title: String, val id : Int)
data class MapState(
    val markers: List<RestaurantMarker> = emptyList(),
)

sealed interface MapApiState {

    object Success : MapApiState
    object Loading : MapApiState
    object Error : MapApiState

}