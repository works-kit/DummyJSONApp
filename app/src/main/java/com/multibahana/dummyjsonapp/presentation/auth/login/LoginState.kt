package com.multibahana.dummyjsonapp.presentation.auth.login

import com.multibahana.dummyjsonapp.data.model.UserDto

data class LoginState(
    val isLogout: Boolean = false,
    val isLoading: Boolean = false,
    val user: Result<UserDto>? = null,
    val error: String? = null
)