package com.example.finalproject.ui.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.finalproject.data.database.JournalDatabase
import com.example.finalproject.data.model.JournalEntry
import com.example.finalproject.data.repository.JournalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.util.Calendar

data class JournalUiState(
    val entries: List<JournalEntry> = emptyList(),
    val searchQuery: String = "",
    val selectedMood: String? = null,
    val filteredEntries: List<JournalEntry> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class JournalViewModel(private val repository: JournalRepository, private val context: Context) :
    ViewModel() {
    
    private val _uiState = MutableStateFlow(JournalUiState())
    val uiState: StateFlow<JournalUiState> = _uiState.asStateFlow()
    
    init {
        observeEntries()
    }
    
    private fun observeEntries() {
        viewModelScope.launch {
            repository.getAllEntries().collect { entries ->
                _uiState.update { it.copy(entries = entries) }
                applyFilters()
            }
        }
    }
    
    fun createEntry(title: String, description: String, mood: String, photoUri: String? = null) {
        if (title.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Title cannot be empty") }
            return
        }
        
        viewModelScope.launch {
            try {
                val savedPhotoUri = photoUri?.let { savePhotoLocally(Uri.parse(it)) } ?: ""
                val newEntry = JournalEntry(
                    title = title,
                    description = description,
                    mood = mood,
                    photoUri = savedPhotoUri
                )
                repository.insertEntry(newEntry)
                _uiState.update { it.copy(errorMessage = null) }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "Failed to create entry: ${e.message}") }
            }
        }
    }
    
    fun updateEntry(entry: JournalEntry) {
        viewModelScope.launch {
            try {
                repository.updateEntry(entry)
                _uiState.update { it.copy(errorMessage = null) }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "Failed to update entry: ${e.message}") }
            }
        }
    }
    
    fun deleteEntry(entry: JournalEntry) {
        viewModelScope.launch {
            try {
                repository.deleteEntry(entry)
                _uiState.update { it.copy(errorMessage = null) }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "Failed to delete entry: ${e.message}") }
            }
        }
    }
    
    fun setSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        applyFilters()
    }
    
    fun setSelectedMood(mood: String?) {
        _uiState.update { it.copy(selectedMood = mood) }
        applyFilters()
    }
    
    private fun applyFilters() {
        val current = _uiState.value
        var filtered = current.entries
        
        if (current.searchQuery.isNotBlank()) {
            filtered = filtered.filter {
                it.title.contains(current.searchQuery, ignoreCase = true) ||
                        it.description.contains(current.searchQuery, ignoreCase = true)
            }
        }
        
        if (current.selectedMood != null) {
            filtered = filtered.filter { it.mood == current.selectedMood }
        }
        
        _uiState.update { it.copy(filteredEntries = filtered) }
    }
    
    private fun savePhotoLocally(uri: Uri): String {
        return try {
            val photoDir = File(context.filesDir, "photos")
            if (!photoDir.exists()) {
                photoDir.mkdirs()
            }
            
            val photoFile = File(photoDir, "photo_${System.currentTimeMillis()}.jpg")
            context.contentResolver.openInputStream(uri)?.use { input ->
                photoFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            photoFile.absolutePath
        } catch (e: Exception) {
            _uiState.update { it.copy(errorMessage = "Failed to save photo: ${e.message}") }
            ""
        }
    }
}

class JournalViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(JournalViewModel::class.java)) {
            val database = JournalDatabase.getDatabase(context)
            val repository = JournalRepository(database.journalEntryDao())
            @Suppress("UNCHECKED_CAST")
            return JournalViewModel(repository, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
