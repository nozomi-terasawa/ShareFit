package com.example.fitbattleandroid.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitbattleandroid.data.remote.auth.UserCreateReq
import com.example.fitbattleandroid.data.remote.auth.UserLoginReq
import com.example.fitbattleandroid.extensions.isEmailValid
import com.example.fitbattleandroid.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {
    private var _registerState = mutableStateOf(RegisterState("", "", ""))
    val registerState: RegisterState
        get() = _registerState.value

    private var _loginState = mutableStateOf(LoginState("", ""))
    val loginState: LoginState
        get() = _loginState.value

    private var _authState = MutableStateFlow<AuthState>(AuthState.Initial)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

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

    fun register(userCreateReq: UserCreateReq) {
        viewModelScope.launch {
            authRepository.register(userCreateReq)
        }
    }

    fun login(userLoginReq: UserLoginReq) {
        _authState.value = AuthState.Loading
        try {
            viewModelScope.launch {
                authRepository.login(userLoginReq)
                _authState.value = AuthState.Success
            }
        } catch (e: Exception) {
            _authState.value = AuthState.Error
        }
    }
}

data class RegisterState(
    val email: String,
    val password: String,
    val userName: String,
)

fun RegisterState.toUserCreateReq(): UserCreateReq =
    UserCreateReq(
        email = email,
        password = password,
        name = userName,
    )

data class LoginState(
    val email: String,
    val password: String,
)

fun LoginState.toUserLoginReq(): UserLoginReq =
    UserLoginReq(
        email = email,
        password = password,
    )

sealed interface AuthState {
    data object Initial : AuthState

    data object Loading : AuthState

    data object Success : AuthState

    data object Error : AuthState
}
