package dev.frananda.carbonfootprinttracker.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.frananda.carbonfootprinttracker.data.local.SecureStorage
import dev.frananda.carbonfootprinttracker.data.local.SecureStorageImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class StorageModule {
    @Binds
    @Singleton
    abstract fun bindSecureStorage(
        secureStorage: SecureStorageImpl
    ): SecureStorage
}