package com.example.loofmeals.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.loofmeals.ui.restaurant.RestaurantDetailApiState
import com.example.loofmeals.ui.restaurant.RestaurantDetailApiState.*
import com.example.loofmeals.ui.restaurant.RestaurantDetailState
import com.example.loofmeals.ui.restaurant.RestaurantDetailViewModel

@Composable
fun Detail(
    restaurantId: Int, navController: NavHostController,
    restaurantDetailViewModel: RestaurantDetailViewModel = viewModel(
        factory = RestaurantDetailViewModel.Factory(restaurantId)
    )
) {

    val restaurantDetailState by restaurantDetailViewModel.uiState.collectAsState()
    val restaurantDetailApiState = restaurantDetailViewModel.restaurantDetailApiState

    when (restaurantDetailApiState) {
        is Loading -> {
            Text(text = "Loading")
        }

        is Success -> {
            RestaurantDetail(
                restaurantDetailState = restaurantDetailState
            )
        }

        is Error -> {
            Text(text = "Error")
        }
    }
}

@Composable
fun RestaurantDetail(
    modifier: Modifier = Modifier,
    restaurantDetailState: RestaurantDetailState,
) {
    Text(text = restaurantDetailState.restaurant.name ?: "No Name")
}