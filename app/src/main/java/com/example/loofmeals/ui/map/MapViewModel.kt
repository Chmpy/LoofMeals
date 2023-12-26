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

class MapViewModel(
    private val restaurantRepository: RestaurantRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MapState())
    val uiState: StateFlow<MapState> = _uiState.asStateFlow()

    var mapApiState: MapApiState by mutableStateOf(MapApiState.Loading)
        private set

    init {
        getRestaurants()
    }

    fun getRestaurants() {
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
                                    id = restaurant.id!!
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