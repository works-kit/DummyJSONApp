package com.multibahana.dummyjsonapp.data.repository

import android.util.Log
import com.multibahana.dummyjsonapp.data.local.DataStoreManager
import com.multibahana.dummyjsonapp.data.model.UserDto
import com.multibahana.dummyjsonapp.data.remote.api.AuthService
import com.multibahana.dummyjsonapp.data.remote.api.LoginRequest
import com.multibahana.dummyjsonapp.data.remote.api.RefreshTokenRequest
import com.multibahana.dummyjsonapp.domain.repository.AuthRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.first

class AuthRepositoryImpl @Inject constructor(
    private val authService: AuthService,
    private val dataStoreManager: DataStoreManager
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

    override  suspend fun getMe(token: String): Result<UserDto> {
        return try {
            val response = authService.getMe("Bearer ${token}")
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Response kosong"))
            } else if (response.code() == 401) {
                generateNewAccessToken()
            }else{
                Result.failure(Exception("Gagal data user: ${response.errorBody()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun generateNewAccessToken(): Result<UserDto> {
        val refreshToken = dataStoreManager.refreshToken.first()  ?: return Result.failure(Exception("No refresh token"))

        return try {
            val refreshResponse = authService.refreshToken(
                RefreshTokenRequest(refreshToken = refreshToken)
            )

            if (refreshResponse.isSuccessful) {
                refreshResponse.body()?.let { newTokens ->
                    Log.i("NEW_TOKEN", "accessToken=${newTokens.accessToken} refreshToken=${newTokens.refreshToken}")
                    dataStoreManager.saveTokens(newTokens.accessToken, newTokens.refreshToken)

                    val retryResponse = authService.getMe("Bearer ${newTokens.accessToken}")
                    if (retryResponse.isSuccessful) {
                        retryResponse.body()?.let {
                            return Result.success(it)
                        }
                    }
                }
            }
            Result.failure(Exception("Refresh token gagal"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}