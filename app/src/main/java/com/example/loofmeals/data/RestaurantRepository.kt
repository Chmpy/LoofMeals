package com.example.loofmeals.data

import android.util.Log
import com.example.loofmeals.data.database.RestaurantDao
import com.example.loofmeals.data.database.asDomainObject
import com.example.loofmeals.data.database.asRestaurantEntity
import com.example.loofmeals.data.model.Restaurant
import com.example.loofmeals.network.RestaurantApiService
import com.example.loofmeals.network.getRestaurantsAsFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

interface RestaurantRepository {
    fun getRestaurantList(): Flow<List<Restaurant>>

    fun getFilteredRestaurants(query: String): Flow<List<Restaurant>>

    fun getRestaurantById(id: Int): Flow<Restaurant>

    fun getFavoriteRestaurants(): Flow<List<Restaurant>>

    suspend fun updateRestaurant(restaurant: Restaurant)

    suspend fun insertRestaurant(restaurant: Restaurant)

    suspend fun refreshRestaurantList()
}

class CachingRestaurantRepository(
    private val restaurantApiService: RestaurantApiService,
    private val restaurantDao: RestaurantDao
) : RestaurantRepository {
    override fun getRestaurantList(): Flow<List<Restaurant>> {
        //Get all restaurants from the database and map them to domain objects
        //If the list is empty, refresh the list from the network
        return restaurantDao.getAllRestaurants().map { restaurantList ->
            Log.d("CachingRestaurantRepository", "getRestaurantList: Fetching from local database")
            restaurantList.asDomainObject()
        }.onEach { restaurantList ->
            if (restaurantList.isEmpty()) {
                Log.d(
                    "CachingRestaurantRepository",
                    "getRestaurantList: Local database is empty, refreshing from network"
                )
                refreshRestaurantList()
            }
        }
    }

    override fun getFilteredRestaurants(query: String): Flow<List<Restaurant>> {
        Log.d("CachingRestaurantRepository", "getFilteredRestaurants: Fetching with query: $query")
        return restaurantDao.getFilteredRestaurants(query).map { restaurantList ->
            restaurantList.asDomainObject()
        }
    }

    override fun getRestaurantById(id: Int): Flow<Restaurant> {
        Log.d("CachingRestaurantRepository", "getRestaurantById: Fetching with id: $id")
        return restaurantDao.getRestaurantById(id).map { it.asDomainObject() }
    }

    override fun getFavoriteRestaurants(): Flow<List<Restaurant>> {
        Log.d("CachingRestaurantRepository", "getFavoriteRestaurants: Fetching favorites")
        return restaurantDao.getFavoriteRestaurants().map { restaurantList ->
            restaurantList.asDomainObject()
        }
    }

    override suspend fun updateRestaurant(restaurant: Restaurant) {
        Log.d("CachingRestaurantRepository", "updateRestaurant: Updating restaurant with id: ${restaurant.id}")
        restaurantDao.updateRestaurant(restaurant.asRestaurantEntity())
    }

    override suspend fun insertRestaurant(restaurant: Restaurant) {
        restaurantDao.insertRestaurant(restaurant.asRestaurantEntity())
    }

    override suspend fun refreshRestaurantList() {
        Log.d("CachingRestaurantRepository", "refreshRestaurantList: Refreshing from network")
        restaurantApiService.getRestaurantsAsFlow().collect { restaurantList ->
            for (restaurant in restaurantList) {
                insertRestaurant(restaurant)
            }
        }
    }
}