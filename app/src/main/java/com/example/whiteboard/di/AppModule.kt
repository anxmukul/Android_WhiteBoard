package com.example.whiteboard.di

import android.content.Context
import com.example.whiteboard.data.LocalRepository
import com.example.whiteboard.domain.ILocalRepository

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideLocalRepo(
        @ApplicationContext context: Context,
    ): ILocalRepository {
        return LocalRepository(context)
    }
}