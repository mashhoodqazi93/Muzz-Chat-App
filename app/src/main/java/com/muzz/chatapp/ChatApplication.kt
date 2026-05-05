package com.muzz.chatapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp(Application::class)
class ChatApplication : Hilt_ChatApplication()
