package com.multibahana.dummyjsonapp.domain.usecase

import com.multibahana.dummyjsonapp.domain.repository.AuthRepository
import jakarta.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String) =
        repository.login(email, password)
}
