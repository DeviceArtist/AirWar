package org.gongclouds.airwar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Ctrl(
    modifier: Modifier = Modifier,
    progress: Float,
    score: Int,
    onUP: () -> Unit,
    onDOWN: () -> Unit,
    onLEFT: () -> Unit,
    onRIGHT: () -> Unit,
    onFire: () -> Unit,
) {
    Box(Modifier.fillMaxSize()) {
        Text(
            text = "SCORE:$score",
            fontSize = 20.sp,
            color = Color.Red,
            modifier = modifier
                .align(Alignment.TopStart)
                .offset(x = 10.dp, y = (10).dp),
        )
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxWidth().height(5.dp),
        )
    }
    Box(Modifier.fillMaxSize().alpha(0.5f)) {
        Button(
            onClick = {
                onLEFT()
            },
            modifier = modifier
                .align(Alignment.BottomStart)
                .width(50.dp)
                .offset(x = 20.dp, y = (-60).dp),
        ) {
            Text("")
        }

        Button(
            onClick = {
                onRIGHT()
            },
            modifier = modifier
                .align(Alignment.BottomStart)
                .width(50.dp)
                .offset(x = 140.dp, y = (-60).dp),
        ) {
            Text("")
        }

        Button(
            onClick = {
                onUP()
            },
            modifier = modifier
                .align(Alignment.BottomStart)
                .width(50.dp)
                .offset(x = 80.dp, y = (-100).dp),
        ) {
            Text("")
        }

        Button(
            onClick = {
                onDOWN()
            },
            modifier = modifier
                .align(Alignment.BottomStart)
                .width(50.dp)
                .offset(x = 80.dp, y = (-10).dp),
        ) {
            Text("")
        }

        Button(
            onClick = {
                onFire()
            },
            modifier = modifier
                .align(Alignment.BottomEnd)
                .width(50.dp)
                .offset(x = -20.dp, y = (-60).dp),
        ) {
            Text("")
        }
    }
}