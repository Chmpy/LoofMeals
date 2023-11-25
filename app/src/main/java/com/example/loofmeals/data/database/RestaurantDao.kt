package com.example.loofmeals.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RestaurantDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRestaurant(restaurantEntity: RestaurantEntity)

    @Query("SELECT * FROM restaurants WHERE id = :id")
    fun getRestaurantById(id: Int): Flow<RestaurantEntity>

    @Query("SELECT * FROM restaurants")
    fun getAllRestaurants(): Flow<List<RestaurantEntity>>

}