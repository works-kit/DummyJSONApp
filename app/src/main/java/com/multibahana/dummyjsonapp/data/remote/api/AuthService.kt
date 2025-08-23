package com.multibahana.dummyjsonapp.data.remote.api

import com.multibahana.dummyjsonapp.data.model.UserDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST



data class LoginRequest(
    val username: String,
    val password: String,
    val expiresInMins: Int = 1
)

data class RefreshTokenRequest(val refreshToken: String?, val expiresInMins : Int = 1)

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String
)


interface AuthService {
    @Headers("Content-Type: application/json")
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<UserDto>

    @GET("auth/me")
    suspend fun getMe(
        @Header("Authorization") token: String
    ): Response<UserDto>

    @POST("auth/refresh")
    suspend fun refreshToken(
        @Body request: RefreshTokenRequest
    ): Response<TokenResponse>
}