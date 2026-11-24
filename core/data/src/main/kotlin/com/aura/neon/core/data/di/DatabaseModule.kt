package com.aura.neon.core.data.di

import android.content.Context
import androidx.room.Room
import com.aura.neon.core.data.db.AuraDatabase
import com.aura.neon.core.data.db.ModelDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AuraDatabase =
        Room.databaseBuilder(context, AuraDatabase::class.java, AuraDatabase.NAME)
            .build()

    @Provides
    fun provideModelDao(database: AuraDatabase): ModelDao = database.modelDao()
}
