package com.websarva.wings.android.myapplication

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import com.example.fitbattleandroid.ui.common.Background
import com.example.fitbattleandroid.ui.common.Header
import com.example.fitbattleandroid.ui.common.NormalBottom
import com.example.fitbattleandroid.ui.common.NormalText
import com.example.fitbattleandroid.ui.theme.onPrimaryDark
import com.example.fitbattleandroid.ui.theme.primaryContainerDarkMediumContrast
import com.example.fitbattleandroid.ui.theme.primaryContainerLight

@Composable
fun TopScreen(navController: NavController) {
    Background {
        Header {
            Image(
                painter = painterResource(R.drawable.title),
                contentDescription = null,
                modifier = Modifier.size(500.dp),
            )

            Column {

                NormalBottom({navController.navigate("login")}) {
                    NormalText("ログイン")
                }

                Spacer(modifier = Modifier.size(20.dp))

                NormalBottom ({ navController.navigate("regi") }){
                    NormalText("登録する")
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
