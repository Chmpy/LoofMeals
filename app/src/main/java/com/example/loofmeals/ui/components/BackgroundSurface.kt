package com.example.loofmeals.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.dimensionResource
import com.example.loofmeals.R

/**
 * Composable function that applies a semi-transparent background and styling to its content.
 *
 * This function creates a Surface composable with a semi-transparent background and styling.
 *
 * @param modifier The modifier to apply to the surface. The default value is Modifier.
 * @param content The content of the surface.
 * This is a composable function passed as a parameter to the BackgroundSurface function.
 */
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