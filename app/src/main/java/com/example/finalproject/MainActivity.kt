package com.example.finalproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.finalproject.navigation.Screen
import com.example.finalproject.ui.screens.DetailScreen
import com.example.finalproject.ui.screens.EditScreen
import com.example.finalproject.ui.screens.HomeScreen
import com.example.finalproject.ui.theme.FinalProjectTheme
import com.example.finalproject.ui.viewmodel.JournalViewModel
import com.example.finalproject.ui.viewmodel.JournalViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FinalProjectTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val currentScreen = remember { mutableStateOf<Screen>(Screen.Home) }
                    val viewModel: JournalViewModel = viewModel(factory = JournalViewModelFactory(this@MainActivity))
                    
                    when (val screen = currentScreen.value) {
                        is Screen.Home -> {
                            HomeScreen(
                                viewModel = viewModel,
                                onEntryClick = { entryId ->
                                    currentScreen.value = Screen.Detail(entryId)
                                },
                                onCreateClick = {
                                    currentScreen.value = Screen.Edit(null)
                                },
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        is Screen.Detail -> {
                            DetailScreen(
                                entryId = screen.entryId,
                                viewModel = viewModel,
                                onBack = {
                                    currentScreen.value = Screen.Home
                                },
                                onEdit = {
                                    currentScreen.value = Screen.Edit(screen.entryId)
                                },
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        is Screen.Edit -> {
                            EditScreen(
                                entryId = screen.entryId,
                                viewModel = viewModel,
                                onBack = {
                                    currentScreen.value = Screen.Home
                                },
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }
        }
    }
}
