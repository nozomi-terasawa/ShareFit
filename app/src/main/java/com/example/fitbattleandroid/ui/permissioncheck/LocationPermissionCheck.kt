package com.example.fitbattleandroid.ui.permissioncheck

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.fitbattleandroid.ui.common.Dialog
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.Priority
import kotlinx.coroutines.CoroutineScope

// TODO おおよその位置情報を設定しているときに起動する権限付与ランチャーの頻度を下げる
@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationPermissionRequest(
    modifier: Modifier,
    requestPermissionLauncher: ActivityResultLauncher<Array<String>>,
    fetchLocation: (scope: CoroutineScope) -> Unit,
    updatePriority: (priority: Int) -> Unit,
    onPermissionGranted: (Boolean) -> Unit,
) {
    val foregroundPermissions =
        listOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
        )
    val foregroundPermissionStates = rememberMultiplePermissionsState(foregroundPermissions)
    val revokedPermission = remember { mutableStateOf(emptyList<PermissionState>()) }
    LaunchedEffect(foregroundPermissionStates) {
        revokedPermission.value = foregroundPermissionStates.revokedPermissions
    }
    val priority = remember { mutableIntStateOf(Priority.PRIORITY_BALANCED_POWER_ACCURACY) }

    LaunchedEffect(revokedPermission.value) {
        priority.intValue =
            if (revokedPermission.value.any { it.permission == android.Manifest.permission.ACCESS_FINE_LOCATION }) {
                Priority.PRIORITY_BALANCED_POWER_ACCURACY
            } else {
                Priority.PRIORITY_HIGH_ACCURACY
            }
        updatePriority(priority.intValue)
    }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        foregroundPermissionStates.launchMultiplePermissionRequest()
    }
    val openDialog = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val packageName = context.packageName

    when {
        foregroundPermissionStates.allPermissionsGranted -> {
            LaunchedEffect(Unit) {
                fetchLocation(scope)
            }
            onPermissionGranted(true)
            if (openDialog.value) {
                Dialog(
                    onDismissRequest = {
                        openDialog.value = false
                    },
                    onConfirmation = {
                        openDialog.value = false
                        val intent =
                            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                data = Uri.parse("package:$packageName")
                            }
                        context.startActivity(intent)
                    },
                    dialogTitle = "位置情報を常に許可",
                    dialogText = "アプリを閉じている時に他のユーザーとマッチするために位置情報を常に許可することが必要です。",
                    icon = Icons.Default.Info,
                )
            }
        }
        foregroundPermissionStates.shouldShowRationale -> {
            RequestPermissionDialog(
                requestPermissionLauncher = requestPermissionLauncher,
            )
        }

        revokedPermission.value.isNotEmpty() -> {
            val revokedPermissions: List<String> = foregroundPermissionStates.revokedPermissions.map { it.permission }
            val isFineLocationDenied = revokedPermissions.contains(android.Manifest.permission.ACCESS_FINE_LOCATION)
            val isCoarseLocationGranted = !revokedPermissions.contains(android.Manifest.permission.ACCESS_COARSE_LOCATION)

            if (isFineLocationDenied && isCoarseLocationGranted) {
                LaunchedEffect(Unit) {
                    fetchLocation(scope)
                }
            }
        }

        else -> {
            requestPermissionLauncher.launch(
                arrayOf(
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                ),
            )
        }
    }
}

@Composable
fun RequestPermissionDialog(requestPermissionLauncher: ActivityResultLauncher<Array<String>>) {
    val showRationaleDialog = remember { mutableStateOf(true) }

    if (showRationaleDialog.value) {
        Dialog(
            onDismissRequest = {
                showRationaleDialog.value = false
            },
            onConfirmation = {
                requestPermissionLauncher.launch(
                    arrayOf(
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                    ),
                )
                showRationaleDialog.value = false
            },
            dialogTitle = "位置情報の許可",
            dialogText = "アプリの機能を使用するためには、位置情報の許可が必要です。",
            icon = Icons.Default.Info,
        )
    }
}
