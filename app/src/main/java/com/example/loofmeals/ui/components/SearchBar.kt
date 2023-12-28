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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(filter: (String) -> Unit, modifier: Modifier = Modifier) {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    fun updateFilter(query: String) {
        searchQuery = query
        filter(query)
    }
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
