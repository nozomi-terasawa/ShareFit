package com.example.fitbattleandroid.viewmodel

import androidx.lifecycle.ViewModel
import com.example.fitbattleandroid.extensions.isEmailValid
import com.example.fitbattleandroid.repositoryImpl.AuthRepositoryImpl

class AuthViewModel(
    private val authRepository: AuthRepositoryImpl

): ViewModel() {
    fun isEmailValid(email: String): Boolean {
        return email.isEmailValid()
    }

    suspend fun register(email: String, password: String) {
        authRepository.register(email, password)
    }

    suspend fun login(email: String, password: String) {
        authRepository.login(email, password)
    }
}