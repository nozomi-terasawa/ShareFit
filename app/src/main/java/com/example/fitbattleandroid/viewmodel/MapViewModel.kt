package com.example.fitbattleandroid.viewmodel

import android.Manifest
import android.app.Application
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.core.app.ActivityCompat
import androidx.lifecycle.AndroidViewModel
import com.example.fitbattleandroid.receiver.GeofenceBroadcastReceiver
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.Tasks
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

private const val TAG = "MapViewModel"

class MapViewModel(
    application: Application,
) : AndroidViewModel(application) {
    private val applicationContext = application.applicationContext

    // ジオフェンス
    private val geofencingClient: GeofencingClient =
        LocationServices.getGeofencingClient(applicationContext)
    private var _geofenceList = mutableStateListOf<Geofence>()
    val geofenceList: List<Geofence> = _geofenceList

    // 位置情報
    private var _location =
        MutableStateFlow(
            LocationData(0.0, 0.0, Priority.PRIORITY_BALANCED_POWER_ACCURACY),
        )
    val location: StateFlow<LocationData> = _location.asStateFlow()
    private val fusedLocationClient =
        LocationServices.getFusedLocationProviderClient(applicationContext)
    private var _locationRequest: LocationRequest = createLocationRequest()
    val locationRequest: LocationRequest = _locationRequest
    private var isLocationUpdatesActive = false

    private val locationCallback =
        object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    _location.value =
                        _location.value.copy(
                            latitude = location.latitude,
                            longitude = location.longitude,
                            priority = _location.value.priority,
                        )
                }
            }
        }

    fun updatePriority(priority: Int) {
        _location.value =
            _location.value.copy(
                priority = priority,
            )
        /* priorityの確認
        Log.d("LocationViewModel", locationRequest.priority.toPriorityString())
         */
    }

    fun updateLocationRequest() {
        _locationRequest = createLocationRequest()
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

// TODO サーバーサイドから受け取る
    private val entry =
        mutableListOf(
            GeofenceData(
                "KIT",
                36.53252814659672,
                136.62909188593616,
                100f,
            ),
        )

    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(applicationContext, GeofenceBroadcastReceiver::class.java)
        PendingIntent.getBroadcast(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE,
        )
    }

    // TODO entryが更新されたら呼び出す
    // entryから受け取ったジオフェンスをリストに追加
    fun addGeofence() {
        entry.forEach { entry ->
            _geofenceList.add(
                Geofence
                    .Builder()
                    .setRequestId(entry.requestId)
                    .setCircularRegion(
                        entry.latitude,
                        entry.longitude,
                        entry.radius,
                    ).setExpirationDuration(Geofence.NEVER_EXPIRE)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
                    // .setLoiteringDelay(60000)
                    .build(),
            )
        }
    }

    private fun getGeofencingRequest(): GeofencingRequest =
        GeofencingRequest
            .Builder()
            .apply {
                setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                addGeofences(_geofenceList)
            }.build()

    // リストのジオフェンスを登録
    fun registerGeofence() {
        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION,
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        if (_geofenceList.isEmpty()) {
            return
        }

        geofencingClient.addGeofences(getGeofencingRequest(), geofencePendingIntent).run {
            addOnSuccessListener {
            }
            addOnFailureListener { e ->
                val errorCode = (e as? ApiException)?.statusCode
                val errorMessage = GeofenceStatusCodes.getStatusCodeString(errorCode ?: -1)
                Log.d(TAG, errorMessage)
            }
        }
    }
}

data class GeofenceData(
    val requestId: String,
    val latitude: Double,
    val longitude: Double,
    val radius: Float,
)

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
