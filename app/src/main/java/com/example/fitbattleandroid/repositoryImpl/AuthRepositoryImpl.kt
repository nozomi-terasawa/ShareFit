package com.example.fitbattleandroid.repositoryImpl

import com.example.fitbattleandroid.repository.AuthRepository

class AuthRepositoryImpl : AuthRepository {
    override suspend fun register(
        email: String,
        password: String,
    ) {
    }

    override suspend fun login(
        email: String,
        password: String,
    ) {
    }
}
