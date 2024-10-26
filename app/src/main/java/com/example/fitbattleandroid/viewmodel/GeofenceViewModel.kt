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
import androidx.lifecycle.viewModelScope
import com.example.fitbattleandroid.data.NewPassingInfo
import com.example.fitbattleandroid.receiver.GeofenceBroadcastReceiver
import com.example.fitbattleandroid.repositoryImpl.WebSocketRepositoryImpl
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

private const val TAG = "GeofencingClientViewModel"

class GeofencingClientViewModel(
    application: Application,
) : AndroidViewModel(application) {
    private val applicationContext = application.applicationContext
    private val geofencingClient: GeofencingClient =
        LocationServices.getGeofencingClient(applicationContext)

    private val webSocketRepository =
        WebSocketRepositoryImpl(
            client =
                HttpClient(CIO) {
                    install(WebSockets)
                },
        )

    fun connectWebSocket() {
        viewModelScope.launch {
            webSocketRepository.connect {
                for (message in incoming) {
                    when (message) {
                        is Frame.Text -> {
                            val text = message.readText()
                            val parsedMessage = Json.decodeFromString<NewPassingInfo>(text)
                            Log.d("WebSocket⭐️", parsedMessage.toString())
                        }
                        is Frame.Binary -> TODO()
                        is Frame.Close -> TODO()
                        is Frame.Ping -> TODO()
                        is Frame.Pong -> TODO()
                    }
                }
            }
        }
    }

    private var _geofenceList = mutableStateListOf<Geofence>()
    val geofenceList: List<Geofence> = _geofenceList

    // TODO サーバーサイドから受け取る
    private val entry =
        mutableListOf(
            GeofenceData(
                "KIT",
                36.53017077369422,
                136.62784179442636,
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
