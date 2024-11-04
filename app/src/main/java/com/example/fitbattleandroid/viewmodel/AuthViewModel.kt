package com.example.fitbattleandroid.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitbattleandroid.data.datastore.DataStoreManager
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
            try {
                val result = authRepository.register(userCreateReq)
                AuthState.Success(
                    token = result.token,
                    userId = result.userId,
                )
            } catch (e: Exception) {
                AuthState.Error
                Log.d("result", e.toString())
            }
        }
    }

    fun login(userLoginReq: UserLoginReq) {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            try {
                val result = authRepository.login(userLoginReq)
                _authState.value =
                    AuthState.Success(
                        token = result.token,
                        userId = result.userId,
                    )
            } catch (e: Exception) {
                Log.d("result", e.toString())

                _authState.value = AuthState.Error
            }
        }
    }

    // トークンの保存
    fun saveAuthToken(
        context: Context,
        token: String,
    ) {
        viewModelScope.launch {
            DataStoreManager.saveAuthToken(context, token)
        }
    }

    // トークンを取得
    fun getAuthToken(context: Context) {
        viewModelScope.launch {
            DataStoreManager.getAuthToken(context)
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

    data class Success(
        val token: String,
        val userId: Int,
    ) : AuthState

    data object Error : AuthState
}
