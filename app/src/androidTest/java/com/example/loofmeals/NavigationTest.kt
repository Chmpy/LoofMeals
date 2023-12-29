package com.example.loofmeals

import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.espresso.Espresso
import androidx.test.rule.GrantPermissionRule
import com.example.loofmeals.ui.LoofMealsApp
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class NavigationTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @get:Rule
    val grantPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
        android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
    )

    private lateinit var navController: TestNavHostController

    @Before
    fun setUp() {
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            LoofMealsApp()
        }
    }

    @Test
    fun verifyStartDestination() {
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.app_name), ignoreCase = true
        ).assertExists().assertIsDisplayed()

        composeTestRule.onNodeWithContentDescription(
            composeTestRule.activity.getString(R.string.menu)
        ).assertExists().assertIsDisplayed()

        composeTestRule.onNodeWithContentDescription("background1", ignoreCase = true)
            .assertExists()
    }

    @Test
    fun startToAbout() {
        composeTestRule.onNodeWithContentDescription(
            composeTestRule.activity.getString(R.string.menu)
        ).performClick()

        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.about), ignoreCase = true
        ).assertExists().assertIsDisplayed()

        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.about), ignoreCase = true
        ).performClick()

        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.app_name), ignoreCase = true
        ).assertDoesNotExist()

        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.welcome), ignoreCase = true
        ).assertExists().assertIsDisplayed()

        composeTestRule.onNodeWithContentDescription("background3", ignoreCase = true)
            .assertExists()
    }

    @Test
    fun startToMap() {
        composeTestRule.onNodeWithContentDescription(
            composeTestRule.activity.getString(R.string.menu)
        ).performClick()

        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.map), ignoreCase = true
        ).assertExists().assertIsDisplayed()

        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.map), ignoreCase = true
        ).performClick()

        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.app_name), ignoreCase = true
        ).assertDoesNotExist()

        composeTestRule.onNodeWithContentDescription("background5", ignoreCase = true)
            .assertExists()
    }

    @Test
    fun startToFavorite() {
        composeTestRule.onNodeWithContentDescription(
            composeTestRule.activity.getString(R.string.menu)
        ).performClick()

        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.favorites), ignoreCase = true
        ).assertExists().assertIsDisplayed()

        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.favorites), ignoreCase = true
        ).performClick()

        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.app_name), ignoreCase = true
        ).assertDoesNotExist()

        composeTestRule.onNodeWithContentDescription("background4", ignoreCase = true)
            .assertExists()
    }

    @Test
    fun startToDetail() {

        composeTestRule.waitUntil(
            10000L
        ) {
            composeTestRule.onAllNodesWithTag("RestaurantCard")
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onAllNodesWithTag("RestaurantCard").onFirst()
            .assertIsDisplayed()

        composeTestRule.onAllNodesWithTag("RestaurantCard").onFirst()
            .performClick()

        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.app_name), ignoreCase = true
        ).assertDoesNotExist()

        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.detail), ignoreCase = true
        ).assertExists().assertIsDisplayed()

        composeTestRule.onNodeWithContentDescription("background2", ignoreCase = true)
            .assertExists()
    }

    @Test
    fun backGestureReturnsToPrevious() {
        composeTestRule.onNodeWithContentDescription(
            composeTestRule.activity.getString(R.string.menu)
        ).performClick()

        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.about), ignoreCase = true
        ).assertExists().assertIsDisplayed()

        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.about), ignoreCase = true
        ).performClick()

        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.app_name), ignoreCase = true
        ).assertDoesNotExist()

        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.welcome), ignoreCase = true
        ).assertExists().assertIsDisplayed()

        composeTestRule.onNodeWithContentDescription("background3", ignoreCase = true)
            .assertExists()

        Espresso.pressBack()

        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.app_name), ignoreCase = true
        ).assertExists().assertIsDisplayed()

        composeTestRule.onNodeWithContentDescription("background1", ignoreCase = true)
            .assertExists()
    }
}