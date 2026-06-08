package com.example.finalproject.data.database

import com.example.finalproject.data.model.JournalEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

// Simple in-memory DAO - stores all entries in memory
class JournalEntryDao {
    private val entries = MutableStateFlow<List<JournalEntry>>(emptyList())
    private var nextId: Long = 1
    
    fun getAllEntries(): Flow<List<JournalEntry>> = entries
    
    suspend fun insertEntry(entry: JournalEntry): Long {
        val newEntry = entry.copy(id = nextId)
        entries.value = entries.value + newEntry
        nextId++
        return newEntry.id
    }
    
    suspend fun updateEntry(entry: JournalEntry) {
        entries.value = entries.value.map { if (it.id == entry.id) entry else it }
    }
    
    suspend fun deleteEntry(entry: JournalEntry) {
        entries.value = entries.value.filter { it.id != entry.id }
    }
    
    fun getEntryById(id: Long): Flow<JournalEntry?> {
        return kotlinx.coroutines.flow.flow {
            emit(entries.value.find { it.id == id })
        }
    }
    
    fun searchEntries(searchQuery: String): Flow<List<JournalEntry>> {
        return kotlinx.coroutines.flow.flow {
            emit(entries.value.filter {
                it.title.contains(searchQuery, ignoreCase = true) ||
                        it.description.contains(searchQuery, ignoreCase = true)
            })
        }
    }
    
    fun getEntriesByMood(mood: String): Flow<List<JournalEntry>> {
        return kotlinx.coroutines.flow.flow {
            emit(entries.value.filter { it.mood == mood })
        }
    }
    
    fun getEntriesByDateRange(startDate: Long, endDate: Long): Flow<List<JournalEntry>> {
        return kotlinx.coroutines.flow.flow {
            emit(entries.value.filter { it.date in startDate..endDate })
        }
    }
}
