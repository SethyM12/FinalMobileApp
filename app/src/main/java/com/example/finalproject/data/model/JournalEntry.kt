package com.example.finalproject.data.model

data class JournalEntry(
    val id: Long = 0,
    val title: String,
    val description: String = "",
    val mood: String = "Neutral",
    val date: Long = System.currentTimeMillis(),
    val photoUri: String = ""
)
