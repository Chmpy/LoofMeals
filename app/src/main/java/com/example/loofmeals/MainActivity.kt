package com.example.loofmeals

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.compose.LoofTheme
import com.example.loofmeals.ui.layout.RootLayout
import kotlinx.coroutines.launch

// TODO: CLEANUP STRUCTURE
data class NavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeCount: Int? = null
)

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoofTheme {

//                TODO: CLEANUP STRUCTURE
                /*TEMP items for drawer*/
                val items = listOf(
                    NavigationItem(
                        title = "Home",
                        selectedIcon = Icons.Filled.Home,
                        unselectedIcon = Icons.Filled.Home,
                    ), NavigationItem(
                        title = "Settings",
                        selectedIcon = Icons.Filled.Settings,
                        unselectedIcon = Icons.Filled.Settings,
                        badgeCount = 45
                    ), NavigationItem(
                        title = "About",
                        selectedIcon = Icons.Filled.Info,
                        unselectedIcon = Icons.Filled.Info,
                    )
                )

                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                    val scope = rememberCoroutineScope()
                    var selectedItemIndex by rememberSaveable {
                        mutableStateOf(0)
                    }
                    ModalNavigationDrawer(drawerContent = {
                        ModalDrawerSheet {
                            Spacer(modifier = Modifier.height(16.dp))
                            items.forEachIndexed { index, item ->
                                NavigationDrawerItem(
                                    label = { Text(text = item.title) },
                                    selected = index == selectedItemIndex,
                                    onClick = {
                                        /*TODO: Navigate*/
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
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                            }
                        }
                    }, drawerState = drawerState) {
                        RootLayout(
                            drawerState = drawerState,
                            scope = scope,
                        )
                    }
                }
            }
        }
    }
}