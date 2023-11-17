package com.example.loofmeals.network

import com.example.loofmeals.data.model.Restaurant
import retrofit2.http.GET
import retrofit2.http.Url

interface RestaurantService {

    @GET
    suspend fun getRestaurantList(@Url url: String): List<Restaurant>
    //TODO: Implement APIRestaurant data source to future proof against changes in the API and convert to domain objects from Restaurants
}