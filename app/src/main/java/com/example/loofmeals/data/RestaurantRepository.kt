package com.example.loofmeals.data

import com.example.loofmeals.data.model.Restaurant
import com.example.loofmeals.network.RestaurantService
import com.example.loofmeals.util.Constants

interface RestaurantRepository {
    suspend fun getRestaurantList(): List<Restaurant>
}

class ApiRestaurantRepository(
    private val restaurantService: RestaurantService
) : RestaurantRepository {
    override suspend fun getRestaurantList(): List<Restaurant> {
        return restaurantService.getRestaurantList(Constants.BASE_URL)
    }
}