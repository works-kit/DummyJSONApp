package com.multibahana.dummyjsonapp.di

// di/RepositoryModule.kt
import com.multibahana.dummyjsonapp.data.local.DataStoreManager
import com.multibahana.dummyjsonapp.data.remote.api.AuthService
import com.multibahana.dummyjsonapp.data.repository.AuthRepositoryImpl
import com.multibahana.dummyjsonapp.domain.repository.AuthRepository
import com.multibahana.dummyjsonapp.domain.usecase.AuthUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideAuthRepository(api: AuthService, dataStoreManager: DataStoreManager): AuthRepository =
        AuthRepositoryImpl(api, dataStoreManager)

    @Provides
    fun provideAuthUseCase(repo: AuthRepository): AuthUseCase =
        AuthUseCase(repo)
}
