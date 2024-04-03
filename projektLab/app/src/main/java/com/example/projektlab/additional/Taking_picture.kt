package com.example.projektlab.additional

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.projektlab.data.Trail
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects


@Composable
fun imageCaptureFromCamera(context: Context, trail: Trail, onImageCaptured: (Uri) -> Unit) {
    val file = context.createImageFile(trail)

    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        context.packageName + ".provider", file
    )

    var capturedImageUri by remember {
        mutableStateOf<Uri>(Uri.EMPTY)
    }


    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()){ success ->
            capturedImageUri = uri
            if(success){
                updateTrailImage(trail, capturedImageUri) { uri ->
                    onImageCaptured(uri)
                    file.delete()
                    GlobalVariables.refreshImage = true
                    GlobalVariables.trailId = trail.id
                }
            }
        }


    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    Box(
        Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        FloatingActionButton(
            onClick = {
                val permissionCheckResult =
                    ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)

                if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                    cameraLauncher.launch(uri)
                } else {
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }
            },
            modifier = Modifier
                .padding(16.dp)
                .size(56.dp)
                .align(Alignment.BottomEnd)
        ) {
            Icon(Icons.Filled.Face, contentDescription = "Take a selfie")
        }
    }
}


fun Context.createImageFile(trail: Trail): File {
    val timeStamp = SimpleDateFormat("yyyy_MM_dd_HH:mm:ss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val image = File.createTempFile(
        imageFileName,
        ".jpg",
        externalCacheDir
    )
    return image
}

fun updateTrailImage(trail: Trail, capturedImageUri: Uri, onImageCaptured: (Uri) -> Unit) {
    var filename = "selfie${trail.id}.jpg"

    val storage = Firebase.storage
    val storageRef = storage.reference

    var file = capturedImageUri
    val trailRef = storageRef.child("selfies/${filename}")
    val uploadTask = trailRef.putFile(file)
    uploadTask.addOnSuccessListener { _ ->
        getImage(trail) { uri ->
            onImageCaptured(uri)
        }
    }.addOnFailureListener { exception ->
        Log.e(ContentValues.TAG, "Failed to upload image", exception)
    }
}

fun getImage(trail: Trail, onImageLoaded: (Uri) -> Unit) {
    val filename = "selfie${trail.id}.jpg"

    val storage = Firebase.storage
    val storageRef = storage.reference

    val trailRef = storageRef.child("selfies/$filename")

    trailRef.downloadUrl.addOnSuccessListener { uri ->
        onImageLoaded(uri)
    }.addOnFailureListener { exception ->
        Log.e(ContentValues.TAG, "getImage: Failed to download image", exception)
    }
}