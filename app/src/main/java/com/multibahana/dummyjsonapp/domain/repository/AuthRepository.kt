package com.multibahana.dummyjsonapp.domain.repository

import com.multibahana.dummyjsonapp.data.model.UserDto

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<UserDto>
    suspend fun getMe(token: String): Result<UserDto>
}
