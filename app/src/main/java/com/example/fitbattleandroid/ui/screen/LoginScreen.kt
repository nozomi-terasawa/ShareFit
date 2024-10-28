package com.example.fitbattleandroid.ui.screen

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
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

@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Background {
        Header {
            Body {
                TitleText("ログイン")

                Spacer(modifier = Modifier.size(50.dp))

                CommonOutlinedTextField(
                    value = email,
                    label = "メールアドレス",
                    onValueChange = { email = it },
                )

                CommonOutlinedTextField(
                    value = password,
                    label = "パスワード",
                    onValueChange = { password = it },
                )

                NormalBottom({ navController.navigate("main") }) {
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
    LoginScreen(navController = rememberNavController())
}
