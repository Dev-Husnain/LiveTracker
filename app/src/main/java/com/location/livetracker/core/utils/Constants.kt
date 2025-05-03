package com.location.livetracker.core.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Log
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MarkerState
import kotlinx.coroutines.delay


fun Context.isPermissionGranted(permissions: Array<String> = emptyArray()): Boolean {
    val mPermissions = permissions.ifEmpty {
        arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }
    return mPermissions.all {
        ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }
}

fun Activity?.shouldShowRationale(permissions: Array<String> = emptyArray()): Boolean {
    val mPermissions = permissions.ifEmpty {
        arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }
    return mPermissions.all {
        ActivityCompat.shouldShowRequestPermissionRationale(this ?: return false, it)
    }
}

fun Context.toBitmap(@DrawableRes id: Int, height: Int = 100, width: Int = 100): Bitmap {
    val drawable = AppCompatResources.getDrawable(this, id) ?: return createBitmap(width, height)
    val bitmap = createBitmap(width, height)
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, width, height)
    drawable.draw(canvas)
    return bitmap
}

fun String.logIt(tag: String = "cvv") {
    Log.d(tag, "logIt: $this")
}

fun Context.toToast(msg: String) {
    if (msg.isNotBlank()) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}

suspend fun animateMarkerState(
    markerState: MarkerState,
    from: LatLng,
    to: LatLng,
    duration: Long = 1000L
) {
    val startTime = System.currentTimeMillis()
    val endTime = startTime + duration
    while (System.currentTimeMillis() < endTime) {
        val elapsed = System.currentTimeMillis() - startTime
        val t = elapsed.toFloat() / duration
        val lat = from.latitude + t * (to.latitude - from.latitude)
        val lng = from.longitude + t * (to.longitude - from.longitude)
        markerState.position = LatLng(lat, lng)
        delay(16L)
    }
    markerState.position = to
}
