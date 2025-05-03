package com.location.livetracker.presentation.map

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import com.location.livetracker.domain.model.ResponseState


data class LocationState(
    val locationResponse: ResponseState<Location?> = ResponseState.Idle,
    val mapConfigurations: MapConfigurations = MapConfigurations(),
    var latLng: LatLng = LatLng(0.0, 0.0),
    var startLatLng: LatLng? = null,
    var endLatLng: LatLng = LatLng(30.204964, 71.420652),
    var error: String = "",
    val latLngList: List<LatLng> = emptyList()
)

