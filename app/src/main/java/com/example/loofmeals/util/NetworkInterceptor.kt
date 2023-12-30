package com.example.loofmeals.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import okio.IOException

/**
 * Network Interceptor class for OkHttp.
 *
 * This class intercepts every network request and checks if there is an internet connection.
 * If there is no internet connection, it throws an IOException.
 *
 * @property context The context to access the connectivity service.
 */
class NetworkInterceptor(private val context: Context) : Interceptor {
    /**
     * Intercepts the network request and checks the internet connection.
     *
     * If there is no internet connection, it logs an info message and throws an IOException.
     * If there is an internet connection, it proceeds with the network request.
     *
     * @param chain The chain of interceptors.
     * @return The response from the network request.
     * @throws IOException If there is no internet connection.
     */
    override fun intercept(chain: Interceptor.Chain): Response = chain.run {
        if (!isConnected(context = context)) {
            Log.i("NetworkInterceptor", "intercept: No internet connection")
            throw IOException()

        } else {
            val builder = chain.request().newBuilder()
            return@run chain.proceed(builder.build())
        }

    }

    /**
     * Checks if there is an internet connection.
     *
     * This function checks if there is a WI-FI, cellular, or ethernet connection.
     *
     * @param context The context to access the connectivity service.
     * @return True if there is an internet connection, false otherwise.
     */
    private fun isConnected(context: Context): Boolean {
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