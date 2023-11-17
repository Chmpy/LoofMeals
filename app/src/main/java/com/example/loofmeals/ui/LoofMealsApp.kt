package com.example.loofmeals.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.twotone.Favorite
import androidx.compose.material.icons.twotone.Info
import androidx.compose.material.icons.twotone.Restaurant
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.loofmeals.R
import com.example.loofmeals.data.general.NavigationItem
import com.example.loofmeals.ui.layout.RootLayout
import com.example.loofmeals.ui.screens.Screens
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoofMealsApp(navController: NavHostController = rememberNavController()) {
    val items: List<NavigationItem> = navigationItems()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedItemIndex by rememberSaveable {
        mutableIntStateOf(0)
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

    ModalNavigationDrawer(drawerContent = {
        ModalDrawerSheet(content = {
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.lg)))
            items.forEachIndexed { index, item ->
                NavigationDrawerItem(
                    label = { Text(text = item.title) },
                    selected = index == selectedItemIndex,
                    onClick = {
                        when (index) {
                            0 -> goToOverview()
                            1 -> goToFavorite()
                            2 -> goToAbout()
                        }
                        selectedItemIndex = index
                        scope.launch { drawerState.close() }
                    },
                    icon = {
                        Icon(
                            imageVector = if (index == selectedItemIndex) item.selectedIcon else item.unselectedIcon,
                            contentDescription = item.title
                        )
                    },
                    badge = {
                        item.badgeCount?.let {
                            Text(text = item.badgeCount.toString())
                        }
                    },
                    modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.md))
                )
            }
        })
    }, drawerState = drawerState) {
        RootLayout(
            drawerState = drawerState,
            scope = scope,
            navController = navController,
        )
    }
}

@Composable
private fun navigationItems(): List<NavigationItem> {
    return listOf(
        NavigationItem(
            title = stringResource(R.string.overview),
            selectedIcon = Icons.Filled.RestaurantMenu,
            unselectedIcon = Icons.TwoTone.Restaurant,
        ), NavigationItem(
            title = stringResource(R.string.favorites),
            selectedIcon = Icons.Filled.Favorite,
            unselectedIcon = Icons.TwoTone.Favorite,
        ), NavigationItem(
            title = stringResource(R.string.about),
            selectedIcon = Icons.Filled.Info,
            unselectedIcon = Icons.TwoTone.Info,
        )
    )
}