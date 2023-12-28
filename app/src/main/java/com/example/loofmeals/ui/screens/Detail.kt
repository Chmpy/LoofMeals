package com.example.loofmeals.ui.screens

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Web
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.loofmeals.R
import com.example.loofmeals.data.model.Restaurant
import com.example.loofmeals.ui.restaurant.RestaurantDetailApiState.Error
import com.example.loofmeals.ui.restaurant.RestaurantDetailApiState.Loading
import com.example.loofmeals.ui.restaurant.RestaurantDetailApiState.Success
import com.example.loofmeals.ui.restaurant.RestaurantDetailState
import com.example.loofmeals.ui.restaurant.RestaurantDetailViewModel


/**
 * Composable function that displays the Detail screen.
 *
 * @param restaurantId The id of the restaurant to display.
 * @param restaurantDetailViewModel The ViewModel for the Detail screen.
 */
@Composable
fun Detail(
    restaurantId: Int, restaurantDetailViewModel: RestaurantDetailViewModel = viewModel(
        factory = RestaurantDetailViewModel.Factory(restaurantId)
    )
) {

    val restaurantDetailState by restaurantDetailViewModel.uiState.collectAsState()
    val restaurantDetailApiState = restaurantDetailViewModel.restaurantDetailApiState

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.background2),
            contentDescription = "background2",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.matchParentSize()
        )
        when (restaurantDetailApiState) {
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
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(dimensionResource(R.dimen.md)),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = CenterHorizontally
                ) {
                    Text(stringResource(id = R.string.restaurant_get_error))
                }
            }

            is Success -> {
                RestaurantDetail(
                    modifier = Modifier.align(Alignment.TopCenter),
                    restaurantDetailState = restaurantDetailState
                )
            }
        }
    }
}

/**
 * Composable function that displays the restaurant detail.
 *
 * @param modifier The modifier to apply to the detail. The default value is Modifier.
 * @param restaurantDetailState The state of the restaurant detail.
 */
@Composable
fun RestaurantDetail(
    modifier: Modifier = Modifier,
    restaurantDetailState: RestaurantDetailState,
) {
    val lazyListState = rememberLazyListState()
    Surface(
        modifier = modifier.alpha(0.8f),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = dimensionResource(id = R.dimen.sm),
    ) {
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
                RestaurantDetailFooter(restaurant = restaurant)
            }
        }
    }
}

/**
 * Composable function that displays the restaurant detail header.
 *
 * @param modifier The modifier to apply to the header. The default value is Modifier.
 * @param restaurant The restaurant to display in the header.
 */
@Composable
fun RestaurantDetailHeader(modifier: Modifier = Modifier, restaurant: Restaurant) {
    Column(
        modifier = modifier
            .padding(
                top = dimensionResource(id = R.dimen.lg),
                bottom = dimensionResource(id = R.dimen.sm)
            )
            .fillMaxWidth(),
        horizontalAlignment = CenterHorizontally,
    ) {
        Text(
            if (restaurant.name.isNullOrEmpty()) stringResource(R.string.no_name) else restaurant.name,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * Composable function that displays the restaurant detail body.
 *
 * @param modifier The modifier to apply to the body. The default value is Modifier.
 * @param restaurant The restaurant to display in the body.
 */
@Composable
fun RestaurantDetailBody(modifier: Modifier = Modifier, restaurant: Restaurant) {
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        DescriptionBody(restaurant = restaurant)
        Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.sm)))
        AccessibilityBody(restaurant = restaurant)
    }
}

@Composable
fun DescriptionBody(restaurant: Restaurant) {
    Column {
        ///* Description *///
        Text(
            text = if (restaurant.productDescription.isNullOrEmpty()) stringResource(R.string.no_description) else restaurant.productDescription,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun AccessibilityBody(restaurant: Restaurant) {
    Column {
        AccessibilityDescriptionBody(restaurant)
        Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.sm)))

        RouteBody(restaurant)
        Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.sm)))

        FacilityBody(restaurant)
        Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.sm)))

        AccessibilityLinkBody(restaurant)
        Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.lg)))
    }
}

@Composable
private fun AccessibilityDescriptionBody(restaurant: Restaurant) {
    ///* Accessibility *///
    Column(
        modifier = Modifier
            .padding(
                top = dimensionResource(id = R.dimen.lg),
                bottom = dimensionResource(id = R.dimen.sm)
            )
            .fillMaxWidth(),
        horizontalAlignment = CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.accesibility),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
    }
    Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.sm)))
    val accessibilityDescription = restaurant.accessibilityDescription
    Text(
        text = if (accessibilityDescription.isNullOrEmpty()) stringResource(R.string.no_accessibility) else accessibilityDescription,
        style = MaterialTheme.typography.bodyMedium
    )
}

