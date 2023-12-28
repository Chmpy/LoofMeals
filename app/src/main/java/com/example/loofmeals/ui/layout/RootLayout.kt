package com.example.loofmeals.ui.layout

import com.example.loofmeals.ui.screens.Screens
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.loofmeals.R
import com.example.loofmeals.ui.screens.About
import com.example.loofmeals.ui.screens.Detail
import com.example.loofmeals.ui.screens.Favorites
import com.example.loofmeals.ui.screens.Map
import com.example.loofmeals.ui.screens.Overview
import kotlinx.coroutines.CoroutineScope

/**
 * Composable function that creates the root layout of the app.
 *
 * This function creates a Scaffold composable that contains a NavHost composable.
 * The NavHost composable
 * contains the different screens of the app and manages the navigation between them.
 *
 * @param drawerState The state of the drawer. This is used to control the opening and closing of the drawer.
 * @param scope The CoroutineScope in which to launch coroutines.
 * This is used to control the lifecycle of the coroutines.
 * @param navController The NavController that manages the navigation between the screens.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RootLayout(
    drawerState: DrawerState,
    scope: CoroutineScope,
    navController: NavHostController,
) {
    // The current back stack entry. This is the destination that the user navigated to.
    val backStackEntry by navController.currentBackStackEntryAsState()

    // The title of the current screen. This is determined by the route of the current back stack entry.
    val currentScreenTitle = backStackEntry?.destination?.route?.let { route ->
        val screen = Screens.getScreenByRoute(route)
        screen?.title ?: R.string.app_name
    } ?: R.string.app_name

    // The Scaffold composable that contains the NavHost composable.
    Scaffold(
        topBar = { TopLoofBar(currentScreenTitle, scope, drawerState) },
    ) { innerPadding ->
        // The NavHost composable that contains the different screens of the app.
        NavHost(
            navController = navController,
            startDestination = Screens.Overview.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screens.Overview.name) {
                Overview(navController)
            }
            composable(Screens.Favorites.name) {
                Favorites(navController)
            }
            composable(Screens.About.name) {
                About()
            }
            composable(
                route = Screens.Detail.route ?: "",
                arguments = listOf(navArgument("restaurantId") { type = NavType.IntType })
            ) { backStackEntry ->
                Detail(
                    restaurantId = backStackEntry.arguments?.getInt("restaurantId") ?: 0,
                )
            }
            composable(Screens.Map.name) {
                Map(navController)
            }
        }
    }
}