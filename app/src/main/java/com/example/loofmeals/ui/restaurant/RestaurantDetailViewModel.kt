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
import com.example.loofmeals.data.RestaurantRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class RestaurantDetailViewModel(
    private val restaurantRepository: RestaurantRepository,
    private val restaurantId: Int
) : ViewModel() {

    private val _uiState = MutableStateFlow(RestaurantDetailState())
    val uiState: StateFlow<RestaurantDetailState> = _uiState.asStateFlow()

    var restaurantDetailApiState: RestaurantDetailApiState by mutableStateOf(
        RestaurantDetailApiState.Loading
    )
        private set

    init {
        getRestaurantDetail()
    }

    fun getRestaurantDetail() {
        viewModelScope.launch {
            restaurantDetailApiState = RestaurantDetailApiState.Loading
            try {
                restaurantRepository.getRestaurantById(restaurantId).collect { restaurantDetail ->
                    if (isActive) {
                        Log.d("RestaurantDetailViewModel", "getRestaurantDetail: ${restaurantDetail.name}")
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

    companion object {
        fun Factory(restaurantId: Int): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as LoofMealsApplication)
                val restaurantRepository = application.container.restaurantRepository
                RestaurantDetailViewModel(restaurantRepository, restaurantId)
            }
        }
    }
}