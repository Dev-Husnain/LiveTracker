package com.location.livetracker.domain.repository

import com.google.android.gms.maps.model.LatLng
import com.location.livetracker.domain.model.ResponseState


interface RoutesRepository {
    suspend fun getRoutes(fromLatLng: LatLng, toLatLng: LatLng): ResponseState<List<LatLng>>
}