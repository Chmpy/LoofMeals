package com.example.loofmeals.data

import android.content.Context
import com.example.loofmeals.data.database.RestaurantDb
import com.example.loofmeals.network.RestaurantApiService
import com.example.loofmeals.util.Constants
import com.example.loofmeals.util.NetworkInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AppContainer {
    val restaurantRepository: RestaurantRepository
}

class DefaultAppContainer(private val context: Context) : AppContainer {

    private val retrofit = Retrofit.Builder().baseUrl(Constants.BASE_URL).client(
        OkHttpClient.Builder().addNetworkInterceptor(
            NetworkInterceptor(context)
        ).build()
    ).addConverterFactory(GsonConverterFactory.create()).build()

    private val retrofitService: RestaurantApiService by lazy {
        retrofit.create(RestaurantApiService::class.java)
    }

    override val restaurantRepository: RestaurantRepository by lazy {
        CachingRestaurantRepository(
            retrofitService,
            RestaurantDb.getInstance(context).restaurantDao()
        )
    }

}