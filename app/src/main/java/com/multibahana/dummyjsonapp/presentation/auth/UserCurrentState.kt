package com.multibahana.dummyjsonapp.presentation.auth

import com.multibahana.dummyjsonapp.data.model.UserDto

data class UserCurrentState(
    val isLogout: Boolean = false,
    val isLoading: Boolean = false,
    val user: Result<UserDto>? = null,
    val error: String? = null
)