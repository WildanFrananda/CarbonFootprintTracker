package dev.frananda.carbonfootprinttracker.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.frananda.carbonfootprinttracker.domain.repository.ActivityRepository
import dev.frananda.carbonfootprinttracker.domain.repository.ActivityRepositoryImpl
import dev.frananda.carbonfootprinttracker.domain.repository.AuthRepository
import dev.frananda.carbonfootprinttracker.domain.repository.AuthRepositoryImpl
import dev.frananda.carbonfootprinttracker.domain.repository.DashboardRepository
import dev.frananda.carbonfootprinttracker.domain.repository.DashboardRepositoryImpl
import dev.frananda.carbonfootprinttracker.domain.repository.UserRepository
import dev.frananda.carbonfootprinttracker.domain.repository.UserRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindActivityRepository(
        activityRepositoryImpl: ActivityRepositoryImpl
    ): ActivityRepository

    @Binds
    @Singleton
    abstract fun bindDashboardRepository(
        dashboardRepositoryImpl: DashboardRepositoryImpl
    ): DashboardRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository
}