package com.example.loofmeals

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.loofmeals.data.IRestaurantRepository
import com.example.loofmeals.data.model.Restaurant
import com.example.loofmeals.ui.restaurant.RestaurantDetailApiState
import com.example.loofmeals.ui.restaurant.RestaurantDetailViewModel
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
class RestaurantDetailViewModelTest {

    private lateinit var viewModel: RestaurantDetailViewModel

    @Mock
    private lateinit var mockRepository: IRestaurantRepository

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    val testCoroutineRule = MainDispatcher()

    private val restaurant = Restaurant(1, name = "test1")

    @Before
    fun setup() {

        `when`(mockRepository.getRestaurantById(1)).thenReturn(flowOf(restaurant))

        viewModel = RestaurantDetailViewModel(mockRepository, 1)
    }

    @Test
    fun fetchesRestaurantSuccessfully() = runTest {

        viewModel.getRestaurantDetail()
        advanceTimeBy(10000)

        assertEquals(RestaurantDetailApiState.Success, viewModel.restaurantDetailApiState)
        assertEquals(restaurant, viewModel.uiState.value.restaurant)
    }

    @Test
    fun fetchesRestaurantUnsuccessfully() = runTest {
        `when`(mockRepository.getRestaurantById(1)).doAnswer { throw Exception() }

        viewModel.getRestaurantDetail()
        advanceTimeBy(10000)

        assertEquals(RestaurantDetailApiState.Error, viewModel.restaurantDetailApiState)
    }

}