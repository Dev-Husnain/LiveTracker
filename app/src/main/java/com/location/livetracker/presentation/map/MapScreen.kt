package com.location.livetracker.presentation.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.location.livetracker.core.utils.PermissionResult
import com.location.livetracker.core.utils.rememberPermissionsManager
import com.location.livetracker.presentation.components.ErrorLoading
import com.location.livetracker.presentation.components.GoogleMapCompose
import org.koin.androidx.compose.koinViewModel

@Composable
fun MapScreen(
    viewModel: LocationViewModel = koinViewModel(),
) {
    val state by viewModel.locationState.collectAsStateWithLifecycle()
    val permissionManager = rememberPermissionsManager()
    LaunchedEffect(Unit) {
        permissionManager.requestPermission {
            when (it) {
                is PermissionResult.Denied -> {}
                is PermissionResult.Granted -> {
                    viewModel.startLocationUpdates()
                }
            }
        }
    }

    GoogleMapCompose(state = state)
    if (state.error.isNotEmpty()) {
        ErrorLoading(showLoading = false, error = state.error)
    }
}