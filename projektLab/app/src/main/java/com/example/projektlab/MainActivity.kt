package com.example.projektlab

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalConfiguration
import com.example.projektlab.additional.AnimatedScene
import com.example.projektlab.data.Trail
import com.example.projektlab.data.TrailRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyScreen()
        }
    }
}


@SuppressLint("CoroutineCreationDuringComposition")
@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun MyScreen() {
    var trails by remember { mutableStateOf<List<Trail>>(emptyList()) }
    var trailsLoaded by remember { mutableStateOf(false) }
    var animationEnd by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    if (!trailsLoaded || !animationEnd) {
        coroutineScope.launch {
            delay(10000) // Wyświetl AnimatedScene przez 10 sekund
            animationEnd = true
        }
        AnimatedScene()
    }

//    uploadTrailsDataFromJson(LocalContext.current)

    LaunchedEffect(Unit) {
        TrailRepository.getTrails(
            onSuccess = { fetchedTrails ->
                trails = fetchedTrails
                println("Pomyślnie pobrano szlaki do stage: $trails")
                trailsLoaded = true
            },
            onError = { exception ->
                println("Błąd podczas pobierania szlaków: $exception")
                trailsLoaded = false
            }
        )
    }

    if(trailsLoaded && animationEnd){
        val configuration = LocalConfiguration.current
        if (configuration.screenWidthDp < 600) {
            PhoneLayout(trails)
        } else if (configuration.screenWidthDp < 1000) {
            MiddleLayout(trails)
        }
        else{
            TabletLayout(trails)
        }
    }
}
