package com.location.livetracker.presentation.components

import android.graphics.Bitmap
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.location.livetracker.R
import com.location.livetracker.core.utils.logIt
import com.location.livetracker.core.utils.toBitmap
import com.location.livetracker.domain.model.ResponseState
import com.location.livetracker.presentation.map.LocationState

@Composable
fun GoogleMapCompose(
    state: LocationState
) {
    val context = LocalContext.current
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var bitmapRider by remember { mutableStateOf<Bitmap?>(null) }
    var isMapLoaded by rememberSaveable {
        mutableStateOf(false)
    }
    val cameraPositionState: CameraPositionState = rememberCameraPositionState()

    val markerState = rememberMarkerState(position = state.latLng)
    val markerStateDes = rememberMarkerState()
    LaunchedEffect(key1 = state.latLng) {
        markerState.position = state.latLng
        "Current LatLng=${state.latLng}".logIt()
        cameraPositionState.position = CameraPosition.fromLatLngZoom(state.latLng, 13f)
    }
    LaunchedEffect(Unit) {
        if (bitmap == null) {
            bitmap = context.toBitmap(R.drawable.ic_location_marker)
            bitmapRider = context.toBitmap(R.drawable.ic_rider, 150, 150)
        }
    }
    LaunchedEffect(state.latLngList) {
        if (state.latLngList.isNotEmpty()) {
            markerStateDes.position = state.latLngList.last()
            "End Point=${state.latLngList.last()}".logIt()
            "Start Point=${state.latLngList.first()}".logIt()
        }
    }


    var bitmapDescriptor by remember { mutableStateOf<BitmapDescriptor?>(null) }
    var bitmapDescriptorRider by remember { mutableStateOf<BitmapDescriptor?>(null) }


    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            properties = state.mapConfigurations.properties,
            uiSettings = state.mapConfigurations.mapUiSettings,
            cameraPositionState = cameraPositionState,
            onMapLoaded = {
                bitmapDescriptor = bitmap?.let { BitmapDescriptorFactory.fromBitmap(it) }
                bitmapDescriptorRider = bitmapRider?.let { BitmapDescriptorFactory.fromBitmap(it) }
                markerState.position = state.latLng
                isMapLoaded = true
            },
            content = {
                if (isMapLoaded) {
                    Marker(
                        state = markerState,
                        icon = bitmapDescriptorRider
                    )
                    Marker(
                        state = markerStateDes,
                        icon = bitmapDescriptor
                    )
                    Polyline(
                        points = state.latLngList,
                        color = Color.Red,
                        width = 10f,
                    )
                }

            })

        AnimatedVisibility(visible = state.locationResponse is ResponseState.Loading || !isMapLoaded) {
            CircularProgressIndicator(modifier = Modifier.wrapContentHeight(), color = Color.Blue)
        }
    }

}


