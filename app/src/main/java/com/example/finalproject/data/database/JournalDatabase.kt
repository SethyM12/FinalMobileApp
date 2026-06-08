package com.example.finalproject.data.database

import android.content.Context

object JournalDatabase {
    private var dao: JournalEntryDao? = null
    
    fun getDao(context: Context): JournalEntryDao {
        return dao ?: JournalEntryDao(context).also { dao = it }
    }
}
