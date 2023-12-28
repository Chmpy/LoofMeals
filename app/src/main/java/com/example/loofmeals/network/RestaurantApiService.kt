package com.example.loofmeals.network

import com.example.loofmeals.data.model.Restaurant
import com.example.loofmeals.util.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.http.GET
import retrofit2.http.Url

/**
 * Interface for the Restaurant API service.
 *
 * This interface defines the endpoints of the Restaurant API.
 */
interface RestaurantApiService {

    /**
     * Get a list of all restaurants.
     *
     * This function fetches the list of all restaurants from the given URL.
     *
     * @param url The URL to fetch the restaurants from.
     * @return A list of ApiRestaurant objects.
     */
    @GET
    suspend fun getRestaurantList(@Url url: String): List<ApiRestaurant>
}

/**
 * Extension function to get a list of all restaurants as a Flow.
 *
 * This function fetches the list of all restaurants from the Restaurant API
 * and converts it to a Flow.
 *
 * @return A Flow emitting a list of Restaurant objects.
 */
fun RestaurantApiService.getRestaurantsAsFlow(): Flow<List<Restaurant>> = flow {
    emit(getRestaurantList(Constants.BASE_URL).asDomainObject())
}