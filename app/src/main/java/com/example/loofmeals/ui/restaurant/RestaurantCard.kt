package com.example.loofmeals.ui.restaurant

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.example.loofmeals.R
import com.example.loofmeals.data.model.Restaurant


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantCard(modifier: Modifier = Modifier, restaurant: Restaurant, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        colors = cardColors(),
        modifier = modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.sm)),
    ) {
        restaurant.name?.let {
            Text(
                text = it,
                modifier = Modifier.padding(dimensionResource(R.dimen.lg))
            )
        }
        restaurant.mainCityName?.let {
            Text(
                text = it,
                modifier = Modifier.padding(dimensionResource(R.dimen.lg))
            )
        }
    }
}

@Composable
fun cardColors(): CardColors {
    return CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
    )
}