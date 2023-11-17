package com.example.loofmeals.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.loofmeals.R
import com.example.loofmeals.ui.restaurant.RestaurantApiState
import com.example.loofmeals.ui.restaurant.RestaurantCard
import com.example.loofmeals.ui.restaurant.RestaurantOverviewState
import com.example.loofmeals.ui.restaurant.RestaurantViewModel

@Composable
fun Overview(
    modifier: Modifier = Modifier,
    restaurantViewModel: RestaurantViewModel = viewModel(factory = RestaurantViewModel.Factory)
) {

    val restaurantOverviewState by restaurantViewModel.uiState.collectAsState()
    val restaurantApiState = restaurantViewModel.restaurantApiState

    Box(modifier = modifier) {
        when (restaurantApiState) {
            // TODO: Replace with loading indicator & pretty error message
            is RestaurantApiState.Loading -> Text("Loading...")
            is RestaurantApiState.Error -> Text(stringResource(R.string.restaurants_get_error))
            is RestaurantApiState.Success -> RestaurantList(restaurantOverviewState = restaurantOverviewState)
        }
    }
}

@Composable
fun RestaurantList(
    modifier: Modifier = Modifier,
    restaurantOverviewState: RestaurantOverviewState
) {

    val lazyListState = rememberLazyListState()
    LazyColumn(state = lazyListState, modifier = modifier.padding(
        dimensionResource(R.dimen.md)
    )) {
        items(restaurantOverviewState.restaurants) {
            RestaurantCard(Restaurant = it) {}
        }
    }
}