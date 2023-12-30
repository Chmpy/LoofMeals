package com.example.loofmeals.ui.components

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.example.loofmeals.R

/**
 * Composable function that creates a search bar.
 *
 * This function creates a TextField composable that acts as a search bar.
 * The search bar has a leading icon
 * that submits the search query and a trailing icon that clears the search query.
 * The search query is updated
 * every time the text in the TextField changes.
 *
 * @param filter The function to call when the search query changes.
 * This function takes the search query as a parameter.
 * @param modifier The modifier to apply to the TextField. The default value is Modifier.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(filter: (String) -> Unit, modifier: Modifier = Modifier) {
    // The search query. This is a mutable state remembered across recompositions.
    var searchQuery by rememberSaveable { mutableStateOf("") }

    // The focus manager. This is used to clear the focus from the TextField.
    val focusManager = LocalFocusManager.current

    // Function to update the search query and call the filter function.
    fun updateFilter(query: String) {
        searchQuery = query
        filter(query)
    }

    // The TextField composable that acts as the search bar.
    TextField(
        value = searchQuery,
        onValueChange = { updateFilter(it) },
        placeholder = { Text(text = stringResource(id = R.string.search_bar_label)) },
        modifier = modifier
            .focusable(true)
            .fillMaxWidth().alpha(0.8f),
        singleLine = true,
        leadingIcon = {
            IconButton(onClick = { updateFilter(searchQuery); focusManager.clearFocus() }) {
                Icon(
                    Icons.Default.Search,
                    contentDescription = stringResource(id = R.string.search_bar_description),

                    )
            }
        },
        trailingIcon = {
            IconButton(onClick = { updateFilter("") }) {
                Icon(
                    Icons.Default.Clear,
                    contentDescription = stringResource(id = R.string.search_bar_clear_description),
                )
            }
        },
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = {
            updateFilter(searchQuery); focusManager.clearFocus()
        }),
    )
}