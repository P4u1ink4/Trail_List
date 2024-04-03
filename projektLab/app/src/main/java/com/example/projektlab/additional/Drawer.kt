package com.example.projektlab.additional

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DrawerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.projektlab.ui.theme.h5
import kotlinx.coroutines.launch

@Composable
fun DrawerContent(navController: NavController, drawerState: DrawerState) {
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primary)
            .width(300.dp)
            .fillMaxHeight()
    ){
        DrawerHeader()
        DrawerItem(text = "Strona główna") {
            navController.navigate("trailList") {
                popUpTo(navController.graph.startDestinationId)
            }
            coroutineScope.launch { drawerState.close() }
        }
        DrawerItem(text = "Ustawienia") {
            navController.navigate("settings_screen") {
            }
            coroutineScope.launch { drawerState.close() }
        }
        DrawerItem(text = "O nas") {
            navController.navigate("about_us_screen") {
            }
            coroutineScope.launch { drawerState.close() }
        }
    }
}

@Composable
fun DrawerHeader() {
    Text(
        text = "Navigation Drawer",
        color = Color.Black,
        style = h5,
        modifier = Modifier.padding(8.dp)
    )
}

@Composable
fun DrawerItem(text: String, onClick: () -> Unit) {
    Text(
        text = text,
        color = Color.Black,
        modifier = Modifier
            .padding(16.dp)
            .clickable(onClick = onClick)
    )
}