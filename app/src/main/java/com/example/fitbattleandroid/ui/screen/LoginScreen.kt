package com.example.fitbattleandroid.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.fitbattleandroid.repositoryImpl.AuthRepositoryImpl
import com.example.fitbattleandroid.ui.common.Background
import com.example.fitbattleandroid.ui.common.Body
import com.example.fitbattleandroid.ui.common.CommonOutlinedTextField
import com.example.fitbattleandroid.ui.common.Header
import com.example.fitbattleandroid.ui.common.MinText
import com.example.fitbattleandroid.ui.common.NormalBottom
import com.example.fitbattleandroid.ui.common.NormalText
import com.example.fitbattleandroid.ui.common.TitleText
import com.example.fitbattleandroid.ui.common.TransparentBottom
import com.example.fitbattleandroid.ui.navigation.Screen
import com.example.fitbattleandroid.viewmodel.AuthState
import com.example.fitbattleandroid.viewmodel.AuthViewModel
import com.example.fitbattleandroid.viewmodel.toUserLoginReq

@Composable
fun LoginScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    authState: AuthState,
) {
    val loginState = authViewModel.loginState

    Background {
        Header {
            Body {
                TitleText("ログイン")

                Spacer(modifier = Modifier.size(50.dp))

                CommonOutlinedTextField(
                    value = loginState.email,
                    label = "メールアドレス",
                    onValueChange = { newValue ->
                        authViewModel.updateLoginTextField("email", newValue)
                    },
                )

                CommonOutlinedTextField(
                    value = loginState.password,
                    label = "パスワード",
                    onValueChange = { newValue ->
                        authViewModel.updateLoginTextField("password", newValue)
                    },
                )

                NormalBottom(
                    onClick = {
                        authViewModel.login(authViewModel.loginState.toUserLoginReq())
                        when (authState) {
                            is AuthState.Loading -> { }
                            is AuthState.Success -> {
                                Log.d("result", authState.token)
                                navController.navigate("main")
                            }
                            is AuthState.Error -> {
                            }
                            else -> {}
                        }
                    },
                ) {
                    NormalText("ログイン")
                }

                TransparentBottom(
                    {
                        navController.navigate(Screen.Regi.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                ) {
                    MinText("新規登録の方はこちら")
                }
            }
        }
    }
}

@Composable
@Preview
fun LoginScreenPreview() {
    LoginScreen(
        navController = rememberNavController(),
        authViewModel = AuthViewModel(AuthRepositoryImpl()),
        authState = AuthState.Loading,
    )
}
