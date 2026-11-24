package com.aura.neon.core.data.di

import com.aura.neon.core.common.AndroidDispatcherProvider
import com.aura.neon.core.common.DispatcherProvider
import com.aura.neon.core.data.hardware.AndroidRamProvider
import com.aura.neon.core.data.repo.ModelRepositoryImpl
import com.aura.neon.core.domain.hardware.RamProvider
import com.aura.neon.core.domain.repository.ModelRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindModelRepository(impl: ModelRepositoryImpl): ModelRepository

    @Binds
    @Singleton
    abstract fun bindRamProvider(impl: AndroidRamProvider): RamProvider

    @Binds
    @Singleton
    abstract fun bindDispatcherProvider(impl: AndroidDispatcherProvider): DispatcherProvider
}
