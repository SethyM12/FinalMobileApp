package com.example.finalproject.data.database

import android.content.Context
import android.content.SharedPreferences
import com.example.finalproject.data.model.JournalEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.json.JSONArray
import org.json.JSONObject

class JournalEntryDao(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("journal_db", Context.MODE_PRIVATE)
    private val entries = MutableStateFlow<List<JournalEntry>>(loadFromPreferences())
    private var nextId: Long = findMaxId() + 1
    
    private fun loadFromPreferences(): List<JournalEntry> {
        val json = prefs.getString("entries_json", "[]")
        return try {
            val jsonArray = JSONArray(json)
            (0 until jsonArray.length()).map { i ->
                val obj = jsonArray.getJSONObject(i)
                JournalEntry(
                    id = obj.getLong("id"),
                    title = obj.getString("title"),
                    description = obj.optString("description", ""),
                    mood = obj.optString("mood", "Neutral"),
                    date = obj.optLong("date", System.currentTimeMillis()),
                    photoUri = obj.optString("photoUri", "")
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    private fun findMaxId(): Long {
        return entries.value.maxOfOrNull { it.id } ?: 0
    }
    
    private fun saveToPreferences() {
        val jsonArray = JSONArray()
        entries.value.forEach { entry ->
            jsonArray.put(JSONObject().apply {
                put("id", entry.id)
                put("title", entry.title)
                put("description", entry.description)
                put("mood", entry.mood)
                put("date", entry.date)
                put("photoUri", entry.photoUri)
            })
        }
        prefs.edit().putString("entries_json", jsonArray.toString()).apply()
    }
    
    fun getAllEntries(): Flow<List<JournalEntry>> = entries
    
    suspend fun insertEntry(entry: JournalEntry): Long {
        val newEntry = entry.copy(id = nextId)
        entries.value = entries.value + newEntry
        nextId++
        saveToPreferences()
        return newEntry.id
    }
    
    suspend fun updateEntry(entry: JournalEntry) {
        entries.value = entries.value.map { if (it.id == entry.id) entry else it }
        saveToPreferences()
    }
    
    suspend fun deleteEntry(entry: JournalEntry) {
        entries.value = entries.value.filter { it.id != entry.id }
        saveToPreferences()
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
