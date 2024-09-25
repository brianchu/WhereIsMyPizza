package com.example.whereismypizza.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.whereismypizza.BuildConfig
import com.example.whereismypizza.model.Business
import com.example.whereismypizza.model.api.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * State object flowing around between View and View Model.
 */
sealed class State() {
    data object Loading : State()
    data class Success(val businesses: List<Business>) : State()
    data class Fail(val reason: String) : State()
    data class Empty(val reason: String) : State()
}

/**
 * Restaurant View Model encapsulate the handling of data and allowing View to observe the data changes.
 */
class RestaurantViewModel : ViewModel() {
    private var _state = MutableStateFlow<State>(State.Loading)
    var state = _state.asStateFlow()

    init {
        fetch()
    }

    private fun fetch() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Make 2 requests in parallel
                val beer = async {
                    ApiService.api.getRestaurant(
                        authorization = BuildConfig.YELP_KEY,
                        searchTerm = BEER,
                        location = LOCATION
                    )
                }

                val pizza = async {
                    ApiService.api.getRestaurant(
                        authorization = BuildConfig.YELP_KEY,
                        searchTerm = PIZZA,
                        location = LOCATION
                    )
                }

                // and then combine the arrays as we can only care about that for now.
                val combined = beer.await().businesses + pizza.await().businesses

                _state.value = State.Success(combined)
            } catch (e: Exception) {
                _state.value = State.Fail(e.localizedMessage ?: "unknown error")
            }
        }
    }

    companion object {
        private const val PIZZA = "pizza"
        private const val BEER = "beer"
        private const val LOCATION = "111 Shutter, San Francisco, CA"
    }
}