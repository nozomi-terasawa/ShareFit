package com.websarva.wings.android.myapplication

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.fitbattleandroid.R
import com.example.fitbattleandroid.ui.theme.onPrimaryDark
import com.example.fitbattleandroid.ui.theme.primaryContainerDarkMediumContrast
import com.example.fitbattleandroid.ui.theme.primaryContainerLight

@Composable
fun TopScreen(navController: NavController) {
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
        ) {

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

            val image = painterResource(R.drawable.title)
            Image(
                painter = image,
                contentDescription = null,
                modifier = Modifier.size(500.dp),
            )

            Column {
                Button(
                    onClick = {
                        navController.navigate("login")
                    },
                    shape = RoundedCornerShape(20.dp),
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor = primaryContainerDarkMediumContrast,
                        ),
                    modifier =
                        Modifier
                            .width(200.dp)
                            .padding(bottom = 20.dp),
                ) {
                    Text(
                        text = "ログイン",
                        color = onPrimaryDark,
                    )
                }
                Button(
                    onClick = {
                        navController.navigate("regi")
                    },
                    shape = RoundedCornerShape(20.dp),
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor = primaryContainerDarkMediumContrast,
                        ),
                    modifier =
                        Modifier
                            .width(200.dp),
                ) {
                    Text(
                        text = "登録する",
                        color = onPrimaryDark,
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun TopScreenPreview() {
    TopScreen(navController = rememberNavController())
}
