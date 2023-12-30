package com.example.loofmeals

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.loofmeals.data.CachingRestaurantRepository
import com.example.loofmeals.data.IRestaurantRepository
import com.example.loofmeals.data.database.RestaurantDao
import com.example.loofmeals.data.database.asDomainObject
import com.example.loofmeals.data.database.asRestaurantEntity
import com.example.loofmeals.data.model.Restaurant
import com.example.loofmeals.network.RestaurantApiService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoInteractions
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CachingRestaurantRepositoryTest {

    @Mock
    private lateinit var mockRestaurantApiService: RestaurantApiService

    @Mock
    private lateinit var mockRestaurantDao: RestaurantDao

    @Mock
    private lateinit var repository: IRestaurantRepository

    private val restaurant1 = Restaurant(1, name = "Restaurant 1")
    private val restaurant2 = Restaurant(2, name = "Restaurant 2")
    private val restaurantEntity1 = restaurant1.asRestaurantEntity()
    private val restaurantEntity2 = restaurant2.asRestaurantEntity()

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    val testCoroutineRule = MainDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        repository = CachingRestaurantRepository(mockRestaurantApiService, mockRestaurantDao)
    }

//    @Test
//    fun `getRestaurantList should fetch from local database and refresh from network if empty`() =
//        runBlocking {
//            `when`(mockRestaurantDao.getAllRestaurants()).thenReturn(flowOf(emptyList()))
//
//            `whenever`(mockRestaurantApiService.getRestaurantsAsFlow()).doReturn(flow {
//                emit(listOf(restaurant1, restaurant2))
//            })
//
//            repository.getRestaurantList().collect { result ->
//                assertEquals(result.size, 2)
//            }
//
//            // Verify that the refreshRestaurantList method is called when the local database is empty
//            verify(mockRestaurantApiService).getRestaurantsAsFlow()
//            verify(mockRestaurantDao, times(1)).insertRestaurant(restaurant1.asRestaurantEntity())
//            verify(mockRestaurantDao, times(1)).insertRestaurant(restaurant2.asRestaurantEntity())
//        }

    @Test
    fun `getRestaurantList should fetch from local database and not refresh if not empty`() =
        runBlocking {
            `when`(mockRestaurantDao.getAllRestaurants()).thenReturn(
                flowOf(
                    listOf(
                        restaurantEntity1, restaurantEntity2
                    )
                )
            )

            repository.getRestaurantList().collect { result ->
                assertEquals(result.size, 2)
            }

            // Verify that the refreshRestaurantList method is not called when the local database is not empty
            verifyNoInteractions(mockRestaurantApiService)
        }

    @Test
    fun `getFilteredRestaurants should fetch from local database`(): Unit = runBlocking {
        `when`(mockRestaurantDao.getFilteredRestaurants("Restaurant")).thenReturn(
            flowOf(
                listOf(
                    restaurantEntity1, restaurantEntity2
                )
            )
        )

        repository.getFilteredRestaurants("Restaurant").collect { result ->
            assertEquals(result.size, 2)
        }

        // Verify that the getFilteredRestaurants method is called with the correct query
        verify(mockRestaurantDao).getFilteredRestaurants("Restaurant")
    }

    @Test
    fun `getRestaurantById should fetch from local database`(): Unit = runBlocking {
        `when`(mockRestaurantDao.getRestaurantById(1)).thenReturn(flowOf(restaurantEntity1))

        repository.getRestaurantById(1).collect { result ->
            assertEquals(result, restaurantEntity1.asDomainObject())
        }

        // Verify that the getRestaurantById method is called with the correct id
        verify(mockRestaurantDao).getRestaurantById(1)
    }

    @Test
    fun `getFavoriteRestaurants should fetch from local database`(): Unit = runBlocking {
        `when`(mockRestaurantDao.getFavoriteRestaurants()).thenReturn(
            flowOf(
                listOf(
                    restaurantEntity1, restaurantEntity2
                )
            )
        )

        repository.getFavoriteRestaurants().collect { result ->
            assertEquals(result.size, 2)
        }

        // Verify that the getFavoriteRestaurants method is called
        verify(mockRestaurantDao).getFavoriteRestaurants()
    }

    @Test
    fun `updateRestaurant should update the local database`() = runBlocking {
        repository.updateRestaurant(restaurant1)

        // Verify that the updateRestaurant method is called with the correct argument
        verify(mockRestaurantDao).updateRestaurant(restaurantEntity1)
    }

    @Test
    fun `insertRestaurant should insert into the local database`() = runBlocking {
        repository.insertRestaurant(restaurant1)

        // Verify that the insertRestaurant method is called with the correct argument
        verify(mockRestaurantDao).insertRestaurant(restaurantEntity1)
    }

//    @Test
//    fun `refreshRestaurantList should fetch from the network and update the local database`(): Unit =
//        runBlocking {
//            // Stubbing the behavior of your mock
//            `whenever`(mockRestaurantApiService.getRestaurantsAsFlow()).thenReturn(flow {
//                emit(listOf(restaurant1, restaurant2))
//            })
//
//            // Your actual test code...
//            repository.refreshRestaurantList()
//
//            // Verification that the mock method was called
//            verify(mockRestaurantApiService).getRestaurantsAsFlow()
//
//            // Verify that the refreshRestaurantList method updates the local database with the correct data
//            verify(mockRestaurantDao, times(1)).insertRestaurant(restaurant1.asRestaurantEntity())
//            verify(mockRestaurantDao, times(1)).insertRestaurant(restaurant2.asRestaurantEntity())
//
//            // Additional assertions can be added based on your specific implementation
//            repository.getRestaurantList().collect { result ->
//                assertEquals(result.size, 2)
//            }
//        }

//    @Test
//    fun `refreshRestaurantList should handle network errors gracefully`() = runBlocking {
//        `whenever`(mockRestaurantApiService.getRestaurantsAsFlow()).thenAnswer { throw IOException() }
//
//        repository.refreshRestaurantList()
//
//        // Verify that the refreshRestaurantList method handles network errors gracefully
//        // Additional assertions can be added based on your specific implementation
//        verifyNoInteractions(mockRestaurantDao)
//    }
}
