package com.example.loofmeals.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.dimensionResource
import androidx.navigation.NavHostController
import com.example.loofmeals.R
import com.example.loofmeals.data.general.NavigationItem
import com.example.loofmeals.ui.layout.RootLayout
import com.example.loofmeals.ui.util.NavigationType
import com.example.loofmeals.ui.util.Screens
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Composable function that creates a small navigation drawer using the Material 3 design
 * components, incorporating a [ModalNavigationDrawer] and [ModalDrawerSheet]. The drawer
 * displays a list of [NavigationItem] items with icons, labels, and optional badge counts.
 * This version is suitable for smaller screens or contexts where a full-width drawer is not
 * ideal, such as mobile or tablet-sized layouts.
 *
 * @param navigationType The type of navigation, used in the [RootLayout] for configuring the
 * interaction with the content of the screen.
 * @param items List of [NavigationItem] representing each navigation destination in the drawer.
 * @param drawerState [DrawerState] representing the state of the modal navigation drawer.
 * @param scope [CoroutineScope] used for managing coroutine lifecycles.
 * @param navController [NavHostController] used for navigation between destinations.
 * @param goToOverview Callback function to navigate to the Overview destination.
 * @param goToFavorite Callback function to navigate to the Favorites destination.
 * @param goToAbout Callback function to navigate to the About destination.
 * @param goToMap Callback function to navigate to the Map destination.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationDrawerSmall(
    navigationType: NavigationType,
    items: List<NavigationItem>,
    drawerState: DrawerState,
    scope: CoroutineScope,
    navController: NavHostController,
    goToOverview: () -> Unit,
    goToFavorite: () -> Unit,
    goToAbout: () -> Unit,
    goToMap: () -> Unit
) {
    // State variable to keep track of the selected item index in the navigation drawer.
    var selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }

    // Determine the initial selected item index based on the current destination.
    selectedItemIndex = when (navController.currentDestination?.route) {
        Screens.Overview.name -> 0
        Screens.Favorites.name -> 1
        Screens.About.name -> 2
        Screens.Map.name -> 3
        else -> 0
    }

    // Build the modal navigation drawer using Material 3 components.
    ModalNavigationDrawer(drawerContent = {
        ModalDrawerSheet(modifier = Modifier.alpha(0.99f)) {
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.lg)))

            // Create a navigation drawer item for each navigation destination.
            items.forEachIndexed { index, item ->
                NavigationDrawerItem(
                    label = { Text(text = item.title) },
                    selected = index == selectedItemIndex,
                    onClick = {
                        // Navigate to the corresponding destination when the item is clicked.
                        when (index) {
                            0 -> goToOverview()
                            1 -> goToFavorite()
                            2 -> goToAbout()
                            3 -> goToMap()
                        }
                        // Close the drawer after navigating.
                        scope.launch { drawerState.close() }
                    },
                    icon = {
                        // Display the appropriate icon based on the selected state.
                        Icon(
                            imageVector = if (index == selectedItemIndex) item.selectedIcon else item.unselectedIcon,
                            contentDescription = item.title
                        )
                    },
                    badge = {
                        // Display a badge with a count if provided in the [NavigationItem].
                        item.badgeCount?.let {
                            Text(text = item.badgeCount.toString())
                        }
                    },
                    modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.md))
                )
            }
        }
    }, drawerState = drawerState) {
        // Include the [RootLayout] to wrap the content of the screen along with the navigation drawer.
        RootLayout(
            drawerState = drawerState,
            scope = scope,
            navController = navController,
            navigationType = navigationType
        )
    }
}