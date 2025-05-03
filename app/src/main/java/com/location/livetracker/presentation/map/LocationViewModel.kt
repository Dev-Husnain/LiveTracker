package com.location.livetracker.presentation.map

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.location.livetracker.core.utils.logIt
import com.location.livetracker.domain.model.ResponseState
import com.location.livetracker.domain.repository.LocationRepository
import com.location.livetracker.domain.repository.RoutesRepository
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LocationViewModel(
    private val repo: LocationRepository,
    private val routesRepo: RoutesRepository
) : ViewModel() {

    private val _locationState = MutableStateFlow(LocationState())
    val locationState: StateFlow<LocationState> = _locationState.asStateFlow()

    fun startLocationUpdates() {
        _locationState.update { it.copy(locationResponse = ResponseState.Loading) }
        viewModelScope.launch {
            repo.startLocationUpdates().collectLatest { response ->
                ensureActive()
                handleResponse(response)
            }
        }
    }

    private fun handleResponse(response: ResponseState<Location?>) {
        when (response) {
            is ResponseState.Success -> {
                response.data?.let { loc ->
                    val newLatLng = LatLng(loc.latitude, loc.longitude)
                    _locationState.update { state ->
                        state.copy(
                            latLng = newLatLng,
                            startLatLng = state.startLatLng ?: newLatLng
                        )
                    }

                    getRoutes()
                }
            }

            is ResponseState.Error -> {
                val errorMessage = response.error.ifBlank { "Something went wrong" }
                _locationState.update { it.copy(error = errorMessage) }
                "From LocationViewModel: $errorMessage".logIt()
            }

            else -> Unit
        }

        _locationState.update { it.copy(locationResponse = response) }
    }

    private fun getRoutes() {
        val state = _locationState.value
        if (state.latLngList.isNotEmpty()) {
            return
        }
        val fromLatLng = state.startLatLng ?: return
        val toLatLng = state.endLatLng
        viewModelScope.launch {
            when (val response = routesRepo.getRoutes(fromLatLng, toLatLng)) {
                is ResponseState.Error -> {
                    _locationState.update { it.copy(error = response.error) }
                    "Failed to Routes: ${response.error}".logIt()
                }

                ResponseState.Idle -> {}
                ResponseState.Loading -> {}
                is ResponseState.Success<*> -> {
                    _locationState.update {
                        it.copy(
                            latLngList = (response as? ResponseState.Success)?.data ?: emptyList()
                        )
                    }
                }
            }

        }
    }

    override fun onCleared() {
        super.onCleared()
        repo.stopLocationUpdates()
    }
}

