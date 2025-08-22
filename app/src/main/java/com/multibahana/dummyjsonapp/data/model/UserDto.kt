package com.multibahana.dummyjsonapp.data.model

data class UserDto(
    val id: Int,
    val username: String,
    val email: String,
    val accessToken: String,
    val refreshToken: String
)
