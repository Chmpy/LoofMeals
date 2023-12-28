package com.example.loofmeals.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import okio.IOException

class NetworkInterceptor(val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = chain.run {
        if (!isConnected(context = context)) {
            Log.i("NetworkInterceptor", "intercept: No internet connection")
            throw IOException()

        } else {
            val builder = chain.request().newBuilder()
            return@run chain.proceed(builder.build())
        }

    }

    fun isConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val actNw =
            connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        val result = when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }

        return result
    }
}