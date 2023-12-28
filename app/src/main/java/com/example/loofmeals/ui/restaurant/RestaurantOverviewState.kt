package com.example.loofmeals.ui.restaurant

import com.example.loofmeals.data.model.Restaurant

/**
 * Data class representing the state of the restaurant overview.
 *
 * @property restaurants The list of restaurants to display in the overview. The default value is an empty list.
 */
data class RestaurantOverviewState(
    val restaurants: List<Restaurant> = emptyList()
)

/**
 * Sealed interface representing the state of the API call to fetch the restaurant overview.
 *
 * This interface has four possible states: Success, Loading, Error, and NetworkError.
 */
sealed interface RestaurantApiState {

    /**
     * Object representing the success state of the API call.
     *
     * This state indicates that the API call was successful.
     */
    data object Success : RestaurantApiState

    /**
     * Object representing the loading state of the API call.
     *
     * This state indicates that the API call is in progress.
     */
    data object Loading : RestaurantApiState

    /**
     * Object representing the error state of the API call.
     *
     * This state indicates that the API call failed.
     */
    data object Error : RestaurantApiState

    /**
     * Object representing the network error state of the API call.
     *
     * This state indicates that the API call failed due to a network error.
     */
    data object NetworkError : RestaurantApiState
}