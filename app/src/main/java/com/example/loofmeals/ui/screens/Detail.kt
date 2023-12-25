package com.example.loofmeals.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.loofmeals.R
import com.example.loofmeals.data.model.Restaurant
import com.example.loofmeals.ui.restaurant.RestaurantDetailApiState.Error
import com.example.loofmeals.ui.restaurant.RestaurantDetailApiState.Loading
import com.example.loofmeals.ui.restaurant.RestaurantDetailApiState.Success
import com.example.loofmeals.ui.restaurant.RestaurantDetailState
import com.example.loofmeals.ui.restaurant.RestaurantDetailViewModel

@Composable
fun Detail(
    restaurantId: Int,
    navController: NavHostController,
    restaurantDetailViewModel: RestaurantDetailViewModel = viewModel(
        factory = RestaurantDetailViewModel.Factory(restaurantId)
    )
) {

    val restaurantDetailState by restaurantDetailViewModel.uiState.collectAsState()
    val restaurantDetailApiState = restaurantDetailViewModel.restaurantDetailApiState

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        when (restaurantDetailApiState) {
            is Loading -> {
                Text(text = "Loading")
            }

            is Success -> {
                RestaurantDetail(
                    modifier = Modifier.align(Alignment.TopCenter),
                    restaurantDetailState = restaurantDetailState
                )
            }

            is Error -> {
                Text(text = "Error")
            }
        }
    }
}

@Composable
fun RestaurantDetail(
    modifier: Modifier = Modifier,
    restaurantDetailState: RestaurantDetailState,
) {
    val lazyListState = rememberLazyListState()
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = dimensionResource(id = R.dimen.xl)),
        state = lazyListState,
    ) {
        item {
            val restaurant = restaurantDetailState.restaurant
            RestaurantDetailHeader(restaurant = restaurant)
            RestaurantDetailBody(restaurant = restaurant)
        }
    }
}

@Composable
fun RestaurantDetailHeader(
    modifier: Modifier = Modifier,
    restaurant: Restaurant,
) {
    Column(
        modifier = modifier
            .padding(
                top = dimensionResource(id = R.dimen.lg),
                bottom = dimensionResource(id = R.dimen.lg)
            )
            .fillMaxWidth(),
        horizontalAlignment = CenterHorizontally,
    ) {
        Text(
//            text = restaurant.name ?: "No Name",
            if (restaurant.name.isNullOrEmpty()) stringResource(R.string.no_name) else restaurant.name,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
        Text(
            if (restaurant.cityName.isNullOrEmpty()) stringResource(R.string.no_city) else restaurant.cityName,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )

    }
}

@Composable
fun RestaurantDetailBody(
    modifier: Modifier = Modifier,
    restaurant: Restaurant,
) {
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        DescriptionBody(restaurant = restaurant)
        Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.sm)))
        AccessibilityBody(restaurant = restaurant)
        Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.sm)))
    }
}

@Composable
fun DescriptionBody(
    restaurant: Restaurant,
) {
    Column {
        ///* Description *///
        Text(
            text = if (restaurant.productDescription.isNullOrEmpty()) stringResource(R.string.no_description) else restaurant.productDescription,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun AccessibilityBody(
    restaurant: Restaurant,
) {
    Column {
        AccessibilityDescriptionBody(restaurant)
        Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.sm)))

        RouteBody(restaurant)
        Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.sm)))

        FacilityBody(restaurant)
        Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.sm)))

        AccessibilityLinkBody(restaurant)
        Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.sm)))
    }
}

@Composable
private fun AccessibilityLinkBody(restaurant: Restaurant) {
    val context = LocalContext.current
    ///* Link to accessibility website *///
    Text(
        text = stringResource(R.string.link_to_accessibility_website),
        style = MaterialTheme.typography.titleSmall
    )
    val linkToAccessibilityWebsite = restaurant.linkToAccessibilityWebsite
    val urlPattern = Regex(pattern = "^(http|https)://.*$")
    val validUrl = urlPattern.matches(linkToAccessibilityWebsite ?: "")
    ClickableText(text = buildAnnotatedString {
        withStyle(
            if (validUrl) SpanStyle(
                textDecoration = TextDecoration.Underline,
                color = MaterialTheme.colorScheme.primary
            ) else SpanStyle(
                textDecoration = TextDecoration.Underline,
                color = MaterialTheme.colorScheme.error
            )
        ) {
            append(
                if (linkToAccessibilityWebsite.isNullOrEmpty()) stringResource(R.string.no_link) else linkToAccessibilityWebsite
            )
        }
    }, onClick = {
        if (validUrl) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(linkToAccessibilityWebsite))
            context.startActivity(intent)
        }
    }, style = MaterialTheme.typography.bodySmall
    )
}

@Composable
private fun AccessibilityDescriptionBody(restaurant: Restaurant) {
    ///* Accessibility *///
    Text(
        text = stringResource(R.string.Accesibility),
        style = MaterialTheme.typography.titleMedium
    )
    val accessibilityDescription = restaurant.accessibilityDescription
    Text(
        text = if (accessibilityDescription.isNullOrEmpty()) stringResource(R.string.no_accessibility) else accessibilityDescription,
        style = MaterialTheme.typography.bodyMedium
    )
}

@Composable
private fun FacilityBody(restaurant: Restaurant) {
    ///* Facilities *///
    Text(
        text = stringResource(R.string.facilities),
        style = MaterialTheme.typography.titleMedium
    )
    val facilities = restaurant.extraFacilities
    Text(
        text = if (facilities.isNullOrEmpty()) stringResource(R.string.no_facilities) else facilities,
        style = MaterialTheme.typography.bodyMedium
    )
}

@Composable
private fun RouteBody(restaurant: Restaurant) {
    ///* Route *///
    Text(
        text = stringResource(R.string.route),
        style = MaterialTheme.typography.titleMedium
    )
    val route = restaurant.routeAndLevels
    Text(
        text = if (route.isNullOrEmpty()) stringResource(R.string.no_route) else route,
        style = MaterialTheme.typography.bodyMedium
    )
}