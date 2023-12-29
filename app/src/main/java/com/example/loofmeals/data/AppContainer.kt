package com.example.loofmeals.data

import android.content.Context
import com.example.loofmeals.data.database.RestaurantDb
import com.example.loofmeals.network.RestaurantApiService
import com.example.loofmeals.util.Constants
import com.example.loofmeals.util.NetworkInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Interface for the application container.
 *
 * This interface defines the properties that should be provided by the application container.
 */
interface AppContainer {
    val IRestaurantRepository: IRestaurantRepository
}

/**
 * Default implementation of the AppContainer interface.
 *
 * This class provides the default implementation of the AppContainer interface. It creates and provides
 * the instances of the RestaurantRepository, Retrofit service, and the Restaurant database.
 *
 * @property context The context to create the database instance and the network interceptor.
 */
class DefaultAppContainer(private val context: Context) : AppContainer {

    // Retrofit instance for making network requests
    private val retrofit = Retrofit.Builder().baseUrl(Constants.BASE_URL).client(
        OkHttpClient.Builder().addNetworkInterceptor(
            NetworkInterceptor(context)
        ).build()
    ).addConverterFactory(GsonConverterFactory.create()).build()

    // Retrofit service for the Restaurant API
    private val retrofitService: RestaurantApiService by lazy {
        retrofit.create(RestaurantApiService::class.java)
    }

    /**
     * The RestaurantRepository instance.
     *
     * This instance is created lazily and uses the Retrofit service and the Restaurant database DAO.
     */
    override val IRestaurantRepository: IRestaurantRepository by lazy {
        CachingRestaurantRepository(
            retrofitService,
            RestaurantDb.getInstance(context).restaurantDao()
        )
    }

}