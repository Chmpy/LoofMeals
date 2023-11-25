package com.example.loofmeals.network

import com.example.loofmeals.data.model.Restaurant
import com.example.loofmeals.util.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.http.GET
import retrofit2.http.Url

interface RestaurantApiService {

    @GET
    suspend fun getRestaurantList(@Url url: String): List<ApiRestaurant>
}

/*Helper function*/
fun RestaurantApiService.getRestaurantsAsFlow(): Flow<List<Restaurant>> = flow {
    emit(getRestaurantList(Constants.BASE_URL).asDomainObject())
}