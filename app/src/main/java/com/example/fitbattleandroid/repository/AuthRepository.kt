package com.example.fitbattleandroid.repository

import com.example.fitbattleandroid.data.remote.auth.UserCreateReq
import com.example.fitbattleandroid.data.remote.auth.UserCreateRes
import com.example.fitbattleandroid.data.remote.auth.UserLoginReq
import com.example.fitbattleandroid.data.remote.auth.UserLoginRes

interface AuthRepository {
    suspend fun register(userCreateReq: UserCreateReq): UserCreateRes

    suspend fun login(userLoginReq: UserLoginReq): UserLoginRes
}
