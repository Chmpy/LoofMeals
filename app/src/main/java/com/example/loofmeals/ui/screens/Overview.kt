package com.example.loofmeals.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.loofmeals.R
import com.example.loofmeals.ui.components.BackgroundSurface
import com.example.loofmeals.ui.components.SearchBar
import com.example.loofmeals.ui.restaurant.RestaurantApiState.Error
import com.example.loofmeals.ui.restaurant.RestaurantApiState.Loading
import com.example.loofmeals.ui.restaurant.RestaurantApiState.NetworkError
import com.example.loofmeals.ui.restaurant.RestaurantApiState.Success
import com.example.loofmeals.ui.restaurant.RestaurantCard
import com.example.loofmeals.ui.restaurant.RestaurantOverviewState
import com.example.loofmeals.ui.restaurant.RestaurantViewModel

@Composable
@OptIn(ExperimentalMaterialApi::class)
fun Overview(
    navController: NavController,
    restaurantViewModel: RestaurantViewModel = viewModel(factory = RestaurantViewModel.Factory)
) {
    val restaurantOverviewState by restaurantViewModel.uiState.collectAsState()
    val restaurantApiState = restaurantViewModel.restaurantApiState
    val pullRefreshState = rememberPullRefreshState(
        refreshing = restaurantApiState is Loading,
        onRefresh = { restaurantViewModel.refreshRestaurants() },
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {
        Image(
            painter = painterResource(id = R.drawable.background1),
            contentDescription = "background1",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.matchParentSize()
        )
        Column {
            SearchBar(restaurantViewModel::filterRestaurants, modifier = Modifier.fillMaxWidth())
            if (restaurantApiState is NetworkError) {
                Toast.makeText(
                    LocalContext.current,
                    stringResource(id = R.string.network_error),
                    Toast.LENGTH_SHORT
                ).show()
            }
            when (restaurantApiState) {
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
                            BackgroundSurface {
                                Text(
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.padding(dimensionResource(id = R.dimen.md)),
                                    text = stringResource(id = R.string.restaurants_get_error)
                                )
                            }
                        }

                    }
                }

                is Success -> {
                    RestaurantList(
                        restaurantOverviewState = restaurantOverviewState,
                        navController = navController,
                        restaurantViewModel = restaurantViewModel
                    )
                }

                is NetworkError -> {
                    if (restaurantOverviewState.restaurants.isEmpty()) {

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(dimensionResource(R.dimen.md)),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            item {
                                BackgroundSurface {
                                    Text(
                                        style = MaterialTheme.typography.bodyLarge,
                                        modifier = Modifier.padding(dimensionResource(id = R.dimen.md)),
                                        text = stringResource(id = R.string.restaurants_get_error)
                                    )
                                }
                            }
                        }
                    } else {
                        RestaurantList(
                            restaurantOverviewState = restaurantOverviewState,
                            navController = navController,
                            restaurantViewModel = restaurantViewModel
                        )
                    }
                }
            }
        }
        if (restaurantApiState !is Loading) {
            PullRefreshIndicator(
                refreshing = restaurantApiState is Loading,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

@Composable
fun RestaurantList(
    modifier: Modifier = Modifier,
    restaurantOverviewState: RestaurantOverviewState,
    navController: NavController,
    restaurantViewModel: RestaurantViewModel
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
        items(restaurantOverviewState.restaurants) { restaurant ->
            RestaurantCard(restaurant = restaurant,
                onClick = { goToDetail(restaurant.id) },
                onIconClick = { restaurantViewModel.updateFavorite(restaurant) })
        }
    }
}