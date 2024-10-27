package com.example.fitbattleandroid.ui.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fitbattleandroid.R
import com.example.fitbattleandroid.ui.common.ShowCurrentTimeAndRemainingTime
import com.example.fitbattleandroid.ui.theme.onPrimaryDark
import com.example.fitbattleandroid.ui.theme.primaryContainerDarkMediumContrast
import com.example.fitbattleandroid.ui.theme.primaryContainerLight

@Composable
fun FitnessMemory(modifier: Modifier) {
    var calories by remember { mutableStateOf(("250")) }
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

        Column(
            modifier =
            Modifier
                .fillMaxSize(),
            // 画面全体を使用
            verticalArrangement = Arrangement.Center, // 子要素を上から配置
            horizontalAlignment = Alignment.CenterHorizontally, // 子要素を左揃え
        ) {
            Text(
                text = "消費カロリー",
                fontSize = 22.sp, // 大きなフォントサイズ
                fontWeight = FontWeight.Bold,
                color = onPrimaryDark,
            )
            Text(
                text = "today", // ヘッダーのテキスト
                fontSize = 28.sp, // 大きなフォントサイズ
                fontWeight = FontWeight.Bold,
                color = onPrimaryDark,
                modifier =
                Modifier
                    .padding(top = 100.dp), // ヘッダーの上部に余白を追加
            )
            CaloriMeter(
                modifier = Modifier,
                max = 2000f,
                progress = calories.toFloat(),
                // .padding(90.dp)
            )

            ShowCurrentTimeAndRemainingTime(modifier)

            Text(
                modifier =
                Modifier
                    .padding(top = 10.dp)
                    .align(Alignment.CenterHorizontally),
                text = "$calories kcal",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = onPrimaryDark,
            )
        }
    }
}

@Composable
fun CaloriMeter(
    modifier: Modifier,
    max: Float,
    progress: Float,
) {
    val circleAngle = 360f
    val angle = 240f
    val progressWidth = 24.dp
    val backgroundWidth = 24.dp
    val startAngle = (circleAngle / 4) + ((circleAngle - angle) / 2)

    Box(
        modifier =
            Modifier
                .padding(top = 100.dp, bottom = 100.dp),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(
            modifier =
                Modifier
                    .size(250.dp)
                    .padding(10.dp),
            onDraw = {
                // 外枠を描画
                drawArc(
                    color = onPrimaryDark,
                    startAngle = startAngle,
                    sweepAngle = angle,
                    useCenter = false,
                    style =
                        Stroke(
                            width = backgroundWidth.toPx(),
                            cap = StrokeCap.Round,
                        ),
                    size = Size(size.width, size.height),
                    // topLeft = Offset(4f, 4f),// 少しずらして影にする
                )
                // 背景を描画
                drawArc(
                    brush =
                        Brush.radialGradient(
                            colors = listOf(Color(192, 192, 192), Color(127, 192, 188)),
                            center = Offset(size.width / 2, size.height / 2),
                            radius = size.width / 2,
                        ),
                    startAngle = startAngle,
                    sweepAngle = angle,
                    useCenter = false,
                    style = Stroke(width = backgroundWidth.toPx(), cap = StrokeCap.Round),
                    size = Size(size.width, size.height),
                )
                // 進捗を描画
                // 進捗部分にグラデーションをかけて立体的なエフェクト
                drawArc(
                    brush =
                        Brush.sweepGradient(
                            colors = listOf(Color(33, 108, 94), Color(80, 200, 180)),
                            center = Offset(size.width / 2, size.height / 2),
                        ),
                    startAngle = startAngle,
                    sweepAngle = angle / max * progress,
                    useCenter = false,
                    style = Stroke(width = progressWidth.toPx(), cap = StrokeCap.Round),
                    size = Size(size.width, size.height),
                )
//                    // 光沢のハイライトを追加して円の上部に光が当たっているように見せる
//                    drawArc(
//                        brush = Brush.linearGradient(
//                            colors = listOf(Color.White.copy(alpha = 0.3f), Color.Transparent),
//                            start = Offset(size.width / 2, 0f),
//                            end = Offset(size.width / 3, size.height / 2),
//                        ),
//                        startAngle = startAngle - 5, // ハイライトを調整
//                        sweepAngle = angle / 1,
//                        useCenter = false,
//                        style = Stroke(width = progressWidth.toPx() / 2, cap = StrokeCap.Round),
//                        size = Size(size.width, size.height),
//                    )
            },
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
//                FireAnimeModule()
            Image(
                painter = painterResource(id = R.drawable.pngtreeburning_fire_5637806),
                contentDescription = null,
                modifier = Modifier.size(80.dp),
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "0/${max.toInt()}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = onPrimaryDark,
                modifier = Modifier.padding(top = 8.dp), // 画像の下に少しスペースを追加
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun FitnessMemoryPreview() {
    FitnessMemory(modifier = Modifier)
}
