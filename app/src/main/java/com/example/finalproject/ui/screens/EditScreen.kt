package com.example.finalproject.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.finalproject.data.model.JournalEntry
import com.example.finalproject.ui.components.getMoodEmoji
import com.example.finalproject.ui.viewmodel.JournalViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreen(
    entryId: Long?,
    viewModel: JournalViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState = viewModel.uiState.collectAsState()
    val existingEntry = if (entryId != null) {
        remember { mutableStateOf<JournalEntry?>(null) }
    } else null
    
    val title = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }
    val selectedMood = remember { mutableStateOf("Neutral") }
    val photoUri = remember { mutableStateOf<Uri?>(null) }
    val moodExpanded = remember { mutableStateOf(false) }
    
    LaunchedEffect(uiState.value.entries) {
        if (entryId != null) {
            val entry = uiState.value.entries.find { it.id == entryId }
            entry?.let {
                existingEntry?.value = it
                title.value = it.title
                description.value = it.description
                selectedMood.value = it.mood
                if (it.photoUri.isNotEmpty()) {
                    photoUri.value = Uri.parse(it.photoUri)
                }
            }
        }
    }
    
    val imagePickerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { photoUri.value = it }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (entryId == null) "New Entry" else "Edit Entry") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = title.value,
                onValueChange = { title.value = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            
            OutlinedTextField(
                value = description.value,
                onValueChange = { description.value = it },
                label = { Text("Description") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                maxLines = 5
            )
            
            Text(text = "Mood", style = MaterialTheme.typography.bodyLarge)
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = getMoodEmoji(selectedMood.value), style = MaterialTheme.typography.headlineMedium)
                
                Button(onClick = { moodExpanded.value = true }) {
                    Text(selectedMood.value)
                }
                
                DropdownMenu(
                    expanded = moodExpanded.value,
                    onDismissRequest = { moodExpanded.value = false }
                ) {
                    listOf("Happy", "Sad", "Excited", "Thoughtful", "Neutral").forEach { mood ->
                        DropdownMenuItem(
                            text = { Text(mood) },
                            onClick = {
                                selectedMood.value = mood
                                moodExpanded.value = false
                            }
                        )
                    }
                }
            }
            
            Text(text = "Photo", style = MaterialTheme.typography.bodyLarge)
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { imagePickerLauncher.launch("image/*") },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Image, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
                    Text("Select Photo")
                }
            }
            
            if (photoUri.value != null) {
                AsyncImage(
                    model = photoUri.value,
                    contentDescription = "Selected photo",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant,
                            RoundedCornerShape(12.dp)
                        ),
                    contentScale = ContentScale.Crop
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = {
                    if (title.value.isBlank()) {
                        return@Button
                    }
                    
                    if (entryId == null) {
                        viewModel.createEntry(
                            title = title.value,
                            description = description.value,
                            mood = selectedMood.value,
                            photoUri = photoUri.value?.toString()
                        )
                    } else {
                        existingEntry?.value?.let { entry ->
                            viewModel.updateEntry(
                                entry.copy(
                                    title = title.value,
                                    description = description.value,
                                    mood = selectedMood.value,
                                    photoUri = photoUri.value?.toString() ?: entry.photoUri
                                )
                            )
                        }
                    }
                    onBack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(if (entryId == null) "Create Entry" else "Update Entry")
            }
            
            uiState.value.errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
