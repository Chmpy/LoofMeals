package com.example.loofmeals.ui.map

/**
 * Data class representing a restaurant marker on the map.
 *
 * @property lat The latitude of the restaurant marker.
 * @property long The longitude of the restaurant marker.
 * @property title The title of the restaurant marker.
 * @property id The unique identifier of the restaurant marker.
 */
data class RestaurantMarker(val lat: Double, val long: Double, val title: String, val id : Int)

/**
 * Data class representing the state of the map.
 *
 * @property markers The list of restaurant markers on the map. The default value is an empty list.
 */
data class MapState(
    val markers: List<RestaurantMarker> = emptyList(),
)

/**
 * Sealed interface representing the state of the API call to fetch the restaurant markers.
 *
 * This interface has three possible states: Success, Loading, and Error.
 */
sealed interface MapApiState {

    /**
     * Object representing the success state of the API call.
     *
     * This state indicates that the API call was successful.
     */
    data object Success : MapApiState

    /**
     * Object representing the loading state of the API call.
     *
     * This state indicates that the API call is in progress.
     */
    data object Loading : MapApiState

    /**
     * Object representing the error state of the API call.
     *
     * This state indicates that the API call failed.
     */
    data object Error : MapApiState

}