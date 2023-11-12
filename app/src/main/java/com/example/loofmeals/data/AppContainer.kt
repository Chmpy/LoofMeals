package com.example.loofmeals.data

import com.example.loofmeals.network.RestaurantService
import com.example.loofmeals.util.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AppContainer {
    val restaurantRepository: RestaurantRepository
}

class DefaultAppContainer : AppContainer {

    private val retrofit = Retrofit.Builder().baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create()).build()

    private val retrofitService: RestaurantService by lazy {
        retrofit.create(RestaurantService::class.java)
    }

    override val restaurantRepository: RestaurantRepository by lazy {
        ApiRestaurantRepository(retrofitService)
    }

}