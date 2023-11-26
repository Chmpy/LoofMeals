package com.example.loofmeals.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.loofmeals.R

val NovaSquare = FontFamily(
    Font(R.font.nova_square_regular)
)
val NovaRound = FontFamily(
    Font(R.font.nova_round_regular)
)

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = NovaRound,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    titleLarge = TextStyle(
        fontFamily = NovaSquare,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    )
)