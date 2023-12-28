package com.example.loofmeals.ui.favorite

import com.example.loofmeals.data.model.Restaurant

/**
 * Data class representing the state of the favorite restaurants.
 *
 * @property restaurants The list of favorite restaurants. The default value is an empty list.
 */
data class FavoriteState(
    val restaurants: List<Restaurant> = emptyList()
)

/**
 * Sealed interface representing the state of the API call to fetch the favorite restaurants.
 *
 * This interface has three possible states: Success, Loading, and Error.
 */
sealed interface FavoriteApiState {

    /**
     * Object representing the success state of the API call.
     *
     * This state indicates that the API call was successful.
     */
    data object Success : FavoriteApiState

    /**
     * Object representing the loading state of the API call.
     *
     * This state indicates that the API call is in progress.
     */
    data object Loading : FavoriteApiState

    /**
     * Object representing the error state of the API call.
     *
     * This state indicates that the API call failed.
     */
    data object Error : FavoriteApiState
}