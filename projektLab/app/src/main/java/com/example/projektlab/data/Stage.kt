package com.example.projektlab.data

data class Stage(
    val id: Int,
    val name: String,
    val description: String,
    val estimatedTime: Int
){
    constructor() : this(0, "", "",0)
}