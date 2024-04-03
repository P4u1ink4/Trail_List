package com.example.projektlab.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.projektlab.ui.theme.h5

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainCardTopBar(onMenuClick: () -> Unit) {
    TopAppBar(
        title = { Text("") },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(Icons.Default.Menu, contentDescription = "Open navigation drawer")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        modifier = Modifier
            .height(75.dp)
    )
}

@Composable
fun MainCard() {
    Column {
        Text(
            text = "Witaj w aplikacji z trasami!",
            style = h5,
            modifier = Modifier.padding(16.dp)
        )
        Text(
            text = "Aplikacja Trasy oferuje możliwość przeglądania różnych tras turystycznych. Znajdziesz tu zarówno krótkie spacerowe szlaki, jak i długie górskie trasy. Wybierz trasę, która Cię interesuje, i rozpocznij swoją przygodę!",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(16.dp),
        )
    }
}
