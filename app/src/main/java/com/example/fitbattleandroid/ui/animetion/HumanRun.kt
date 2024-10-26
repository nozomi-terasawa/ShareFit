package com.websarva.wings.android.myapplication

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fitbattleandroid.R
import kotlinx.coroutines.delay

@Composable
fun AnimemotionModule()  {
    var alpha by remember { mutableStateOf(1f) }
    var offsetX by remember { mutableStateOf(300f) }
    var fadeOut by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(50)
            if (fadeOut) {
                alpha -= 0.04f
                offsetX -= 20f
                if (alpha <= 0f) {
                    alpha = 0f
                    fadeOut = false
                    offsetX = 350f
                }
            } else {
                alpha += 0.04f
                offsetX -= 20f
                if (alpha >= 1f) {
                    alpha = 1f
                    fadeOut = true
                }
            }
        }
    }

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
            val image = painterResource(R.drawable.pngtreerunning_character_silhouette_png_free_4627520)

            Image(
                painter = image,
                contentDescription = null,
                modifier = Modifier
                    .graphicsLayer {
                        this.alpha = alpha
                        translationX = offsetX
                    }
                    .size(100.dp)
            )
        }
    }
}

@Preview
@Composable
fun AnimeModulePreview()  {
    AnimemotionModule()
}
