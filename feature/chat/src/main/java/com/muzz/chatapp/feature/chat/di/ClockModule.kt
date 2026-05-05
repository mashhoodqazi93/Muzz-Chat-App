package com.muzz.chatapp.feature.chat.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.time.Clock
import java.time.ZoneId

/**
 * Indirection over [System.currentTimeMillis] / system time zone so tests can pin time
 * by overriding these bindings.
 */
@Module
@InstallIn(SingletonComponent::class)
object ClockModule {

    @Provides
    fun provideClock(): Clock = Clock.systemDefaultZone()

    @Provides
    fun provideZoneId(): ZoneId = ZoneId.systemDefault()
}
