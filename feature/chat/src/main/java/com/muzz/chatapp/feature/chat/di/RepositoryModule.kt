package com.muzz.chatapp.feature.chat.di

import com.muzz.chatapp.feature.chat.data.DefaultChatRepository
import com.muzz.chatapp.feature.chat.domain.ChatRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {

    @Binds
    abstract fun bindChatRepository(impl: DefaultChatRepository): ChatRepository
}
