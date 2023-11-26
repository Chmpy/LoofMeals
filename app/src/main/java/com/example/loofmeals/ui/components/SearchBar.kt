package com.example.loofmeals.ui.components

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.example.loofmeals.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(Filter: (String) -> Unit, modifier: Modifier = Modifier.fillMaxWidth()) {
    var searchQuery by remember {
        mutableStateOf("")
    }
    val focusManager = LocalFocusManager.current

    fun updateFilter(query: String) {
        searchQuery = query
        Filter(query)
    }

    Surface(
        modifier = modifier,
        elevation = dimensionResource(R.dimen.md),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = modifier
                .padding(dimensionResource(R.dimen.lg)),
            horizontalArrangement = Arrangement.Center,
        ) {
            TextField(
                value = searchQuery,
                onValueChange = { updateFilter(it) },
                placeholder = { Text(text = stringResource(id = R.string.search_bar_label)) },
                modifier = modifier
                    .focusable(true),
                singleLine = true,
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = stringResource(id = R.string.search_bar_description)
                    )
                },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    updateFilter(searchQuery); focusManager.clearFocus()
                }),
            )
        }
    }

}
