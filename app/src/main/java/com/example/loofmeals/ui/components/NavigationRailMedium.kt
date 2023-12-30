package com.example.loofmeals.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.loofmeals.R
import com.example.loofmeals.data.general.NavigationItem
import com.example.loofmeals.ui.layout.RootLayout
import com.example.loofmeals.ui.util.NavigationType
import com.example.loofmeals.ui.util.Screens
import kotlinx.coroutines.CoroutineScope

/**
 * Composable function that creates a medium-sized navigation rail using the Material 3 design
 * components, incorporating a [NavigationRail] and [NavigationRailItem]. The navigation rail
 * displays a list of [NavigationItem] items with icons, allowing users to navigate between
 * different destinations.
 * This version is suitable for medium-sized screens or contexts where a full-width drawer is not
 * ideal, such as tablet-sized layouts.
 *
 * @param navigationType The type of navigation, used in the [RootLayout] for configuring the
 * interaction with the content of the screen.
 * @param items List of [NavigationItem] representing each navigation destination in the rail.
 * @param scope [CoroutineScope] used for managing coroutine lifecycles.
 * @param navController [NavHostController] used for navigation between destinations.
 * @param goToOverview Callback function to navigate to the Overview destination.
 * @param goToFavorite Callback function to navigate to the Favorites destination.
 * @param goToAbout Callback function to navigate to the About destination.
 * @param goToMap Callback function to navigate to the Map destination.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationRailMedium(
    navigationType: NavigationType,
    items: List<NavigationItem>,
    scope: CoroutineScope,
    navController: NavHostController,
    goToOverview: () -> Unit,
    goToFavorite: () -> Unit,
    goToAbout: () -> Unit,
    goToMap: () -> Unit
) {
    // State variable to keep track of the selected item index in the navigation rail.
    var selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }

    // Effect to listen to changes in the navigation destination and update the selected item index.
    DisposableEffect(navController) {
        val listener = NavController.OnDestinationChangedListener { _, _, _ ->
            selectedItemIndex = when (navController.currentDestination?.route) {
                Screens.Overview.name -> 0
                Screens.Favorites.name -> 1
                Screens.About.name -> 2
                Screens.Map.name -> 3
                else -> 0
            }
        }
        navController.addOnDestinationChangedListener(listener)

        // Remove the listener when the composable is disposed to avoid memory leaks.
        onDispose {
            navController.removeOnDestinationChangedListener(listener)
        }
    }

    // Build the navigation rail using Material 3 components.
    Row {
        NavigationRail {
            // Create a navigation rail item for each navigation destination.
            items.forEachIndexed { index, item ->
                NavigationRailItem(
                    selected = index == selectedItemIndex,
                    onClick = {
                        // Navigate to the corresponding destination when the item is clicked.
                        when (index) {
                            0 -> goToOverview()
                            1 -> goToFavorite()
                            2 -> goToAbout()
                            3 -> goToMap()
                        }
                    },
                    icon = {
                        // Display the appropriate icon based on the selected state.
                        Icon(
                            imageVector = if (index == selectedItemIndex) item.selectedIcon else item.unselectedIcon,
                            contentDescription = item.title
                        )
                    },
                    modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.md))
                )
            }
        }

        // Include the [RootLayout] to wrap the content of the screen along with the navigation rail.
        RootLayout(
            scope = scope,
            navController = navController,
            navigationType = navigationType
        )
    }
}