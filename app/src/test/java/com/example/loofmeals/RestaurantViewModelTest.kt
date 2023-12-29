package com.example.loofmeals

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.loofmeals.data.IRestaurantRepository
import com.example.loofmeals.data.model.Restaurant
import com.example.loofmeals.ui.restaurant.RestaurantApiState
import com.example.loofmeals.ui.restaurant.RestaurantViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import okio.IOException
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doAnswer

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class RestaurantViewModelTest {

    private lateinit var viewModel: RestaurantViewModel

    @Mock
    private lateinit var mockRepository: IRestaurantRepository

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    val testCoroutineRule = MainDispatcher()

    private val restaurant1 = Restaurant(1, name = "test1")
    private val restaurant2 = Restaurant(2, name = "test2")
    private val restaurants = listOf(restaurant1, restaurant2)
    @Before
    fun setup() = runBlocking {

        `when`(mockRepository.getRestaurantList()).thenReturn(flowOf(restaurants))
        `when`(mockRepository.getFilteredRestaurants("test")).thenReturn(flowOf(restaurants))
        `when`(mockRepository.getFilteredRestaurants("test2")).thenReturn(flowOf(listOf(restaurant2)))
        `when`(mockRepository.getFilteredRestaurants("test3")).thenReturn(flowOf(emptyList()))
        `when`(mockRepository.updateRestaurant(restaurant1)).thenReturn(Unit)

        viewModel = RestaurantViewModel(mockRepository)
    }


    @Test
    fun fetchesRestaurantsSuccessfully() = runTest {

        viewModel.getRestaurants()

        // Simulate the passage of time
        advanceTimeBy(10000)

        assertEquals(restaurants, viewModel.uiState.value.restaurants)
    }

    @Test
    fun fetchesRestaurantsWithError() = runTest {
        `when`(mockRepository.getRestaurantList()).doAnswer {
            throw IOException()
        }

        viewModel.getRestaurants()
        advanceTimeBy(10000)

        assertEquals(RestaurantApiState.NetworkError, viewModel.restaurantApiState)
    }

    @Test
    fun refreshesRestaurantsSuccessfully() = runTest {
        `when`(mockRepository.refreshRestaurantList()).thenReturn(Unit)

        viewModel.refreshRestaurants()
        advanceTimeBy(10000)

        assertEquals(RestaurantApiState.Success, viewModel.restaurantApiState)
    }

    @Test
    fun refreshesRestaurantsWithError() = runTest {
        `when`(mockRepository.getRestaurantList()).doAnswer {
            throw IOException()
        }

        viewModel.refreshRestaurants()
        advanceTimeBy(10000)

        assertEquals(RestaurantApiState.NetworkError, viewModel.restaurantApiState)
    }

    @Test
    fun filtersRestaurantsSuccessfully() = runTest {

        viewModel.filterRestaurants("test")
        advanceTimeBy(10000)

        assertEquals(restaurants, viewModel.uiState.value.restaurants)
    }

    @Test
    fun filtersRestaurantsSuccessfully2() = runTest {

        viewModel.filterRestaurants("test2")
        advanceTimeBy(10000)

        assertEquals(listOf(restaurant2), viewModel.uiState.value.restaurants)
    }

    @Test
    fun filtersRestaurantsSuccessfully3() = runTest {

        viewModel.filterRestaurants("test3")
        advanceTimeBy(10000)

        assertEquals(emptyList<Restaurant>(), viewModel.uiState.value.restaurants)
    }

    @Test
    fun updatesFavoriteStatusSuccessfully() = runTest {
        val restaurant = restaurant1

        viewModel.updateFavorite(restaurant)
        advanceTimeBy(10000)

        assertEquals(true, restaurant.isFavorite)
    }
}