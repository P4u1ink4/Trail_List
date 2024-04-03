package com.example.projektlab.data

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object TrailRepository {
    private val db = Firebase.firestore

    fun getTrails(onSuccess: (List<Trail>) -> Unit, onError: (Exception) -> Unit) {
        db.collection("trails")
            .get()
            .addOnSuccessListener { result ->
                val trailsList = mutableListOf<Trail>()
                for (document in result) {
                    val trail = document.toObject(Trail::class.java)
                    trailsList.add(trail)
                }
                onSuccess(trailsList)
            }
            .addOnFailureListener { exception ->
                println("Error getting documents: $exception")
            }
    }
}
