package com.example.loofmeals.ui.restaurant

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.twotone.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.example.loofmeals.R
import com.example.loofmeals.data.model.Restaurant


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantCard(
    modifier: Modifier = Modifier,
    restaurant: Restaurant,
    onClick: () -> Unit,
    onIconClick: () -> Unit
) {
    Card(
        onClick = onClick,
        colors = cardColors(),
        modifier = modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.sm)),
    ) {
        Row {
            Column(modifier = Modifier.weight(1f)) {
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
            IconButton(
                onClick = onIconClick,
                interactionSource =
                remember { MutableInteractionSource() },
            ) {
                if (restaurant.isFavorite == true) {
                    Icon(Icons.Filled.Favorite, contentDescription = "favorite")
                } else {
                    Icon(Icons.TwoTone.Favorite, contentDescription = "no_favorite")
                }
            }
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