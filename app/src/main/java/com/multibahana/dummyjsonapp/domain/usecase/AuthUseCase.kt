package com.multibahana.dummyjsonapp.domain.usecase

import com.multibahana.dummyjsonapp.domain.repository.AuthRepository
import jakarta.inject.Inject

class AuthUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String) =
        repository.login(email, password)

    suspend operator fun invoke(token: String) = repository.getMe(token)
}
