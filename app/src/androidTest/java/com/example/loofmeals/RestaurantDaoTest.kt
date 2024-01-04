package com.example.loofmeals

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.loofmeals.data.database.RestaurantDao
import com.example.loofmeals.data.database.RestaurantDb
import com.example.loofmeals.data.database.asRestaurantEntity
import com.example.loofmeals.data.model.Restaurant
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class RestaurantDaoTest {
    private lateinit var restaurantDao: RestaurantDao
    private lateinit var restaurantDb: RestaurantDb

    private val restaurant1 = Restaurant(1, name = "Restaurant 1")
    private val restaurant2 = Restaurant(2, name = "Restaurant 2")
    private val restaurant3 = Restaurant(3, name = "Restaurant 3", isFavorite = true)

    private suspend fun insertRestaurants() {
        restaurantDao.insertRestaurant(restaurant1.asRestaurantEntity())
        restaurantDao.insertRestaurant(restaurant2.asRestaurantEntity())
        restaurantDao.insertRestaurant(restaurant3.asRestaurantEntity())
    }

    private suspend fun insertFirstTwoRestaurant() {
        restaurantDao.insertRestaurant(restaurant1.asRestaurantEntity())
        restaurantDao.insertRestaurant(restaurant2.asRestaurantEntity())
    }

    @Before
    fun setup() {
        val context: Context = ApplicationProvider.getApplicationContext()

        restaurantDb = Room.inMemoryDatabaseBuilder(
            context, RestaurantDb::class.java
        ).allowMainThreadQueries().build()

        restaurantDao = restaurantDb.restaurantDao()
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        restaurantDb.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetRestaurant() = runBlocking {
        insertFirstTwoRestaurant()

        val restaurantList = restaurantDao.getAllRestaurants().first()

        assertEquals(2, restaurantList.size)
        assertEquals(restaurant1.asRestaurantEntity(), restaurantList[0])
        assertEquals(restaurant2.asRestaurantEntity(), restaurantList[1])
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetFavoriteRestaurant() = runBlocking {
        insertRestaurants()

        val restaurantList = restaurantDao.getFavoriteRestaurants().first()

        assertEquals(1, restaurantList.size)
        assertEquals(restaurant3.asRestaurantEntity(), restaurantList[0])
    }

    @Test
    @Throws(Exception::class)
    fun updateAndGetFavoriteRestaurant() = runBlocking {
        insertRestaurants()

        restaurantDao.updateRestaurant(restaurant3.asRestaurantEntity().copy(isFavorite = false))

        val restaurantList = restaurantDao.getFavoriteRestaurants().first()

        assertEquals(0, restaurantList.size)
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetFilteredRestaurant() = runBlocking {
        insertRestaurants()

        val restaurantList = restaurantDao.getFilteredRestaurants("Restaurant").first()

        assertEquals(3, restaurantList.size)
        assertEquals(restaurant1.asRestaurantEntity(), restaurantList[0])
        assertEquals(restaurant2.asRestaurantEntity(), restaurantList[1])
        assertEquals(restaurant3.asRestaurantEntity(), restaurantList[2])
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetFilteredRestaurantByName() = runBlocking {
        insertRestaurants()

        val restaurantList = restaurantDao.getFilteredRestaurants("1").first()

        assertEquals(1, restaurantList.size)
        assertEquals(restaurant1.asRestaurantEntity(), restaurantList[0])
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetRestaurantById() = runBlocking {
        insertRestaurants()

        val restaurant = restaurantDao.getRestaurantById(1).first()

        assertEquals(restaurant1.asRestaurantEntity(), restaurant)
    }

    @Test
    @Throws(Exception::class)
    fun updateAndGetRestaurantById() = runBlocking {
        insertRestaurants()

        restaurantDao.updateRestaurant(restaurant1.asRestaurantEntity().copy(isFavorite = true))

        val restaurant = restaurantDao.getRestaurantById(1).first()

        assertEquals(restaurant1.asRestaurantEntity().copy(isFavorite = true), restaurant)
    }
}