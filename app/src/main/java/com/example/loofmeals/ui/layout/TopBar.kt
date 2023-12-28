package com.example.loofmeals.ui.layout

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.loofmeals.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Composable function that creates a top bar with a title and a navigation icon.
 *
 * This function creates a CenterAlignedTopAppBar composable with a title and a navigation icon.
 * The title is centered and the navigation icon is aligned to the start.
 * When the navigation icon is clicked,
 * it opens the drawer.
 *
 * @param screenTitle The resource id of the string to display as the title.
 * @param scope The CoroutineScope in which to launch coroutines.
 * This is used to control the lifecycle of the coroutines.
 * @param drawerState The state of the drawer. This is used to control the opening and closing of the drawer.
 */
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TopLoofBar(
    @StringRes screenTitle: Int,
    scope: CoroutineScope,
    drawerState: DrawerState
) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        title = {
            Text(
                text = stringResource(screenTitle),
                color = MaterialTheme.colorScheme.onPrimary
            )
        },
        navigationIcon = {
            IconButton(onClick = {
                scope.launch { drawerState.open() }
            }) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = stringResource(R.string.menu),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
    )
}