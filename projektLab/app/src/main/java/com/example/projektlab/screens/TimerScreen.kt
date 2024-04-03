package com.example.projektlab.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class TimeEntry(val time: Int, val timestamp: Long)

@Composable
fun TimerScreen() {
    var time by remember { mutableStateOf(0) }
    var timerJob: Job? by remember { mutableStateOf(null) }
    var isRunning by remember { mutableStateOf(false) }
    var times by remember { mutableStateOf(listOf<TimeEntry>()) }
    var showTimes by remember { mutableStateOf(false) }
    val db = Firebase.firestore

//    fun clearDatabase() {
//        val db = Firebase.firestore
//
//        db.collection("times")
//            .get()
//            .addOnSuccessListener { result ->
//                for (document in result) {
//                    db.collection("times").document(document.id).delete()
//                        .addOnSuccessListener {
//                            println("Document successfully deleted!")
//                        }
//                        .addOnFailureListener { e ->
//                            println("Error deleting document: $e")
//                        }
//                }
//            }
//            .addOnFailureListener { exception ->
//                println("Error getting documents: $exception")
//            }
//    }

    fun saveTimeToDatabase(time: Int) {
        val timestamp = System.currentTimeMillis()
        val docData = hashMapOf(
            "time" to time,
            "timestamp" to timestamp
        )

        db.collection("times")
            .add(docData)
            .addOnSuccessListener { documentReference ->
                println("DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                println("Error adding document: $e")
            }
    }

    fun getAllTimes() {
        db.collection("times")
            .get()
            .addOnSuccessListener { result ->
                times = result.documents.mapNotNull { document ->
                    val time = document.getLong("time")?.toInt()
                    val timestamp = document.getLong("timestamp")
                    if (time != null && timestamp != null) {
                        TimeEntry(time, timestamp)
                    } else {
                        null
                    }
                }
            }
            .addOnFailureListener { exception ->
                println("Error getting documents: $exception")
            }
    }

    Column {
        val hours = time / 3600
        val minutes = (time % 3600) / 60
        val seconds = time % 60
        Text(text = String.format("%02d:%02d:%02d", hours, minutes, seconds))

        Row {
            IconButton(onClick = {
                if (isRunning) {
                    timerJob?.cancel()
                } else {
                    timerJob = startTimer(time) { newTime ->
                        time = newTime
                    }
                }
                isRunning = !isRunning
            }) {
                Icon(if (isRunning) Icons.Filled.Lock else Icons.Filled.PlayArrow, contentDescription = if (isRunning) "Stop" else "Start")
            }

            IconButton(onClick = {
                timerJob?.cancel()
                time = 0
                isRunning = false
            }) {
                Icon(Icons.Filled.Refresh, contentDescription = "Reset")
            }

            IconButton(onClick = {
                saveTimeToDatabase(time)
                getAllTimes()
            }) {
                Icon(Icons.Filled.Check, contentDescription = "Zapisz")
            }

            IconButton(onClick = {
                if (showTimes) {
                    showTimes = false
                } else {
                    getAllTimes()
                    showTimes = true
                }
            }) {
                Icon(Icons.Filled.List, contentDescription = "Pokaż wszystkie")
            }
        }

        if (showTimes) {
            val sortedTimes = times.sortedByDescending { it.timestamp }
            sortedTimes.forEach { savedTime ->
                val savedHours = savedTime.time / 3600
                val savedMinutes = (savedTime.time % 3600) / 60
                val savedSeconds = savedTime.time % 60
                val formattedTimestamp = formatTimestamp(savedTime.timestamp)
                Text(text = String.format("%02d:%02d:%02d - Timestamp: %s", savedHours, savedMinutes, savedSeconds, formattedTimestamp))
            }
        }
    }
}

fun startTimer(startTime: Int, onTimeChanged: (Int) -> Unit): Job {
    return CoroutineScope(Dispatchers.Main).launch {
        var time = startTime
        while (true) {
            onTimeChanged(time)
            time++
            delay(1000L)
        }
    }
}

fun formatTimestamp(timestamp: Long): String {
    val date = Date(timestamp)
    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    return formatter.format(date)
}