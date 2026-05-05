package com.muzz.chatapp.feature.chat.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.muzz.chatapp.feature.chat.data.AppDatabase
import com.muzz.chatapp.feature.chat.data.MessageDao
import com.muzz.chatapp.feature.chat.data.seedMessages
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Provider
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * Provides the Room database and its DAO. The first-run seed is wired through a
 * [Provider] to break the chicken-and-egg between database creation and DAO retrieval.
 */
@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
        daoProvider: Provider<MessageDao>,
    ): AppDatabase {
        val seedScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "muzz-chat.db",
        )
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    seedScope.launch {
                        daoProvider.get().insertAll(seedMessages(System.currentTimeMillis()))
                    }
                }
            })
            .build()
    }

    @Provides
    fun provideMessageDao(database: AppDatabase): MessageDao = database.messageDao()
}
