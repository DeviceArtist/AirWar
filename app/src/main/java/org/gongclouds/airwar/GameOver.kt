package org.gongclouds.airwar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GameOver(modifier: Modifier = Modifier, onClick:()->Unit){
    Column(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "Game Over",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Red
        )
        Button(onClick = {
            onClick()
        }) {
            Text("Restart")
        }
    }
}