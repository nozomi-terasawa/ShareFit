package com.example.fitbattleandroid.data.remote.auth

import kotlinx.serialization.Serializable

@Serializable
data class UserCreateReq(
    val name: String,
    val email: String,
    val password: String,
)
