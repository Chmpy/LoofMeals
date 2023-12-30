package com.example.loofmeals.ui

import androidx.activity.compose.BackHandler
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.loofmeals.data.general.NavigationItem
import com.example.loofmeals.ui.components.NavigationDrawerExpanded
import com.example.loofmeals.ui.components.NavigationDrawerSmall
import com.example.loofmeals.ui.components.NavigationRailMedium
import com.example.loofmeals.ui.components.navigationItems
import com.example.loofmeals.ui.util.NavigationType
import com.example.loofmeals.ui.util.Screens
import kotlinx.coroutines.launch

/**
 * Composable function that displays the main application screen.
 *
 * Based on the size of the window, this function displays the appropriate navigation UI.
 *
 * @param windowSize The size of the window.
 * @param navController The NavController that manages the navigation between the screens.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoofMealsApp(
    windowSize: WindowWidthSizeClass = WindowWidthSizeClass.Compact,
    navController: NavHostController = rememberNavController()
) {
    val items: List<NavigationItem> = navigationItems()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navigationType: NavigationType

    BackHandler(drawerState.isOpen) {
        scope.launch { drawerState.close() }
    }

    val goToOverview: () -> Unit = {
        navController.popBackStack(Screens.Overview.name, inclusive = false)
    }
    val goToFavorite: () -> Unit = {
        navController.navigate(Screens.Favorites.name)
    }
    val goToAbout: () -> Unit = {
        navController.navigate(Screens.About.name)
    }

    val goToMap: () -> Unit = {
        navController.navigate(Screens.Map.name)
    }

    when (windowSize) {
        WindowWidthSizeClass.Compact -> {
            navigationType = NavigationType.VAR_NAVIGATION_DRAWER
            NavigationDrawerSmall(
                navigationType = navigationType,
                items = items,
                drawerState = drawerState,
                scope = scope,
                navController = navController,
                goToOverview = goToOverview,
                goToFavorite = goToFavorite,
                goToAbout = goToAbout,
                goToMap = goToMap,
            )
        }

        WindowWidthSizeClass.Medium -> {
            navigationType = NavigationType.NAVIGATION_RAIL
            NavigationRailMedium(
                navigationType = navigationType,
                items = items,
                scope = scope,
                navController = navController,
                goToOverview = goToOverview,
                goToFavorite = goToFavorite,
                goToAbout = goToAbout,
                goToMap = goToMap,
            )
        }

        WindowWidthSizeClass.Expanded -> {
            navigationType = NavigationType.PERMANENT_NAVIGATION_DRAWER
            NavigationDrawerExpanded(
                navigationType = navigationType,
                items = items,
                scope = scope,
                navController = navController,
                goToOverview = goToOverview,
                goToFavorite = goToFavorite,
                goToAbout = goToAbout,
                goToMap = goToMap,
            )
        }
    }
}