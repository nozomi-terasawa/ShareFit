package com.example.fitbattleandroid.viewmodel

import androidx.lifecycle.ViewModel
import com.example.fitbattleandroid.data.remote.EntryGeoFenceReq
import com.example.fitbattleandroid.data.remote.EntryGeoFenceRes
import com.example.fitbattleandroid.repositoryImpl.GeofenceEntryRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HealthDataApiViewModel(
    private val repository: GeofenceEntryRepositoryImpl,
) : ViewModel() {
    private val _geofenceEntryState = MutableStateFlow<GeofenceEntryState>(GeofenceEntryState.Loading)
    val geofenceEntryState: StateFlow<GeofenceEntryState> = _geofenceEntryState.asStateFlow()

    // ジオフェンス入室リクエスト関数
    suspend fun sendGeoFenceEntryRequest(entry: EntryGeoFenceReq) {
        try {
            val res = repository.sendGeofenceEntryRequest(entry)
            _geofenceEntryState.value = GeofenceEntryState.Success(res)
        } catch (e: Exception) {
            _geofenceEntryState.value = GeofenceEntryState.Error
        }
    }
}

sealed interface GeofenceEntryState {
    data object Loading : GeofenceEntryState

    data class Success(
        val entryGeoFenceRes: EntryGeoFenceRes,
    ) : GeofenceEntryState

    data object Error : GeofenceEntryState
}
