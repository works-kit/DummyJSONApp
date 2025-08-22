package com.multibahana.dummyjsonapp.data.repository

import com.multibahana.dummyjsonapp.data.model.UserDto
import com.multibahana.dummyjsonapp.data.remote.api.AuthService
import com.multibahana.dummyjsonapp.data.remote.api.LoginRequest
import com.multibahana.dummyjsonapp.domain.repository.AuthRepository
import jakarta.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authService: AuthService
) : AuthRepository {
    override suspend fun login(username: String, password: String): Result<UserDto> {
        return try {
            val response = authService.login(LoginRequest(username, password))
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Response kosong"))
            } else {
                Result.failure(Exception("Login gagal: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}