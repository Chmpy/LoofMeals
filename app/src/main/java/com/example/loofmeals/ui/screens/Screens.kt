package com.example.loofmeals.ui.screens

import androidx.annotation.StringRes
import com.example.loofmeals.R

/**
 * Enum class representing the different screens in the application.
 *
 * Each screen has a title and a route. The title is a string resource id that represents the title of the screen.
 * The route is a string that represents the route of the screen in the navigation graph.
 *
 * @property title The string resource id of the title of the screen.
 * @property route The route of the screen in the navigation graph.
 * @property id The id of the screen. This is used for screens that require an id parameter in their route.
 */
enum class Screens(@StringRes val title: Int, val route: String? = null, val id: String? = null) {
    Overview(title = R.string.app_name, route = "overview"),
    Favorites(title = R.string.favorites, route = "favorites"),
    About(title = R.string.about, route = "about"),
    Detail(title = R.string.detail, route = "Detail/{restaurantId}", id = "restaurantId"),
    Map(title = R.string.map, route = "map");

    companion object {
        /**
         * Function to get a screen by its route.
         *
         * This function finds the screen whose route starts with the given route.
         *
         * @param route The route to find the screen by.
         * @return The screen whose route starts with the given route, or null if no such screen is found.
         */
        fun getScreenByRoute(route: String): Screens? =
            values().find { it.route?.let { r -> route.startsWith(r, ignoreCase = true) } ?: false }
    }
}