package com.example.fitbattleandroid.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.fitbattleandroid.ui.common.CommonOutlinedTextField
import com.example.fitbattleandroid.ui.navigation.Screen
import com.example.fitbattleandroid.ui.theme.onPrimaryDark
import com.example.fitbattleandroid.ui.theme.primaryContainerDarkMediumContrast
import com.example.fitbattleandroid.ui.theme.primaryContainerLight

@Composable
fun RegistrationScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    Box(
        contentAlignment = Alignment.Center,
        modifier =
            Modifier
                .fillMaxSize()
                .background(primaryContainerLight)
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier =
            Modifier
                .fillMaxSize()
                .imePadding()
        ){

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(primaryContainerDarkMediumContrast)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Share Fit",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = onPrimaryDark,
                    )
                )
            }

            Column(
                modifier =
                    modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .imePadding(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    "新規登録",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = modifier.padding(bottom = 60.dp),
                    color = onPrimaryDark,
                )

                CommonOutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = "メールアドレス",
                )

                CommonOutlinedTextField(
                    value = password,
                    label = "パスワード",
                    onValueChange = { password = it },
                )
                Button(
                    onClick = {
                        navController.navigate("main")
                    },
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor = primaryContainerDarkMediumContrast,
                        ),
                    modifier =
                    modifier
                        .width(200.dp),
                    // .border(BorderStroke(1.dp, Color.Blue),
                ) {
                    Text("新規登録", color = Color.White)
                }
                Button(
                    onClick = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Regi.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    shape = RoundedCornerShape(20.dp),
                    colors =
                    ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                    ),
                ) {
                    Text(
                        text = "登録済みの方はこちら",
                        color = onPrimaryDark,
                        fontSize = 10.sp,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun RegistrationScreenPreview(modifier: Modifier = Modifier) {
    RegistrationScreen(navController = rememberNavController(), modifier = modifier)
}
