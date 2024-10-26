package com.example.fitbattleandroid.ui.screen

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fitbattleandroid.ui.permissioncheck.LocationPermissionRequest
import com.example.fitbattleandroid.viewmodel.GeofencingClientViewModel
import com.example.fitbattleandroid.viewmodel.LocationData
import com.example.fitbattleandroid.viewmodel.LocationViewModel
import com.google.android.gms.location.Geofence
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun MapScreen(
    modifier: Modifier,
    requestPermissionLauncher: ActivityResultLauncher<Array<String>>,
    locationViewModel: LocationViewModel,
    geofenceViewModel: GeofencingClientViewModel = viewModel(),
    backgroundPermissionGranted: MutableState<Boolean>,
) {
    val locationData = locationViewModel.location.collectAsState().value
    val geofenceList = geofenceViewModel.geofenceList
    val scope = rememberCoroutineScope()
    val currentLocation = remember { mutableStateOf(locationData) }
    val permissionGranted = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        geofenceViewModel.connectWebSocket()
    }

    if (permissionGranted.value) {
        LaunchedEffect(Unit) {
            scope.launch(Dispatchers.IO) {
                currentLocation.value = locationViewModel.fetchLocation()
            }
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        LocationPermissionRequest(
            requestPermissionLauncher = requestPermissionLauncher,
            fetchLocation = {
                scope.launch(Dispatchers.IO) {
                    locationViewModel.fetchLocation()
                }
            },
            updatePriority = { priority ->
                locationViewModel.updatePriority(priority)
                locationViewModel.createLocationRequest()
            },
            onPermissionGranted = { boolean ->
                permissionGranted.value = boolean
            },
            backgroundPermissionGranted = backgroundPermissionGranted,
        )

        Button(onClick = {
            if (backgroundPermissionGranted.value) {
                geofenceViewModel.addGeofence()
                geofenceViewModel.registerGeofence()
            }
        }) {
            Text(text = "ジオフェンスを追加")
        }

        ShowMap(
            modifier = modifier,
            locationData =
                LocationData(
                    currentLocation.value.latitude,
                    currentLocation.value.longitude,
                    0,
                ),
            geofenceList = geofenceList.toList(),
            permissionState = permissionGranted.value,
        )
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
fun isBackgroundLocationPermissionGranted(context: Context): Boolean {
    // 位置情報取得権限が常に許可されているかチェック
    return ContextCompat.checkSelfPermission(
        context,
        android.Manifest.permission.ACCESS_BACKGROUND_LOCATION,
    ) == PackageManager.PERMISSION_GRANTED
}

@Composable
fun ShowMap(
    modifier: Modifier,
    locationData: LocationData,
    geofenceList: List<Geofence>,
    permissionState: Boolean,
) {
    var mapProperties = MapProperties()
    val cameraPosition =
        rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(LatLng(locationData.latitude, locationData.longitude), 15f)
        }

    LaunchedEffect(locationData) {
        cameraPosition.position = CameraPosition.fromLatLngZoom(LatLng(locationData.latitude, locationData.longitude), 15f)
    }

    if (permissionState) {
        mapProperties =
            MapProperties(
                isMyLocationEnabled = true,
            )
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPosition,
        properties = mapProperties,
    ) {
        geofenceList.forEach { geofence ->
            Circle(
                center = LatLng(geofence.latitude, geofence.longitude),
                radius = geofence.radius.toDouble(),
                fillColor = Color(0, 255, 0, 40),
                strokeColor = Color.Red,
                strokeWidth = 2f,
            )
        }
    }
}
