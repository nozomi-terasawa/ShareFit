package com.example.fitbattleandroid.repository

import com.example.fitbattleandroid.data.remote.auth.UserCreateReq
import com.example.fitbattleandroid.data.remote.auth.UserCreateRes

interface AuthRepository {
    suspend fun register(userCreateReq: UserCreateReq): UserCreateRes

    suspend fun login(
        email: String,
        password: String,
    )
}
