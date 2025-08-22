package com.multibahana.dummyjsonapp.data.remote.api

import com.multibahana.dummyjsonapp.data.model.UserDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST



data class LoginRequest(
    val username: String,
    val password: String,
    val expiresInMins: Int = 30
)

interface AuthService {
    @Headers("Content-Type: application/json")
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<UserDto>
}