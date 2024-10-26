package com.websarva.wings.android.myapplication

import android.service.notification.NotificationListenerService.RankingMap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fitbattleandroid.R
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun FireAnimeModule()  {

    Box(
        contentAlignment = Alignment.Center,
        modifier =
        Modifier
            .fillMaxSize()
            .background(Color.DarkGray),
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier =
            Modifier
                .fillMaxSize(),
        ) {
            val image = painterResource(R.drawable.pngtreeburning_fire_5637806)

            Column{
//                Image(
//                    painter = image,
//                    contentDescription = null,
//                    modifier = Modifier
//                        .size(100.dp)
//                )
                for(i in 0 until 10){
                    FireParticle(image)
                }
            }
        }
    }
}

@Composable
fun FireParticle(image: Painter) {

    val angle = Random.nextDouble(0.0,2*Math.PI)
    val speed = Random.nextFloat() * 5 + 2
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }
    var alpha by remember { mutableStateOf(1f) }

    LaunchedEffect(Unit) {
        while (alpha > 0f) {
            offsetX += Random.nextFloat() * 10 - 5
            offsetY += Random.nextFloat() * 10 - 5
            alpha -= 0.05f

            delay(50)
        }
    }

    Image(
        painter = image,
        contentDescription = null,
        modifier = Modifier
            .size(5.dp)
            .graphicsLayer(
                translationX = offsetX+100f,
                translationY = offsetY-200f,
                scaleX = 1f,
                scaleY = 1f,
                alpha = alpha
            )
    )
}


@Preview
@Composable
fun FireAnimeModulePreview()  {
    FireAnimeModule()
}
