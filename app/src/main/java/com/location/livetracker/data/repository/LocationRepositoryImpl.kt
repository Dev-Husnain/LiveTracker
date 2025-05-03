package com.location.livetracker.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.location.livetracker.core.utils.isPermissionGranted
import com.location.livetracker.domain.model.ResponseState
import com.location.livetracker.domain.repository.LocationRepository
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class LocationRepositoryImpl(private val context: Context) : LocationRepository {

    private val fusedLocationClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }

    private val locationRequest by lazy {
        LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 2000)
            .setMinUpdateDistanceMeters(10F).build()
    }

    private var locationCallback: LocationCallback? = null

    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocation(): ResponseState<Location?> {
        if (context.isPermissionGranted().not()) {
            return ResponseState.Error("PERMISSION_ERROR")
        }
        return try {
            val location = suspendCancellableCoroutine<Location?> { continuation ->
                fusedLocationClient.getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY, CancellationTokenSource().token
                ).addOnSuccessListener { loc ->
                    continuation.resume(loc)
                }.addOnFailureListener { e ->
                    continuation.resumeWithException(e)
                }
            }
            if (location != null) {
                ResponseState.Success(location)
            } else {
                ResponseState.Error("Unable to get location")
            }
        } catch (e: Exception) {
            ResponseState.Error(e.message.toString())
        }
    }


    override fun stopLocationUpdates() {
        locationCallback?.let {
            fusedLocationClient.removeLocationUpdates(it)
        }
    }

    @SuppressLint("MissingPermission")
    override fun startLocationUpdates() = callbackFlow {
        if (context.isPermissionGranted().not()) {
            trySend(ResponseState.Error("PERMISSION_ERROR"))
        }
        locationCallback = createLocationCallback(this)
        locationCallback?.let { callBack ->
            try {
                fusedLocationClient.requestLocationUpdates(
                    locationRequest, callBack, Looper.getMainLooper()
                )
            } catch (e: Exception) {
                trySend(ResponseState.Error(e.message.toString()))
            }
        } ?: run {
            trySend(ResponseState.Error("Some thing went wrong"))
        }
        awaitClose {
            locationCallback?.let {
                fusedLocationClient.removeLocationUpdates(it)
            }
        }
    }

    private fun createLocationCallback(channel: SendChannel<ResponseState<Location?>>): LocationCallback =
        object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let {
                    channel.trySend(ResponseState.Success(it))
                }
            }


        }
}









