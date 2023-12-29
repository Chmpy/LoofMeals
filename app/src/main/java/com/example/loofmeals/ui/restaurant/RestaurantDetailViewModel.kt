package com.example.loofmeals.ui.restaurant

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.loofmeals.LoofMealsApplication
import com.example.loofmeals.data.IRestaurantRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * ViewModel for the RestaurantDetail screen.
 *
 * This ViewModel fetches the details of a specific restaurant from the repository
 * and updates the UI state.
 * If the API call is successful,
 * it updates the UI state with the fetched restaurant details and sets the API call state to Success.
 * If the API call fails, it sets the API call state to Error.
 *
 * @property restaurantRepository The repository to fetch the restaurant details from.
 * @property restaurantId The id of the restaurant to fetch the details of.
 */
class RestaurantDetailViewModel(
    private val restaurantRepository: IRestaurantRepository,
    private val restaurantId: Int
) : ViewModel() {

    private val _uiState = MutableStateFlow(RestaurantDetailState())
    val uiState: StateFlow<RestaurantDetailState> = _uiState.asStateFlow()

    // The state of the API call to fetch the restaurant details.
    // This is a mutable state that is updated every time the API call state changes.
    var restaurantDetailApiState: RestaurantDetailApiState by mutableStateOf(
        RestaurantDetailApiState.Loading
    )
        private set

    // Fetch the restaurant details when the ViewModel is initialized.
    init {
        getRestaurantDetail()
    }

    /**
     * Fetch the details of a specific restaurant from the repository.
     *
     * This function fetches the details of the restaurant with the given id from the repository
     * and updates the UI state.
     * If the API call is successful,
     * it updates the UI state with the fetched restaurant details and sets the API call state to Success.
     * If the API call fails, it sets the API call state to Error.
     */
    fun getRestaurantDetail() {
        viewModelScope.launch {
            restaurantDetailApiState = RestaurantDetailApiState.Loading
            try {
                restaurantRepository.getRestaurantById(restaurantId).collect { restaurantDetail ->
                    if (isActive) {
                        Log.d(
                            "RestaurantDetailViewModel",
                            "getRestaurantDetail: ${restaurantDetail.name}"
                        )
                        _uiState.update {
                            it.copy(restaurant = restaurantDetail)
                        }
                        restaurantDetailApiState = RestaurantDetailApiState.Success
                    }
                }
            } catch (e: Exception) {
                if (isActive) {
                    Log.d("RestaurantDetailViewModel", "getRestaurantDetail: ${e.message}")
                    restaurantDetailApiState = RestaurantDetailApiState.Error
                }
            }
        }
    }

    /**
     * Factory for creating RestaurantDetailViewModel instances.
     *
     * This factory uses the application container to get the restaurant repository
     * and creates a new RestaurantDetailViewModel instance with it.
     */
    companion object {
        fun Factory(restaurantId: Int): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as LoofMealsApplication)
                val restaurantRepository = application.container.IRestaurantRepository
                RestaurantDetailViewModel(restaurantRepository, restaurantId)
            }
        }
    }
}