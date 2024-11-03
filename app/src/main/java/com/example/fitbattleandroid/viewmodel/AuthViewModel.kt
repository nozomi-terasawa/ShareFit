package com.example.fitbattleandroid.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.fitbattleandroid.extensions.isEmailValid
import com.example.fitbattleandroid.repositoryImpl.AuthRepositoryImpl

class AuthViewModel(
    private val authRepository: AuthRepositoryImpl,
) : ViewModel() {
    private var _registerState = mutableStateOf(RegisterState("", "", ""))
    val registerState: RegisterState
        get() = _registerState.value

    private var _loginState = mutableStateOf(LoginState("", ""))
    val loginState: LoginState
        get() = _loginState.value

    fun updateRegisterTextField(
        field: String,
        newText: String,
    ) {
        when (field) {
            "email" -> _registerState.value = _registerState.value.copy(email = newText)
            "password" -> _registerState.value = _registerState.value.copy(password = newText)
            "userName" -> _registerState.value = _registerState.value.copy(userName = newText)
        }
    }

    fun updateLoginTextField(
        field: String,
        newText: String,
    ) {
        when (field) {
            "email" -> _loginState.value = _loginState.value.copy(email = newText)
            "password" -> _loginState.value = _loginState.value.copy(password = newText)
        }
    }

    fun isEmailValid(email: String): Boolean = email.isEmailValid()

    suspend fun register() {
        authRepository.register(_registerState.value.email, _registerState.value.password)
    }

    suspend fun login(
        email: String,
        password: String,
    ) {
        authRepository.login(email, password)
    }
}

data class RegisterState(
    val email: String,
    val password: String,
    val userName: String,
)

data class LoginState(
    val email: String,
    val password: String,
)
