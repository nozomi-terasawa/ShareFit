package com.example.fitbattleandroid.repository

interface AuthRepository {
    suspend fun register(
        email: String,
        password: String,
    )

    suspend fun login(
        email: String,
        password: String,
    )
}
