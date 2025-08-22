package com.multibahana.dummyjsonapp.domain.model

data class User(
    val id: Int,
    val username: String,
    val email: String,
    val accessToken: String,
    val refreshToken: String
)
