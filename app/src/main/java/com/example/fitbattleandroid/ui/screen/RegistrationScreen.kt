package com.example.fitbattleandroid.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.fitbattleandroid.ui.common.CommonOutlinedTextField

@Composable
fun RegistrationScreen(navController: NavController, modifier: Modifier = Modifier) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    Box(
        contentAlignment = Alignment.Center,
        modifier =
        Modifier
            .fillMaxSize()
            .background(Color.DarkGray),
    ) {
        Column(
            modifier =
            Modifier
                .clip(RoundedCornerShape(16.dp)) // 角を丸くする
                .background(Color.Black.copy(alpha = 0.4f)) // 丸みを反映した後に再度背景色を設定
                .padding(200.dp),
        ) {
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
                color = Color.White,
            )

            CommonOutlinedTextField (
                value = email,
                onValueChange = { email = it },
                label = "メールアドレス",
            )

            CommonOutlinedTextField(
                value = password,
                label = "password" ,
                onValueChange = { password = it},
            )
            Button(
                onClick = {
                    navController.navigate("main")
                },
                colors =
                ButtonDefaults.buttonColors(
                    containerColor = Color.Gray,
                ),
                modifier =
                modifier
                    .width(200.dp),
                // .border(BorderStroke(1.dp, Color.Blue),
            ) {
                Text("新規登録", color = Color.White)
            }
        }
    }
}

@Preview
@Composable
fun RegistrationScreenPreview(modifier: Modifier = Modifier) {
    RegistrationScreen(navController = rememberNavController(), modifier = modifier)
}