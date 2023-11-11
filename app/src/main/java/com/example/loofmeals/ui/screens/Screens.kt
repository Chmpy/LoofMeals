package com.example.loofmeals.ui.screens

import androidx.annotation.StringRes
import com.example.loofmeals.R

enum class Screens(@StringRes val title: Int) {
    Overview(title = R.string.app_name),
    Favorites(title = R.string.favorites),
    About(title = R.string.about)
}