package com.example.loofmeals

import android.os.Build
import android.os.Bundle
import android.window.OnBackInvokedDispatcher
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.loofmeals.ui.LoofMealsApp
import com.example.loofmeals.ui.theme.LoofTheme

/**
 * Main activity for the LoofMeals app.
 *
 * This activity is the entry point of the app.
 * It sets up the splash screen and the back button behavior,
 * and it sets the content to the LoofMealsApp composable.
 */
class MainActivity : ComponentActivity() {
    /**
     * Called when the activity is starting.
     *
     * This function sets up the splash screen and the back button behavior,
     * and it sets the content to the LoofMealsApp composable.
     *
     * @param savedInstanceState If the activity is being re-initialized
     * after previously being shut down, then this Bundle contains the data
     * it most recently supplied in onSaveInstanceState(Bundle).
     * Note: Otherwise it is null.
     */
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        // Check if the Android version is 33 or higher
        if (Build.VERSION.SDK_INT >= 33) {
            // Register a callback for the back button
            onBackInvokedDispatcher.registerOnBackInvokedCallback(
                OnBackInvokedDispatcher.PRIORITY_DEFAULT,
            ) {
                // If there are enabled callbacks for the back button,
                // invoke the back button and return true
                if (onBackPressedDispatcher.hasEnabledCallbacks()) {
                    onBackPressedDispatcher.onBackPressed()
                }
            }
            // Install the splash screen
            installSplashScreen()
            // Call the superclass implementation of onCreate
            super.onCreate(savedInstanceState)
            // Set the content to the LoofMealsApp composable
            setContent {
                LoofTheme {
                    val windowSize = calculateWindowSizeClass(this)
                    LoofMealsApp(windowSize.widthSizeClass)
                }
            }
        }
    }
}