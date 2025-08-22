package com.multibahana.dummyjsonapp.di

// di/RepositoryModule.kt
import com.multibahana.dummyjsonapp.data.remote.api.AuthService
import com.multibahana.dummyjsonapp.data.repository.AuthRepositoryImpl
import com.multibahana.dummyjsonapp.domain.repository.AuthRepository
import com.multibahana.dummyjsonapp.domain.usecase.LoginUseCase
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
    fun provideAuthRepository(api: AuthService): AuthRepository =
        AuthRepositoryImpl(api)

    @Provides
    fun provideLoginUseCase(repo: AuthRepository): LoginUseCase =
        LoginUseCase(repo)
}
