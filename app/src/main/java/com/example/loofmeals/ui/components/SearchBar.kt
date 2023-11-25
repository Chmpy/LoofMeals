package com.example.loofmeals.ui.components

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.example.loofmeals.R
import kotlin.reflect.KFunction1

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(Filter: (String) -> Unit, modifier: Modifier = Modifier.fillMaxWidth()) {
    var searchQuery by remember {
        mutableStateOf("")
    }

    fun updateFilter(query: String) {
        searchQuery = query
        Filter(query)
    }

    Row(
        modifier = modifier
            .padding(dimensionResource(R.dimen.lg)),
        horizontalArrangement = Arrangement.Center,
    ) {
        TextField(
            value = searchQuery,
            onValueChange = { updateFilter(it) },
            placeholder = { Text(text = stringResource(id = R.string.search_bar_label)) },
            modifier = modifier.focusable(true),
            maxLines = 1,
            leadingIcon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = stringResource(id = R.string.search_bar_description)
                )
            },
        )
    }
}
