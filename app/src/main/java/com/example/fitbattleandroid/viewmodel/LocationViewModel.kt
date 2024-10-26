package com.example.fitbattleandroid.viewmodel

import android.app.Application
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.Tasks
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

private const val TAG = "LocationViewModel"

class LocationViewModel(
    application: Application,
) : AndroidViewModel(application) {
    private var _location = MutableStateFlow(LocationData(0.0, 0.0, Priority.PRIORITY_BALANCED_POWER_ACCURACY))
    val location: StateFlow<LocationData> = _location.asStateFlow()

    private val applicationContext = application.applicationContext
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(applicationContext)

    private var _locationRequest: LocationRequest = createLocationRequest()
    val locationRequest: LocationRequest = _locationRequest

    private val locationCallback =
        object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    _location.value = _location.value.copy(location.latitude, location.longitude)
                }
            }
        }

    private var isLocationUpdatesActive = false

    fun updatePriority(priority: Int) {
        _location.value =
            _location.value.copy(
                priority = priority,
            )
        /* priorityの確認
        Log.d("LocationViewModel", locationRequest.priority.toPriorityString())
         */
    }

    // 　位置情報の設定
    fun createLocationRequest(): LocationRequest =
        LocationRequest
            .Builder(5000)
            .setPriority(_location.value.priority)
            .build()

    // 位置情報の取得
    fun fetchLocation(): LocationData {
        try {
            val result =
                Tasks.await(
                    fusedLocationClient.lastLocation
                        .addOnSuccessListener { location: Location? -> // Log.d("result", "緯度: ${location?.latitude}, 経度: ${location?.longitude}")
                        },
                )
            _location.value =
                _location.value.copy(
                    latitude = result.latitude,
                    longitude = result.longitude,
                )
        } catch (e: SecurityException) {
            // 権限が付与されていない場合
            Log.d(TAG, e.toString())
        }
        return _location.value
    }

    // 位置情報の更新を開始
    fun startLocationUpdates() {
        if (!isLocationUpdatesActive) {
            try {
                fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.getMainLooper(),
                )
            } catch (e: SecurityException) {
                Log.d(TAG, e.toString())
            }
        }
    }

    // 位置情報の更新を停止
    fun stopLocationUpdates() {
        if (isLocationUpdatesActive) {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }
}

data class LocationData(
    val latitude: Double, // 緯度
    val longitude: Double, // 経度
    val priority: Int,
)

fun Int.toPriorityString(): String =
    when (this) {
        Priority.PRIORITY_BALANCED_POWER_ACCURACY -> "PRIORITY_BALANCED_POWER_ACCURACY"
        Priority.PRIORITY_HIGH_ACCURACY -> "PRIORITY_HIGH_ACCURACY"
        Priority.PRIORITY_LOW_POWER -> "PRIORITY_LOW_POWER"
        else -> "UNKNOWN_PRIORITY"
    }
