package com.example.loofmeals.ui.map

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
import com.example.loofmeals.data.RestaurantRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel for the Map screen.
 *
 * This ViewModel fetches the restaurants from the repository and updates the UI state.
 * If the API call is successful,
 * it updates the UI state with the fetched restaurants and sets the API call state to Success.
 * If the API call fails, it sets the API call state to Error.
 *
 * @property restaurantRepository The repository to fetch the restaurants from.
 */
class MapViewModel(
    private val restaurantRepository: RestaurantRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MapState())
    val uiState: StateFlow<MapState> = _uiState.asStateFlow()

    // The state of the API call to fetch the restaurants.
    // This is a mutable state that is updated every time the API call state changes.
    var mapApiState: MapApiState by mutableStateOf(MapApiState.Loading)

    // Fetch the restaurants when the ViewModel is initialized.
    init {
        getRestaurants()
    }

    /**
     * Fetch the restaurants from the repository.
     *
     * This function fetches the restaurants from the repository and updates the UI state.
     * If the API call is successful,
     * it updates the UI state with the fetched restaurants and sets the API call state to Success.
     * If the API call fails, it sets the API call state to Error.
     */
    private fun getRestaurants() {
        viewModelScope.launch {
            mapApiState = MapApiState.Loading
            try {
                restaurantRepository.getRestaurantList().collect { restaurants ->
                    Log.d("MapViewModel", "getRestaurants: ${restaurants.size}")
                    _uiState.update {
                        it.copy(
                            markers = restaurants.mapNotNull { restaurant ->
                                if (restaurant.lat != "" && restaurant.long != "")
                                    RestaurantMarker(
                                        lat = restaurant.lat!!.toDouble(),
                                        long = restaurant.long!!.toDouble(),
                                        title = restaurant.name!!,
                                        id = restaurant.id
                                    )
                                else null
                            }
                        )
                    }
                    mapApiState = MapApiState.Success
                }
            } catch (e: Exception) {
                Log.d("RestaurantViewModel", "getRestaurants: ${e.message}")
                mapApiState = MapApiState.Error
            }
        }
    }

    /**
     * Factory for creating MapViewModel instances.
     *
     * This factory uses the application container to get the restaurant repository
     * and creates a new MapViewModel instance with it.
     */
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as LoofMealsApplication)
                val restaurantRepository = application.container.restaurantRepository
                MapViewModel(restaurantRepository)
            }
        }
    }

}