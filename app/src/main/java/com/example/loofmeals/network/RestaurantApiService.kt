package com.example.loofmeals.network

import com.example.loofmeals.data.model.Restaurant
import retrofit2.http.GET
import retrofit2.http.Url

interface RestaurantApiService {

    @GET
    suspend fun getRestaurantList(@Url url: String): List<ApiRestaurant>
}