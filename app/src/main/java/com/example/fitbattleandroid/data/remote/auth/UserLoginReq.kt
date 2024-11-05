package com.example.fitbattleandroid.data.remote.auth

import kotlinx.serialization.Serializable

@Serializable
data class UserLoginReq(
    val email: String,
    val password: String,
)
