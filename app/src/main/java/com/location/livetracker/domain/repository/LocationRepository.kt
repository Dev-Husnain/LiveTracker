package com.location.livetracker.domain.repository

import android.location.Location
import com.location.livetracker.domain.model.ResponseState
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    suspend fun getCurrentLocation(): ResponseState<Location?>
    fun startLocationUpdates(): Flow<ResponseState<Location?>>
    fun stopLocationUpdates()
}