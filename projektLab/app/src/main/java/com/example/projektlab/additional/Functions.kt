package com.example.projektlab.additional

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.drawable.toBitmap

@Composable
fun loadImageFromAssets(context: Context, fileName: String): ImageBitmap? {
    return try {
        val inputStream = context.assets.open(fileName)
        val drawable = Drawable.createFromStream(inputStream, null)
        drawable?.toBitmap()?.asImageBitmap()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

object GlobalVariables {
    var refreshImage by mutableStateOf(false)
    var trailId by mutableIntStateOf(0)
}