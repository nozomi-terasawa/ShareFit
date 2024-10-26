package com.example.fitbattleandroid.receiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.fitbattleandroid.notification.sendGeofenceNotification
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent

class GeofenceBroadcastReceiver : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(
        context: Context,
        intent: Intent?,
    ) {
        if (intent == null) {
            return
        }

        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        if (geofencingEvent == null) {
            return
        }
        if (geofencingEvent.hasError()) {
            val errorMessage =
                GeofenceStatusCodes
                    .getStatusCodeString(geofencingEvent.errorCode)
            Log.e(TAG, errorMessage)
            return
        }

        val geofenceTransition = geofencingEvent.geofenceTransition

        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
            geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT ||
            geofenceTransition == Geofence.GEOFENCE_TRANSITION_DWELL
        ) {
            val triggeringGeofences = geofencingEvent.triggeringGeofences

            val geofenceTransitionDetails =
                triggeringGeofences?.let {
                    getGeofenceTransitionDetails(
                        geofenceTransition,
                        it,
                    )
                }

            sendGeofenceNotification(
                context,
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager,
                "ジオフェンス通知",
                "$geofenceTransitionDetails",
            )
        }
    }

    private fun getGeofenceTransitionDetails(
        geofenceTransition: Int,
        triggeringGeofences: MutableList<Geofence>,
    ): String {
        val latestGeofenceIndex = triggeringGeofences.size - 1
        return when (geofenceTransition) {
            Geofence.GEOFENCE_TRANSITION_ENTER -> "${triggeringGeofences[latestGeofenceIndex].requestId}の中に入りました"
            Geofence.GEOFENCE_TRANSITION_EXIT -> "${triggeringGeofences[latestGeofenceIndex].requestId}から出ました"
            else -> "不明"
        }
    }
}
