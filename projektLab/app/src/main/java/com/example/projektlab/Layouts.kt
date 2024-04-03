package com.example.projektlab

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.projektlab.data.Trail
import com.example.projektlab.screens.AboutUsScreen
import com.example.projektlab.screens.ListScreen
import com.example.projektlab.screens.SettingsScreen
import com.example.projektlab.screens.TrailDetailsScreen
import com.example.projektlab.ui.theme.ProjektLabTheme

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun PhoneLayout(trails: List<Trail>) {
    val navController = rememberNavController()

    ProjektLabTheme {
        NavHost(navController = navController, startDestination = "trailList") {
            composable("trailList") {
                ListScreen(navController, LocalContext.current, trails, 2)
            }
            composable("trailDetails/{trailId}") { backStackEntry ->
                val trailId = backStackEntry.arguments?.getString("trailId")?.toIntOrNull()
                if (trailId != null) {
                    TrailDetailsScreen(trailId, context = LocalContext.current, navController, trails)
                }
            }
            composable("settings_screen") {
                SettingsScreen(navController)
            }
            composable("about_us_screen") {
                AboutUsScreen(navController)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.R)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MiddleLayout(trails: List<Trail>) {
    val navController = rememberNavController()
    var selectedTrailId by remember { mutableStateOf<Int?>(null) }

    Row {
        Box(modifier = Modifier.weight(1f)) {
            ListScreen(navController, LocalContext.current, trails, 3)
            ProjektLabTheme {
                NavHost(navController = navController, startDestination = "trailList") {
                    composable("trailList") {
                    }
                    composable("trailDetails/{trailId}") { backStackEntry ->
                        val trailId = backStackEntry.arguments?.getString("trailId")?.toIntOrNull()
                        if (trailId != null) {
                            selectedTrailId = trailId
                        }
                        navController.navigate("trailList")
                    }
                    composable("settings_screen") {
                        SettingsScreen(navController)
                    }
                    composable("about_us_screen") {
                        AboutUsScreen(navController)
                    }
                }
            }
        }

        Box(modifier = Modifier.weight(1f)) {
            if (selectedTrailId != null) {
                TrailDetailsScreen(selectedTrailId!!, context = LocalContext.current, navController, trails)
            }
            else{
                TopAppBar(
                    title = { Text("") },
                    actions = {
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier
                        .height(75.dp)
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.R)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabletLayout(trails: List<Trail>) {
    val navController = rememberNavController()
    var selectedTrailId by remember { mutableStateOf<Int?>(null) }

    Row {
        Box(modifier = Modifier.weight(1f)) {
            ListScreen(navController, LocalContext.current, trails, 4)
            ProjektLabTheme {
                NavHost(navController = navController, startDestination = "trailList") {
                    composable("trailList") {
                    }
                    composable("trailDetails/{trailId}") { backStackEntry ->
                        val trailId = backStackEntry.arguments?.getString("trailId")?.toIntOrNull()
                        if (trailId != null) {
                            selectedTrailId = trailId
                        }
                        navController.navigate("trailList")
                    }
                    composable("settings_screen") {
                        SettingsScreen(navController)
                    }
                    composable("about_us_screen") {
                        AboutUsScreen(navController)
                    }
                }
            }
        }

        Box(modifier = Modifier.weight(1f)) {
            if (selectedTrailId != null) {
                TrailDetailsScreen(selectedTrailId!!, context = LocalContext.current, navController, trails)
            }
            else{
                TopAppBar(
                    title = { Text("") },
                    actions = {
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier
                        .height(75.dp)
                )
            }
        }
    }
}
