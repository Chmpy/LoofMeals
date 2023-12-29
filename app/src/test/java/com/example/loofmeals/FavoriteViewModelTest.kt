package com.example.loofmeals

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.loofmeals.data.IRestaurantRepository
import com.example.loofmeals.data.model.Restaurant
import com.example.loofmeals.ui.favorite.FavoriteApiState
import com.example.loofmeals.ui.favorite.FavoriteViewModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotSame
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doAnswer

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class FavoriteViewModelTest {

    private lateinit var viewModel: FavoriteViewModel

    @Mock
    private lateinit var mockRepository: IRestaurantRepository

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    val testCoroutineRule = MainDispatcher()

    private val restaurant1 = Restaurant(1, name = "test1", isFavorite = true)
    private val restaurant2 = Restaurant(2, name = "test2")
    private val restaurants = listOf(restaurant1, restaurant2)

    @Before
    fun setup() = runBlocking {

        `when`(mockRepository.getFavoriteRestaurants()).thenReturn(flowOf(listOf(restaurant1)))
        `when`(mockRepository.updateRestaurant(restaurant1)).thenReturn(Unit)

        viewModel = FavoriteViewModel(mockRepository)
    }


    @Test
    fun fetchesRestaurantsSuccessfully() = runTest {

        viewModel.getFavoriteRestaurants()
        advanceTimeBy(10000)

        assertEquals(FavoriteApiState.Success, viewModel.favoriteApiState)
        assertEquals(listOf(restaurant1), viewModel.uiState.value.restaurants)
        assertEquals(true, viewModel.uiState.value.restaurants[0].isFavorite)
    }

    @Test
    fun fetchesRestaurantsUnsuccessfully() = runTest {
        `when`(mockRepository.getFavoriteRestaurants()).doAnswer { throw Exception() }

        viewModel.getFavoriteRestaurants()
        advanceTimeBy(10000)

        assertEquals(FavoriteApiState.Error, viewModel.favoriteApiState)
    }

    @Test
    fun fetchesRestaurantsNotInclude() = runTest {

        viewModel.getFavoriteRestaurants()
        advanceTimeBy(10000)

        assertNotSame(listOf(restaurant2), viewModel.uiState.value.restaurants)
        assertNotSame(restaurants, viewModel.uiState.value.restaurants)
    }

    @Test
    fun updateFavoriteSuccessfully() = runTest {
        val restaurant = restaurant1

        viewModel.updateFavorite(restaurant)
        advanceTimeBy(10000)

        assertEquals(false, viewModel.uiState.value.restaurants[0].isFavorite)
    }
}