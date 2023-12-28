package com.example.loofmeals.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for the restaurants table.
 */
@Dao
interface RestaurantDao {

    /**
     * Insert a restaurant into the database. If the restaurant already exists, ignore the conflict.
     *
     * @param restaurantEntity The restaurant to insert.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRestaurant(restaurantEntity: RestaurantEntity)

    /**
     * Get a restaurant by its id.
     *
     * @param id The id of the restaurant to retrieve.
     * @return A Flow emitting the restaurant with the given id.
     */
    @Query("SELECT * FROM restaurants WHERE id = :id")
    fun getRestaurantById(id: Int): Flow<RestaurantEntity>

    /**
     * Get all restaurants from the database.
     *
     * @return A Flow emitting a list of all restaurants.
     */
    @Query("SELECT * FROM restaurants")
    fun getAllRestaurants(): Flow<List<RestaurantEntity>>

    /**
     * Get all favorite restaurants from the database.
     *
     * @return A Flow emitting a list of all favorite restaurants.
     */
    @Query("SELECT * FROM restaurants WHERE isFavorite = 1")
    fun getFavoriteRestaurants(): Flow<List<RestaurantEntity>>

    /**
     * Update a restaurant in the database.
     *
     * @param restaurantEntity The restaurant to update.
     */
    @Update
    suspend fun updateRestaurant(restaurantEntity: RestaurantEntity)

    /**
     * Get restaurants from the database that match the given query. The query is matched against
     * the name, main city name, and postal code of the restaurants. The matching is case-insensitive.
     *
     * @param query The query to match against.
     * @return A Flow emitting a list of restaurants that match the query.
     */
    @Query(
        "SELECT * FROM restaurants WHERE LOWER(name) LIKE '%' || LOWER(:query) || '%' OR" +
                " LOWER(mainCityName) LIKE '%' || LOWER(:query) || '%' OR" +
                " LOWER(postalCode) LIKE '%' || LOWER(:query) || '%'"
    )
    fun getFilteredRestaurants(query: String): Flow<List<RestaurantEntity>>
}