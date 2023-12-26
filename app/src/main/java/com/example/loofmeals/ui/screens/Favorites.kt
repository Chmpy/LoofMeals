package com.example.loofmeals.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.loofmeals.R
import com.example.loofmeals.ui.favorite.FavoriteApiState
import com.example.loofmeals.ui.favorite.FavoriteApiState.*
import com.example.loofmeals.ui.favorite.FavoriteState
import com.example.loofmeals.ui.favorite.FavoriteViewModel
import com.example.loofmeals.ui.restaurant.RestaurantCard
import com.example.loofmeals.ui.restaurant.RestaurantDetailApiState
import com.example.loofmeals.ui.restaurant.RestaurantOverviewState
import com.example.loofmeals.ui.restaurant.RestaurantViewModel

@Composable
fun Favorites(
    navController: NavController,
    favoriteViewModel: FavoriteViewModel = viewModel(factory = FavoriteViewModel.Factory)
) {
    val favoriteState by favoriteViewModel.uiState.collectAsState()
    val favoriteApiState = favoriteViewModel.favoriteApiState

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        when (favoriteApiState) {
            is Loading -> {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(dimensionResource(R.dimen.xl))
                            .height(dimensionResource(R.dimen.xs)),
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }

            is Error -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(dimensionResource(R.dimen.md)),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        Text(stringResource(id = R.string.favorites_get_error))
                    }
                }
            }

            is Success -> {
                FavoritesList(
                    modifier = Modifier.align(Alignment.TopCenter),
                    favoriteState = favoriteState,
                    navController = navController,
                    favoriteViewModel = favoriteViewModel
                )
            }
        }
    }
}

@Composable
fun FavoritesList(
    modifier: Modifier = Modifier,
    favoriteState: FavoriteState,
    navController: NavController,
    favoriteViewModel: FavoriteViewModel
) {

    fun goToDetail(restaurantId: Int) {
        navController.navigate("${Screens.Detail.name}/$restaurantId")
    }

    val lazyListState = rememberLazyListState()

    LazyColumn(
        state = lazyListState, modifier = modifier.padding(
            horizontal = dimensionResource(R.dimen.md),
        )
    ) {
        items(favoriteState.restaurants) { restaurant ->
            RestaurantCard(
                restaurant = restaurant,
                onClick = { goToDetail(restaurant.id) },
                onIconClick = { favoriteViewModel.updateFavorite(restaurant) }
            )
        }
    }
}