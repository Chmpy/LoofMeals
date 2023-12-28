package com.example.loofmeals.ui.restaurant

import com.example.loofmeals.data.model.Restaurant

/**
 * Data class representing the state of the restaurant detail.
 *
 * @property restaurant The restaurant to display in the detail. The default value is a Restaurant with id 0.
 */
data class RestaurantDetailState(
    val restaurant: Restaurant = Restaurant(0)
)

/**
 * Sealed interface representing the state of the API call to fetch the restaurant detail.
 *
 * This interface has three possible states: Success, Loading, and Error.
 */
sealed interface RestaurantDetailApiState {

    /**
     * Object representing the success state of the API call.
     *
     * This state indicates that the API call was successful.
     */
    data object Success : RestaurantDetailApiState

    /**
     * Object representing the loading state of the API call.
     *
     * This state indicates that the API call is in progress.
     */
    data object Loading : RestaurantDetailApiState

    /**
     * Object representing the error state of the API call.
     *
     * This state indicates that the API call failed.
     */
    data object Error : RestaurantDetailApiState
}