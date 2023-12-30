package com.example.loofmeals.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.twotone.Favorite
import androidx.compose.material.icons.twotone.Info
import androidx.compose.material.icons.twotone.Map
import androidx.compose.material.icons.twotone.Restaurant
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.loofmeals.R
import com.example.loofmeals.data.general.NavigationItem

/**
 * Composable function that displays a list of navigation items in the navigation drawer.
 *
 * This function creates a list of NavigationDrawerItems for each item in the navigationItems list.
 * Each NavigationDrawerItem has a label, an icon, and an onClick event handler.
 * The onClick event handler navigates to the corresponding screen and closes the navigation drawer.
 *
 * @return A list of NavigationItems for the navigation drawer.
 */
@Composable
fun navigationItems(): List<NavigationItem> {
    return listOf(
        NavigationItem(
            title = stringResource(R.string.overview),
            selectedIcon = Icons.Filled.RestaurantMenu,
            unselectedIcon = Icons.TwoTone.Restaurant,
        ),
        NavigationItem(
            title = stringResource(R.string.favorites),
            selectedIcon = Icons.Filled.Favorite,
            unselectedIcon = Icons.TwoTone.Favorite,
        ),
        NavigationItem(
            title = stringResource(R.string.about),
            selectedIcon = Icons.Filled.Info,
            unselectedIcon = Icons.TwoTone.Info,
        ),
        NavigationItem(
            title = stringResource(R.string.map),
            selectedIcon = Icons.Filled.Map,
            unselectedIcon = Icons.TwoTone.Map,
        ),
    )
}