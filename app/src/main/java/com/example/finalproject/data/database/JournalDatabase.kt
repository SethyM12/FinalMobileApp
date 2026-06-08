package com.example.finalproject.data.database

// Simple database provider for in-memory storage
object JournalDatabase {
    private var dao: JournalEntryDao? = null
    
    fun getDao(): JournalEntryDao {
        return dao ?: JournalEntryDao().also { dao = it }
    }
}
