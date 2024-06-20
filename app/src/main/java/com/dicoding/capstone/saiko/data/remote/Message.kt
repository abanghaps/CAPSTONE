package com.dicoding.capstone.saiko.data.remote

data class Message(
    val id: String = "",
    val body: String = "",
    val isSent: Boolean = false,
    val timestamp: Long = 0L
)

