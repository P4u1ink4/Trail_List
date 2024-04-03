package com.example.projektlab.additional

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

enum class Speed {
    SLOW,
    NORMAL,
    FAST
}

@Composable
fun SpeedSelection(selectedSpeed: Speed, onSpeedSelected: (Speed) -> Unit) {
    val speeds = Speed.values()

    Row {
        speeds.forEach { speed ->
            Text(
                text = speed.name,
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .clickable { onSpeedSelected(speed) },
                style = MaterialTheme.typography.bodyMedium,
                color = if (speed == selectedSpeed) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.54f)
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}