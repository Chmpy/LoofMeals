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

/**
 * Interface for the restaurant repository.
 *
 * This interface defines the operations that can be performed on the restaurant data.
 */
interface IRestaurantRepository {
    fun getRestaurantList(): Flow<List<Restaurant>>
    fun getFilteredRestaurants(query: String): Flow<List<Restaurant>>
    fun getRestaurantById(id: Int): Flow<Restaurant>
    fun getFavoriteRestaurants(): Flow<List<Restaurant>>
    suspend fun updateRestaurant(restaurant: Restaurant)
    suspend fun insertRestaurant(restaurant: Restaurant)
    suspend fun refreshRestaurantList()
}

/**
 * Implementation of the RestaurantRepository interface that caches the restaurant data.
 *
 * This class fetches the restaurant data from the network and stores it in a local database.
 * When fetching the restaurant data, it first tries to fetch it from the local database.
 * If the local database is empty, it fetches the data from the network and stores it in the local database.
 *
 * @property restaurantApiService The service to fetch the restaurant data from the network.
 * @property restaurantDao The DAO to fetch and store the restaurant data in the local database.
 */
class CachingRestaurantRepository(
    private val restaurantApiService: RestaurantApiService,
    private val restaurantDao: RestaurantDao
) : IRestaurantRepository {
    /**
     * Get a list of all restaurants.
     *
     * This function fetches the restaurants from the local database. If the local database is empty,
     * it fetches the restaurants from the network and stores them in the local database.
     *
     * @return A Flow emitting a list of all restaurants.
     */
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

    /**
     * Get a list of restaurants that match the given query.
     *
     * This function fetches the restaurants from the local database that match the given query.
     *
     * @param query The query to match against.
     * @return A Flow emitting a list of restaurants that match the query.
     */
    override fun getFilteredRestaurants(query: String): Flow<List<Restaurant>> {
        Log.d("CachingRestaurantRepository", "getFilteredRestaurants: Fetching with query: $query")
        return restaurantDao.getFilteredRestaurants(query).map { restaurantList ->
            restaurantList.asDomainObject()
        }
    }

    /**
     * Get a restaurant by its id.
     *
     * This function fetches the restaurant with the given id from the local database.
     *
     * @param id The id of the restaurant to retrieve.
     * @return A Flow emitting the restaurant with the given id.
     */
    override fun getRestaurantById(id: Int): Flow<Restaurant> {
        Log.d("CachingRestaurantRepository", "getRestaurantById: Fetching with id: $id")
        return restaurantDao.getRestaurantById(id).map { it.asDomainObject() }
    }

    /**
     * Get a list of all favorite restaurants.
     *
     * This function fetches the favorite restaurants from the local database.
     *
     * @return A Flow emitting a list of all favorite restaurants.
     */
    override fun getFavoriteRestaurants(): Flow<List<Restaurant>> {
        Log.d("CachingRestaurantRepository", "getFavoriteRestaurants: Fetching favorites")
        return restaurantDao.getFavoriteRestaurants().map { restaurantList ->
            restaurantList.asDomainObject()
        }
    }

    /**
     * Update a restaurant in the repository.
     *
     * This function updates the given restaurant in the local database.
     *
     * @param restaurant The restaurant to update.
     */
    override suspend fun updateRestaurant(restaurant: Restaurant) {
        Log.d("CachingRestaurantRepository", "updateRestaurant: Updating restaurant with id: ${restaurant.id}")
        restaurantDao.updateRestaurant(restaurant.asRestaurantEntity())
    }

    /**
     * Insert a restaurant into the repository.
     *
     * This function inserts the given restaurant into the local database.
     *
     * @param restaurant The restaurant to insert.
     */
    override suspend fun insertRestaurant(restaurant: Restaurant) {
        restaurantDao.insertRestaurant(restaurant.asRestaurantEntity())
    }

    /**
     * Refresh the list of restaurants from the network.
     *
     * This function fetches the restaurants from the network and stores them in the local database.
     */
    override suspend fun refreshRestaurantList() {
        Log.d("CachingRestaurantRepository", "refreshRestaurantList: Refreshing from network")
        restaurantApiService.getRestaurantsAsFlow().collect { restaurantList ->
            for (restaurant in restaurantList) {
                insertRestaurant(restaurant)
            }
        }
    }
}