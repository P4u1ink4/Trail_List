package com.example.projektlab.additional

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.example.projektlab.data.Trail
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.json.JSONArray

// KOD UŻYTY JEDNORAZOWO DO ŁATWIEJSZEGO PRZESŁANIA DANYCH DO FIRESTORE

// Inicjalizacja bazy danych Firestore
val db = Firebase.firestore

// Funkcja do wczytania danych z pliku JSON i wstawienia ich do Firestore
fun uploadTrailsDataFromJson(context: Context) {
    val inputStream = context.assets.open("trails.json")
    val jsonString = inputStream.bufferedReader().use { it.readText() }
    val jsonArray = JSONArray(jsonString)
    for (i in 0 until jsonArray.length()) {
        val jsonTrail = jsonArray.getJSONObject(i)
        val id = jsonTrail.getInt("id")
        val name = jsonTrail.getString("name")
        val description = jsonTrail.getString("description")
        val image = jsonTrail.getString("image")

        // Pobranie danych etapów (stages) jako tablicy JSON
        val stagesArray = jsonTrail.getJSONArray("stages")
        val stagesList = mutableListOf<Map<String, Any>>()

        // Iteracja przez etapy i dodanie ich do listy
        for (j in 0 until stagesArray.length()) {
            val jsonStage = stagesArray.getJSONObject(j)
            val stageId = jsonStage.getInt("id")
            val stageName = jsonStage.getString("name")
            val stageDescription = jsonStage.getString("description")
            val estimatedTime = jsonStage.getInt("estimatedTime")

            // Dodanie danych etapu do mapy
            val stageData = mapOf(
                "id" to stageId,
                "name" to stageName,
                "description" to stageDescription,
                "estimatedTime" to estimatedTime
            )

            // Dodanie mapy danych etapu do listy etapów
            stagesList.add(stageData)
        }

        // Utworzenie danych szlaku (trails) zawierających listę etapów
        val trailData = hashMapOf(
            "id" to id,
            "name" to name,
            "description" to description,
            "image" to image,
            "stages" to stagesList
        )

        // Wstawienie danych szlaku do Firestore
        db.collection("trails")
            .add(trailData)
            .addOnSuccessListener { documentReference ->
                println("DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                println("Error adding document: $e")
            }
    }
}


fun clearDatabase() {
        val db = Firebase.firestore

        db.collection("trails")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    db.collection("trails").document(document.id).delete()
                        .addOnSuccessListener {
                            println("Document successfully deleted!")
                        }
                        .addOnFailureListener { e ->
                            println("Error deleting document: $e")
                        }
                }
            }
            .addOnFailureListener { exception ->
                println("Error getting documents: $exception")
            }
    }

fun clearDatabaseElement() {
    val db = Firebase.firestore

    db.collection("trails")
        .get()
        .addOnSuccessListener { result ->
            for (document in result) {
                val trailFromDb = document.toObject(Trail::class.java)
                Log.d(ContentValues.TAG,"document: ${trailFromDb.id}")
                if (trailFromDb.id == 0) {
                    db.collection("trails").document(document.id).delete()
                        .addOnSuccessListener {
                            Log.d(ContentValues.TAG, "DocumentSnapshot successfully updated!")
                        }
                        .addOnFailureListener { e ->
                            Log.w(ContentValues.TAG, "Error updating document", e)
                        }
                }
            }
        }
        .addOnFailureListener { exception ->
            println("Error getting documents: $exception")
        }
}