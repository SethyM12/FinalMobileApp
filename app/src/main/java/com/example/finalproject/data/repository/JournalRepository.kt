package com.example.finalproject.data.repository

import com.example.finalproject.data.database.JournalEntryDao
import com.example.finalproject.data.model.JournalEntry
import kotlinx.coroutines.flow.Flow

class JournalRepository(private val journalEntryDao: JournalEntryDao) {
    
    fun getAllEntries(): Flow<List<JournalEntry>> = journalEntryDao.getAllEntries()
    
    fun getEntryById(id: Long): Flow<JournalEntry?> = journalEntryDao.getEntryById(id)
    
    fun searchEntries(searchQuery: String): Flow<List<JournalEntry>> =
        journalEntryDao.searchEntries(searchQuery)
    
    fun getEntriesByMood(mood: String): Flow<List<JournalEntry>> =
        journalEntryDao.getEntriesByMood(mood)
    
    fun getEntriesByDateRange(startDate: Long, endDate: Long): Flow<List<JournalEntry>> =
        journalEntryDao.getEntriesByDateRange(startDate, endDate)
    
    suspend fun insertEntry(entry: JournalEntry): Long = journalEntryDao.insertEntry(entry)
    
    suspend fun updateEntry(entry: JournalEntry) = journalEntryDao.updateEntry(entry)
    
    suspend fun deleteEntry(entry: JournalEntry) = journalEntryDao.deleteEntry(entry)
}
