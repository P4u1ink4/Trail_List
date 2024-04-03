package com.example.projektlab.additional

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.projektlab.R
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun AnimatedScene() {
    val isRaining = remember { mutableStateOf(false) }
    val cloudSize = 200.dp
    val cloudOffset1 = remember { mutableStateOf(Offset.Zero) }
    val cloudOffset2 = remember { mutableStateOf(Offset.Zero) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFADD8E6))
    ) {
        Image(
            painter = painterResource(id = if (isRaining.value) R.drawable.cloud2 else R.drawable.cloud1),
            contentDescription = "Cloud",
            modifier = Modifier
                .size(cloudSize)
                .align(Alignment.TopStart)
                .clickable { isRaining.value = !isRaining.value }
                .onGloballyPositioned { coordinates ->
                    cloudOffset1.value = coordinates.positionInRoot()
                }
        )
        Image(
            painter = painterResource(id = if (isRaining.value) R.drawable.cloud2 else R.drawable.cloud1),
            contentDescription = "Cloud",
            modifier = Modifier
                .size(cloudSize)
                .align(Alignment.CenterEnd)
                .clickable { isRaining.value = !isRaining.value }
                .onGloballyPositioned { coordinates ->
                    cloudOffset2.value = coordinates.positionInRoot()
                }
        )

        if (isRaining.value) {
            for(i in 0 until 10) {
                RainAnimation(cloudSize, cloudOffset1.value)
                RainAnimation(cloudSize, cloudOffset2.value)
            }
        }
    }
}

@Composable
fun RainAnimation(cloudSize: Dp, cloudOffset: Offset) {
    val raindropCount = 20
    val raindropOffsets = remember { List(raindropCount) { Animatable(0f) } }
    val coroutineScope = rememberCoroutineScope()

    Canvas(modifier = Modifier.fillMaxSize()) {
        val raindropWidth = 2.dp.toPx()
        val raindropHeight = 7.dp.toPx()

        for (i in 0 until raindropCount) {
            val duration = 100 + Random.nextInt(30000)
            val raindropOffset = raindropOffsets[i]

            coroutineScope.launch {
                raindropOffset.animateTo(
                    targetValue = 1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(duration, easing = LinearEasing),
                        repeatMode = RepeatMode.Restart
                    )
                )
            }

            val x = cloudOffset.x + 30 + i * (cloudSize.toPx() - 70) / raindropCount
            val y = cloudOffset.y + 400 + raindropOffset.value * size.height
            drawRect(
                color = Color.Blue,
                topLeft = Offset(x, y),
                size = Size(raindropWidth, raindropHeight)
            )
        }
    }

}
