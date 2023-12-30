package com.example.loofmeals.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.twotone.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import com.example.loofmeals.R
import com.example.loofmeals.data.model.Restaurant

/**
 * Composable function that creates a restaurant card.
 *
 * This function creates a Card composable
 * that displays the name of the restaurant and a favorite icon.
 * The card is clickable and the favorite icon is also clickable.
 *
 * @param modifier The modifier to apply to the Card. The default value is Modifier.
 * @param restaurant The restaurant to display in the card.
 * @param onClick The function to call when the card is clicked.
 * @param onIconClick The function to call when the favorite icon is clicked.
 */
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
        modifier = modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.sm))
            .alpha(0.8f).testTag("RestaurantCard"),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.md)),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(modifier = Modifier.weight(1f)) {
                restaurant.name?.let {
                    Text(
                        text = it,
                        modifier = Modifier.padding(dimensionResource(R.dimen.md))
                    )
                }
            }
            IconButton(
                onClick = onIconClick,
                interactionSource =
                remember { MutableInteractionSource() },
            ) {
                if (restaurant.isFavorite) {
                    Icon(Icons.Filled.Favorite, contentDescription = "favorite")
                } else {
                    Icon(Icons.TwoTone.Favorite, contentDescription = "no_favorite")
                }
            }
        }

    }
}