package com.example.finalproject.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "journal_entries")
data class JournalEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String = "",
    val mood: String = "Neutral",
    val date: Long = System.currentTimeMillis(),
    val photoUri: String = ""
)
