package com.example.loofmeals

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.loofmeals.data.IRestaurantRepository
import com.example.loofmeals.data.model.Restaurant
import com.example.loofmeals.ui.map.MapApiState
import com.example.loofmeals.ui.map.MapViewModel
import com.example.loofmeals.ui.map.RestaurantMarker
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
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
class MapViewModelTest {

    private lateinit var viewModel: MapViewModel

    @Mock
    private lateinit var mockRepository: IRestaurantRepository

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    val testCoroutineRule = MainDispatcher()

    private val restaurant1 = Restaurant(1, name = "test1", lat = "1", long = "1")
    private val restaurant2 = Restaurant(2, name = "test2", lat = "2", long = "2")
    private val restaurants = listOf(restaurant1, restaurant2)

    @Before
    fun setup() {

        `when`(mockRepository.getRestaurantList()).thenReturn(flowOf(restaurants))

        viewModel = MapViewModel(mockRepository)
    }

    @Test
    fun fetchesRestaurantsSuccessfully() = runTest {
        val restaurantMarkers = restaurants.map {
            RestaurantMarker(
                lat = it.lat!!.toDouble(),
                long = it.long!!.toDouble(),
                title = it.name!!,
                id = it.id
            )
        }

        viewModel.getRestaurants()
        advanceTimeBy(10000)

        assertEquals(MapApiState.Success, viewModel.mapApiState)
        assertEquals(restaurantMarkers, viewModel.uiState.value.markers)
    }

    @Test
    fun fetchesRestaurantsUnsuccessfully() = runTest {
        `when`(mockRepository.getRestaurantList()).doAnswer { throw Exception() }

        viewModel.getRestaurants()
        advanceTimeBy(10000)

        assertEquals(MapApiState.Error, viewModel.mapApiState)
    }

}