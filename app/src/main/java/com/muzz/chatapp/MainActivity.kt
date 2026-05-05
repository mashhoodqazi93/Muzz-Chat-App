package com.muzz.chatapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.muzz.chatapp.core.designsystem.MuzzTheme
import com.muzz.chatapp.feature.chat.ui.ChatRoute
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint(ComponentActivity::class)
class MainActivity : Hilt_MainActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            MuzzTheme {
                ChatRoute(onBackClick = { finish() })
            }
        }
    }
}
