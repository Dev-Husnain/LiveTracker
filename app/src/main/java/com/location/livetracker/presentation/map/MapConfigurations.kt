package com.location.livetracker.presentation.map

import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings

data class MapConfigurations(
    val properties: MapProperties = MapProperties(
        isMyLocationEnabled = false,
        isTrafficEnabled = false,
        isBuildingEnabled = true,
        mapType = MapType.NORMAL
    ),
    val mapUiSettings: MapUiSettings = MapUiSettings(
        zoomControlsEnabled = true,
        rotationGesturesEnabled = true,
        myLocationButtonEnabled = false,
        tiltGesturesEnabled = true,
        mapToolbarEnabled = false,

        ),
)


