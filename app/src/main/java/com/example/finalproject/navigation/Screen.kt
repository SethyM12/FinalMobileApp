package com.example.finalproject.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    object Home : Screen()
    
    @Serializable
    data class Detail(val entryId: Long) : Screen()
    
    @Serializable
    data class Edit(val entryId: Long? = null) : Screen()
}
