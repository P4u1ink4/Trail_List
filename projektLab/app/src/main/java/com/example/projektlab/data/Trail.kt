package com.example.projektlab.data

data class Trail(
    val id: Int,
    val name: String,
    val description: String,
    var image: String,
    val stages: List<Stage>
){
    constructor() : this(0, "", "","", listOf())
}