package com.example.loofmeals

import android.os.Build
import android.os.Bundle
import android.window.OnBackInvokedDispatcher
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.loofmeals.ui.LoofMealsApp
import com.example.loofmeals.ui.theme.LoofTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= 33) {
            onBackInvokedDispatcher.registerOnBackInvokedCallback(
                OnBackInvokedDispatcher.PRIORITY_DEFAULT,
            ) {
                if (onBackPressedDispatcher.hasEnabledCallbacks()) {
                    onBackPressedDispatcher.onBackPressed()
                    true
                } else {
                    false
                }

            }
            installSplashScreen()
            super.onCreate(savedInstanceState)
            setContent {
                LoofTheme {
                    LoofMealsApp()
                }
            }
        }
    }
}