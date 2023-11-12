package com.example.loofmeals.ui.restaurant

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.loofmeals.data.model.Restaurant

@Composable
fun RestaurantCard(Restaurant: Restaurant, onClick: () -> Unit) {
    Text(text = Restaurant.name)
}