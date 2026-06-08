package com.example.finalproject.data.repository

import com.example.finalproject.data.database.JournalDatabase
import com.example.finalproject.data.model.JournalEntry
import kotlinx.coroutines.flow.Flow

class JournalRepository {
    private val dao = JournalDatabase.getDao()
    
    fun getAllEntries(): Flow<List<JournalEntry>> = dao.getAllEntries()
    
    fun getEntryById(id: Long): Flow<JournalEntry?> = dao.getEntryById(id)
    
    fun searchEntries(searchQuery: String): Flow<List<JournalEntry>> =
        dao.searchEntries(searchQuery)
    
    fun getEntriesByMood(mood: String): Flow<List<JournalEntry>> =
        dao.getEntriesByMood(mood)
    
    fun getEntriesByDateRange(startDate: Long, endDate: Long): Flow<List<JournalEntry>> =
        dao.getEntriesByDateRange(startDate, endDate)
    
    suspend fun insertEntry(entry: JournalEntry): Long = dao.insertEntry(entry)
    
    suspend fun updateEntry(entry: JournalEntry) = dao.updateEntry(entry)
    
    suspend fun deleteEntry(entry: JournalEntry) = dao.deleteEntry(entry)
}
