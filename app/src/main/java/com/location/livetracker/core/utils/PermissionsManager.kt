package com.location.livetracker.core.utils

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.DefaultLifecycleObserver

class PermissionsManager(
    private val activity: Activity,
    private val permissionsLauncher: ActivityResultLauncher<Array<String>>,
    private val settingLauncher: ActivityResultLauncher<Intent>,
) : DefaultLifecycleObserver {
    private var resultCallback: ((PermissionResult) -> Unit)? = null
    private val _showSettingDialog = mutableStateOf(false)
    val showSettingDialog: State<Boolean> get() = _showSettingDialog

    fun requestPermission(onResult: ((PermissionResult) -> Unit)?) {
        resultCallback = onResult

        if (hasPermission()) {
            resultCallback?.invoke(PermissionResult.Granted)
            return
        }
        permissionsLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION))
    }

    private fun hasPermission(): Boolean {
        return activity.isPermissionGranted()
    }

    fun onPermissionResult() {
        if (hasPermission()) {
            resultCallback?.invoke(PermissionResult.Granted)
        } else {
            _showSettingDialog.value = true
        }
    }

    fun onDialogResult(goToSettings: Boolean) {
        _showSettingDialog.value = false
        if (goToSettings) {
            goToSettings()
        } else {
            resultCallback?.invoke(PermissionResult.Denied)
        }
    }

    private fun goToSettings() {
        if (activity.shouldShowRationale()) {
            requestPermission(resultCallback)
            return
        }
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", activity.packageName, null)
        }
        settingLauncher.launch(intent)
    }


}

@Composable
fun rememberPermissionsManager(): PermissionsManager {
    val context = LocalContext.current
    val activity = context as Activity
    val managerRef = remember { mutableStateOf<PermissionsManager?>(null) }

    val permissionsLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            managerRef.value?.onPermissionResult()
        }

    val settingLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            managerRef.value?.onPermissionResult()
        }

    val manager =
        remember(activity, permissionsLauncher, settingLauncher) {
            PermissionsManager(context, permissionsLauncher,
                settingLauncher
            ).also { managerRef.value = it }
        }

    ObservePermissionDialog(manager)
    return manager
}

@Composable
fun ObservePermissionDialog(manager: PermissionsManager) {
    if (manager.showSettingDialog.value) {
        AlertDialog(
            onDismissRequest = { manager.onDialogResult(false) },
            title = { Text("Permission Required") },
            text = { Text("Permission is required to continue. Please allow in the settings.") },
            confirmButton = {
                TextButton(onClick = {
                    manager.onDialogResult(true)
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { manager.onDialogResult(false) }) {
                    Text("Cancel")
                }
            }
        )
    }
}


sealed class PermissionResult {
    data object Granted : PermissionResult()
    data object Denied : PermissionResult()
}
