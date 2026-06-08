package com.example.finalproject.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.finalproject.data.model.JournalEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface JournalEntryDao {
    
    @Insert
    suspend fun insertEntry(entry: JournalEntry): Long
    
    @Update
    suspend fun updateEntry(entry: JournalEntry)
    
    @Delete
    suspend fun deleteEntry(entry: JournalEntry)
    
    @Query("SELECT * FROM journal_entries ORDER BY date DESC")
    fun getAllEntries(): Flow<List<JournalEntry>>
    
    @Query("SELECT * FROM journal_entries WHERE id = :id")
    fun getEntryById(id: Long): Flow<JournalEntry?>
    
    @Query("SELECT * FROM journal_entries WHERE title LIKE '%' || :searchQuery || '%' OR description LIKE '%' || :searchQuery || '%' ORDER BY date DESC")
    fun searchEntries(searchQuery: String): Flow<List<JournalEntry>>
    
    @Query("SELECT * FROM journal_entries WHERE mood = :mood ORDER BY date DESC")
    fun getEntriesByMood(mood: String): Flow<List<JournalEntry>>
    
    @Query("SELECT * FROM journal_entries WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun getEntriesByDateRange(startDate: Long, endDate: Long): Flow<List<JournalEntry>>
}
