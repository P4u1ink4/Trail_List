package com.example.projektlab.screens

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.projektlab.data.Stage
import com.example.projektlab.data.Trail
import com.example.projektlab.ui.theme.h5
import androidx.compose.runtime.Composable
import androidx.compose.ui.draw.clip
import coil.compose.rememberImagePainter
import com.example.projektlab.additional.Speed
import com.example.projektlab.additional.SpeedSelection
import com.example.projektlab.additional.getImage
import com.example.projektlab.additional.imageCaptureFromCamera
import com.example.projektlab.additional.loadImageFromAssets

@RequiresApi(Build.VERSION_CODES.R)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrailDetailsScreen(trailId: Int, context: Context, navController: NavController, trails: List<Trail>) {
        val trail = remember(trailId) { getTrailDetails(trailId, trails) }
        var selectedSpeed by remember { mutableStateOf(Speed.NORMAL) }
        val scrollState = rememberScrollState()

        var showSpeedSelection by remember { mutableStateOf(false) }
        var showTimer by remember { mutableStateOf(false) }

        var capturedImageUri by remember { mutableStateOf<Uri?>(null) }

        LaunchedEffect(capturedImageUri) {
            getImage(trail) { uri ->
                capturedImageUri = uri
            }
        }

        LaunchedEffect(trail) {
            getImage(trail) { uri ->
                capturedImageUri = uri
            }
        }

        Column {
            TopAppBar(
                title = { Text("") },
                navigationIcon = {
                    if (LocalConfiguration.current.screenWidthDp < 600) {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                },
                actions = {
                    capturedImageUri?.let { uri ->
                        Image(
                            painter = rememberImagePainter(uri),
                            contentDescription = null,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(shape = RoundedCornerShape(8.dp))
                        )
                    }
                    IconButton(onClick = { showSpeedSelection = !showSpeedSelection }) {
                        val speedIcon = loadImageFromAssets(context, "icon_speed.png")
                        speedIcon?.let { icon ->
                            Icon(
                                painter = BitmapPainter(icon),
                                contentDescription = "Speed",
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }
                    IconButton(onClick = { showTimer = !showTimer }) {
                        val timerIcon = loadImageFromAssets(context, "icon_timer.png")
                        timerIcon?.let { icon ->
                            Icon(
                                painter = BitmapPainter(icon),
                                contentDescription = "Timer",
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier
                    .height(75.dp)
            )

            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .verticalScroll(scrollState)
                )
                {
                    TrailDetails(trail = trail, speed = selectedSpeed)
                    Spacer(modifier = Modifier.height(16.dp))
                    if (showSpeedSelection) {
                        Text(
                            text = "Choose your way of walking: ",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        SpeedSelection(selectedSpeed) { speed ->
                            selectedSpeed = speed
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    if (showTimer) {
                        TimerScreen()
                    }
                }
                imageCaptureFromCamera(context = context, trail = trail) { uri ->
                    capturedImageUri = uri
                }
            }
        }
}

@Composable
fun TrailDetails(trail: Trail, speed: Speed) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = trail.name,
            style = h5,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = trail.description,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        trail.stages.forEach { stage ->
            StageItem(stage = stage, speed = speed)
        }
    }
}

@Composable
fun StageItem(stage: Stage, speed: Speed) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text(
            text = stage.name,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = stage.description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        val estimatedTime = when(speed) {
            Speed.SLOW -> stage.estimatedTime * 4 / 3
            Speed.NORMAL -> stage.estimatedTime
            Speed.FAST -> stage.estimatedTime * 3 / 4
        }
        Text(
            text = "Estimated Time: $estimatedTime minutes",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

fun getTrailDetails(trailId: Int, trailList: List<Trail>): Trail {
    for (trail in trailList) {
        if (trail.id == trailId) {
            return trail
        }
    }
    throw IllegalArgumentException("Trail not found ${trailList}")
}