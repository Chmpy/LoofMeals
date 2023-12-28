package com.example.loofmeals

import android.app.Application
import com.example.loofmeals.data.AppContainer
import com.example.loofmeals.data.DefaultAppContainer

/**
 * Application class for the LoofMeals app.
 *
 * This class initializes the application container when the application is created.
 */
class LoofMealsApplication: Application() {
    /**
     * The application container for the LoofMeals app.
     *
     * This container provides the instances of the RestaurantRepository,
     * Retrofit service, and the Restaurant database.
     */
    lateinit var container : AppContainer

    /**
     * Called when the application is starting,
     * before any activity, service, or receiver objects have been created.
     *
     * This function initializes the application container with the application context.
     */
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(applicationContext)
    }
}