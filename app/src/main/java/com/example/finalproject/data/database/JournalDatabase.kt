package com.example.finalproject.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import com.example.finalproject.data.model.JournalEntry

@Database(entities = [JournalEntry::class], version = 1, exportSchema = false)
abstract class JournalDatabase : androidx.room.RoomDatabase() {
    abstract fun journalEntryDao(): JournalEntryDao
    
    companion object {
        @Volatile
        private var Instance: JournalDatabase? = null
        
        fun getDatabase(context: Context): JournalDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    JournalDatabase::class.java,
                    "journal_database"
                ).build().also { Instance = it }
            }
        }
    }
}
