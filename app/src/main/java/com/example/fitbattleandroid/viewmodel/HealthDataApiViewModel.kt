package com.example.fitbattleandroid.viewmodel

import androidx.lifecycle.ViewModel
import com.example.fitbattleandroid.data.remote.EntryGeoFenceReq
import com.example.fitbattleandroid.data.remote.EntryGeoFenceRes
import com.example.fitbattleandroid.data.remote.MemberInfo
import com.example.fitbattleandroid.repositoryImpl.GeofenceEntryRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HealthDataApiViewModel(
    private val repository: GeofenceEntryRepositoryImpl,
) : ViewModel() {
    private val _encounterMembers = MutableStateFlow<List<MemberInfo>>(emptyList())
    val encounterMembers: StateFlow<List<MemberInfo>> = _encounterMembers.asStateFlow()

    // ジオフェンス入室リクエスト関数
    suspend fun sendGeoFenceEntryRequest(entry: EntryGeoFenceReq): EntryGeoFenceRes {
        val res = repository.sendGeofenceEntryRequest(entry)
        for (member in res.passingMember) {
            _encounterMembers.value += member
        }
        return res
    }
}