@Composable
private fun RouteBody(restaurant: Restaurant) {
    ///* Route *///
    Text(
        text = stringResource(R.string.route), style = MaterialTheme.typography.titleMedium
    )
    val route = restaurant.routeAndLevels
    Text(
        text = if (route.isNullOrEmpty()) stringResource(R.string.no_route) else route,
        style = MaterialTheme.typography.bodyMedium
    )
}

@Composable
private fun FacilityBody(restaurant: Restaurant) {
    ///* Facilities *///
    Text(
        text = stringResource(R.string.facilities), style = MaterialTheme.typography.titleMedium
    )
    val facilities = restaurant.extraFacilities
    Text(
        text = if (facilities.isNullOrEmpty()) stringResource(R.string.no_facilities) else facilities,
        style = MaterialTheme.typography.bodyMedium
    )
}

@Composable
private fun AccessibilityLinkBody(restaurant: Restaurant) {
    ///* Link to accessibility website *///
    Text(
        text = stringResource(R.string.link_to_accessibility_website),
        style = MaterialTheme.typography.titleSmall
    )
    val context = LocalContext.current
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

/**
 * Composable function that displays the restaurant detail footer.
 *
 * @param modifier The modifier to apply to the footer. The default value is Modifier.
 * @param restaurant The restaurant to display in the footer.
 */
@Composable
fun RestaurantDetailFooter(modifier: Modifier = Modifier, restaurant: Restaurant) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {

        ContactBody()
        Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.xs)))

        AddressBody(restaurant)
        Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.sm)))

        PhoneBody(restaurant)
        Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.sm)))

        EmailBody(restaurant)
        Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.sm)))

        WebsiteBody(restaurant)
        Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.lg)))
    }
}

@Composable
private fun ContactBody() {
    ///* Contact *///
    Text(
        text = stringResource(R.string.contact), style = MaterialTheme.typography.titleMedium
    )
}

@Composable
private fun AddressBody(restaurant: Restaurant) {
    ///* Address *///
    val address =
        restaurant.street + " " + restaurant.houseNumber + ", " + restaurant.postalCode + ", " + restaurant.cityName
    Row(
        modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Home, contentDescription = null, modifier = Modifier.size(
                dimensionResource(R.dimen.xl)
            )
        )
        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.sm)))
        val context = LocalContext.current
        ClickableText(text = buildAnnotatedString {
            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                append(address)
            }
        }, onClick = {
            val long = restaurant.long
            val lat = restaurant.lat
            val gmmIntentUri = Uri.parse("google.navigation:q=$lat,$long")
            val intent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            intent.setPackage("com.google.android.apps.maps")
            Log.d(
                "Detail",
                "Opening google maps on: http://maps.google.com/maps?daddr=$lat,$long"
            )
            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
            }
        }, style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun PhoneBody(restaurant: Restaurant) {
    ///* Phone *///
    Row(
        modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(
                dimensionResource(R.dimen.xl)
            )
        )
        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.sm)))
//        Text(
//            text = if (restaurant.phone1.isNullOrEmpty()) stringResource(R.string.no_phone) else restaurant.phone1,
//            style = MaterialTheme.typography.bodyMedium
//        )
        val context = LocalContext.current
        ClickableText(text = buildAnnotatedString {
            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                append(restaurant.phone1)
            }
        }, onClick = {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${restaurant.phone1}")
            Log.d("Detail", "Opening phone dialer on: " + "tel:${restaurant.phone1}")
            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
            }
        }, style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun EmailBody(restaurant: Restaurant) {
    ///* Email *///
    Row(
        modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Email, contentDescription = null, modifier = Modifier.size(
                dimensionResource(R.dimen.xl)
            )
        )
        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.sm)))
        val context = LocalContext.current
        ClickableText(text = buildAnnotatedString {
            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                append(restaurant.email)
            }
        }, onClick = {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:${restaurant.email}")
            intent.putExtra(Intent.EXTRA_SUBJECT, R.string.subject)
            Log.d("Detail", "Opening email on: " + "mailto:${restaurant.email}")
            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
            }
        }, style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun WebsiteBody(restaurant: Restaurant) {
    ///* Website *///
    Row(
        modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Web, contentDescription = null, modifier = Modifier.size(
                dimensionResource(R.dimen.xl)
            )
        )
        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.sm)))
        val context = LocalContext.current
        val website = restaurant.website
        val urlPattern = Regex(pattern = "^(http|https)://.*$")
        val validUrl = urlPattern.matches(website ?: "")
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
                    if (website.isNullOrEmpty()) stringResource(R.string.no_website) else website
                )
            }
        }, onClick = {
            if (validUrl) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(website))
                Log.d("Detail", "Opening website on: $website")
                context.startActivity(intent)
            }
        }, style = MaterialTheme.typography.bodyMedium
        )
    }
}