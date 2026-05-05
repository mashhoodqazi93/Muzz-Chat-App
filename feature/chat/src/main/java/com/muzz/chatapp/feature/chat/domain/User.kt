package com.muzz.chatapp.feature.chat.domain

sealed class User(val id: String, val displayName: String) {
    data object Me : User(id = "me", displayName = "You")
    data object Sarah : User(id = "sarah", displayName = "Sarah")

    companion object {
        val all: List<User> = listOf(Me, Sarah)
        fun other(of: User): User = if (of == Me) Sarah else Me
    }
}
