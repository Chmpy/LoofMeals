package com.example.loofmeals.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import com.example.loofmeals.R

//Generic component to apply opaque background/card styling to a composable
@Composable
fun BackgroundSurface(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier.alpha(0.8f),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = dimensionResource(id = R.dimen.sm),
        shape = MaterialTheme.shapes.medium,
    ) {
        content()
    }
}