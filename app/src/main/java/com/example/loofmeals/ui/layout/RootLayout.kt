package com.example.loofmeals.ui.layout

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.loofmeals.ui.screens.About
import com.example.loofmeals.ui.screens.Favorites
import com.example.loofmeals.ui.screens.Overview
import com.example.loofmeals.ui.screens.Screens
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RootLayout(
    drawerState: DrawerState,
    scope: CoroutineScope,
    navController: NavHostController,
) {
    val backStackEntry by navController.currentBackStackEntryAsState()

    val currentScreenTitle = Screens.valueOf(
        backStackEntry?.destination?.route ?: Screens.Overview.name,
    ).title

    Scaffold(
        topBar = { TopLoofBar(currentScreenTitle, scope, drawerState) },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screens.Overview.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screens.Overview.name) {
                Overview()
            }
            composable(Screens.Favorites.name) {
                Favorites()
            }
            composable(Screens.About.name) {
                About()
            }
        }
    }
}