package com.example.fitbattleandroid.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class EntryGeoFenceReq(
    val userId: Int,
    val geoFenceId: Int,
    val entryTime: String,
)
