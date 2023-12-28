package com.example.loofmeals.data.general

import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Data class representing a navigation item in the application.
 *
 * @property title The title of the navigation item.
 * @property selectedIcon The icon to display when the navigation item is selected.
 * @property unselectedIcon The icon to display when the navigation item is not selected.
 * @property badgeCount The number to display as a badge on the navigation item. If null, no badge is displayed.
 */
data class NavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeCount: Int? = null
)