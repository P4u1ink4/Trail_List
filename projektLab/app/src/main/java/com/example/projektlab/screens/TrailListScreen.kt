package com.example.projektlab.screens

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.projektlab.additional.GlobalVariables
import com.example.projektlab.additional.getImage
import com.example.projektlab.data.Trail

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrailListTopBar(searchText: String, onSearchTextChanged: (String) -> Unit, onMenuClick: () -> Unit) {
    TopAppBar(
        title = { Text("") },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(Icons.Default.Menu, contentDescription = "Open navigation drawer")
            }
        },
        actions = {
            SearchTextField(searchText, onSearchTextChanged)
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        modifier = Modifier
            .height(75.dp)
    )
}

@Composable
fun TrailListScreen(navController: NavController, context: Context, searchText: String, isShortRoute: Boolean, trails: List<Trail>, gridcells: Int) {
    var selectedTrails = trails.filter { trail ->
        trail.name.contains(searchText, ignoreCase = true) || trail.description.contains(searchText, ignoreCase = true)
    }.filter { trail ->
        if (isShortRoute) {
            trail.stages.sumBy { it.estimatedTime } < 180
        } else {
            trail.stages.sumBy { it.estimatedTime } >= 180
        }
    }

    Column {
        TrailList(trails = selectedTrails, navController = navController, gridcells)
    }
}

@Composable
fun SearchTextField(searchText: String, onSearchTextChanged: (String) -> Unit) {
    OutlinedTextField(
        value = searchText,
        onValueChange = onSearchTextChanged,
        label = { Text("Wyszukaj szlak", color = Color.White) },
        modifier = Modifier
            .padding(end = 8.dp, top = 8.dp, bottom = 8.dp)
            .height(60.dp),
        textStyle = MaterialTheme.typography.bodySmall
    )
}

@Composable
fun TrailList(trails: List<Trail>, navController: NavController, gridcells: Int) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(gridcells),
        modifier = Modifier.padding(8.dp)
    ) {
        items(trails.size) { index ->
            val trail = trails[index]
            var capturedImageUri by remember { mutableStateOf<Uri?>(null) }

            LaunchedEffect(trail) {
                getImage(trail) { uri ->
                    capturedImageUri = uri
                }
            }

            LaunchedEffect(trails) {
                getImage(trail) { uri ->
                    capturedImageUri = uri
                }
            }

            if (GlobalVariables.refreshImage && GlobalVariables.trailId == trail.id) {
                getImage(trail) { uri ->
                    capturedImageUri = uri
                    GlobalVariables.refreshImage = false
                    GlobalVariables.trailId = 0
                }
            }

            Card(
                modifier = Modifier
                    .padding(4.dp)
                    .height(200.dp)
                    .clickable {
                        navController.navigate("trailDetails/${trail.id}")
                    },
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Color.Gray)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center) {
                    capturedImageUri?.let { uri ->
                        Image(
                            painter = rememberImagePainter(uri),
                            contentDescription = null,
                            modifier = Modifier
                                .size(120.dp)
                                .clip(shape = RoundedCornerShape(8.dp))
                        )
                    }
                    Text(
                        text = trail.name,
                        modifier = Modifier.padding(16.dp),
                        color = Color.Black
                    )
                }
            }
        }
    }
}