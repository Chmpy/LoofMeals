package com.example.loofmeals.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.loofmeals.util.Constants

/**
 * Room database class for storing restaurant data.
 *
 * This class is a singleton and provides a method to get the instance of the database.
 */
@Database(entities = [RestaurantEntity::class], version = 2, exportSchema = false)
abstract class RestaurantDb : RoomDatabase() {

    /**
     * Get the DAO for the restaurants table.
     *
     * @return The DAO for the restaurants table.
     */
    abstract fun restaurantDao(): RestaurantDao

    companion object {
        // Singleton instance of the database
        @Volatile
        private var INSTANCE: RestaurantDb? = null

        /**
         * Get the singleton instance of the database.
         *
         * If the instance is not already created, it will be created.
         * If the instance is already created, it will be returned.
         *
         * @param context The context to create the database instance.
         * @return The singleton instance of the database.
         */
        fun getInstance(context: Context): RestaurantDb {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context,
                        RestaurantDb::class.java,
                        Constants.DATABASE_NAME
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}