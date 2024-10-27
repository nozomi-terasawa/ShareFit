package com.example.fitbattleandroid.viewmodel

import android.Manifest
import android.app.Application
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
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
import com.google.android.gms.location.LocationServices

private const val TAG = "GeofencingClientViewModel"

class GeofencingClientViewModel(
    application: Application,
) : AndroidViewModel(application) {
    private val applicationContext = application.applicationContext
    private val geofencingClient: GeofencingClient =
        LocationServices.getGeofencingClient(applicationContext)

    private var _geofenceList = mutableStateListOf<Geofence>()
    val geofenceList: List<Geofence> = _geofenceList

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
