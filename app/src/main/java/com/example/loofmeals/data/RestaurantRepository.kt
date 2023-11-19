package com.example.loofmeals.data

import com.example.loofmeals.data.model.Restaurant
import com.example.loofmeals.network.RestaurantApiService
import com.example.loofmeals.network.asDomainObject
import com.example.loofmeals.util.Constants

interface RestaurantRepository {
    suspend fun getRestaurantList(): List<Restaurant>
}

class ApiRestaurantRepository(
    private val restaurantApiService: RestaurantApiService
) : RestaurantRepository {
    override suspend fun getRestaurantList(): List<Restaurant> {
        return restaurantApiService.getRestaurantList(Constants.BASE_URL).asDomainObject()
    }
}