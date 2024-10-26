package com.example.fitbattleandroid

import android.content.IntentSender
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import com.example.fitbattleandroid.ui.navigation.App
import com.example.fitbattleandroid.ui.screen.isBackgroundLocationPermissionGranted
import com.example.fitbattleandroid.ui.theme.FitBattleAndroidTheme
import com.example.fitbattleandroid.viewmodel.LocationViewModel
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.Task

private const val TAG = "MainActivity"
private const val PERMISSION_SETTING_TAG = "PermissionSetting"

class MainActivity : ComponentActivity() {
    companion object {
        private const val REQUEST_CHECK_SETTINGS = 1
    }

    private val locationViewModel: LocationViewModel by viewModels()
    private val backgroundPermissionGranted = mutableStateOf(false)

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 位置情報の設定を確認
        checkLocationSettings(locationViewModel)

        val requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions(),
            ) { permissions ->
                permissions.forEach { (permission, isGranted) ->
                    when (permission) {
                        android.Manifest.permission.ACCESS_FINE_LOCATION -> {
                            Log.d(TAG, "ACCESS_FINE_LOCATION: $isGranted")
                        }

                        android.Manifest.permission.ACCESS_COARSE_LOCATION -> {
                            Log.d(TAG, "ACCESS_COARSE_LOCATION: $isGranted")
                        }
                    }
                }
            }

        enableEdgeToEdge()
        setContent {
            FitBattleAndroidTheme {
                App(
                    modifier =
                        Modifier
                            .fillMaxSize(),
                    requestPermissionLauncher = requestPermissionLauncher,
                    locationViewModel = locationViewModel,
                    backgroundPermissionGranted = backgroundPermissionGranted,
                )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onResume() {
        super.onResume()
        backgroundPermissionGranted.value = isBackgroundLocationPermissionGranted(this)

        /* 位置情報の更新を開始
        // TODO こっちの位置情報を使用して現在地をトラッキングする
        locationViewModel.startLocationUpdates()
         */
    }

    override fun onPause() {
        super.onPause()

        /* 位置情報の更新を停止
        locationViewModel.stopLocationUpdates()
         */
    }

    private fun checkLocationSettings(locationViewModel: LocationViewModel) {
        val builder =
            LocationSettingsRequest
                .Builder()
                .addLocationRequest(locationViewModel.locationRequest)
        val client: SettingsClient = LocationServices.getSettingsClient(this)

        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener { response ->
            Log.d(PERMISSION_SETTING_TAG, "GPS Enabled: ${response.locationSettingsStates?.isGpsUsable}") // GPSが有効かどうか
            Log.d(PERMISSION_SETTING_TAG, "Network Location Enabled: ${response.locationSettingsStates?.isNetworkLocationUsable}") // ネットワーク位置情報が有効かどうか
            Log.d(PERMISSION_SETTING_TAG, "Ble Location Enabled: ${response.locationSettingsStates?.isBleUsable}") // BLE位置情報が有効かどうか
            Log.d(PERMISSION_SETTING_TAG, "Location Settings are satisfied: ${response.locationSettingsStates?.isLocationUsable}") // 位置情報が有効かどうか
        }
        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    exception.startResolutionForResult(
                        this@MainActivity,
                        REQUEST_CHECK_SETTINGS,
                    )
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }
    }
}

/*
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FitBattleAndroidTheme {
        App()
    }
}

 */
