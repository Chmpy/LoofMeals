package com.example.loofmeals

import android.app.Application
import com.example.loofmeals.data.AppContainer
import com.example.loofmeals.data.DefaultAppContainer

class LoofMealsApplication: Application() {
    lateinit var container : AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}