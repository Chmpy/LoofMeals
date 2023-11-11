package com.example.loofmeals.ui.layout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.loofmeals.R
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RootLayout(
    drawerState: DrawerState,
    scope: CoroutineScope,
    //TODO : Pass content from navigation later
) {
    Scaffold(
        topBar = { TopLoofBar(R.string.app_name, scope, drawerState) },
    ) {
        Column(modifier = Modifier.padding(it)) {
            Text(text = "Content")
            //TODO : Add content
        }
    }
}