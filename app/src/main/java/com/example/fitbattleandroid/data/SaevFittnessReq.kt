package com.example.fitbattleandroid.data

import kotlinx.serialization.Serializable

@Serializable
data class SaveFitnessReq(
    val userId: Int,
    val calories: Float,
    val timestamp: String,
)

@Serializable
data class SaveFitnessRes(
    val success: Boolean,
)
