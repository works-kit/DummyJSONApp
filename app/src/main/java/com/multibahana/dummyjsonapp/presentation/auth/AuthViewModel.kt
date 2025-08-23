package com.multibahana.dummyjsonapp.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.multibahana.dummyjsonapp.data.local.DataStoreManager
import com.multibahana.dummyjsonapp.domain.usecase.AuthUseCase
import com.multibahana.dummyjsonapp.presentation.auth.login.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state

    private val _currentUserState = MutableStateFlow(UserCurrentState())
    val currentUserState: StateFlow<UserCurrentState> = _currentUserState

    val accessToken: StateFlow<String?> = dataStoreManager.accessToken
        .stateIn(viewModelScope, SharingStarted.Companion.Lazily, null)

    val refreshToken: StateFlow<String?> = dataStoreManager.refreshToken
        .stateIn(viewModelScope, SharingStarted.Companion.Lazily, null)

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _state.value = LoginState(isLoading = true)
            try {
                val result = authUseCase(email, password) // ini Result<UserEntity>
                val user = result.getOrNull()

                user?.let {
                    dataStoreManager.saveTokens(
                        access = it.accessToken,
                        refresh = it.refreshToken
                    )
                    _state.value = LoginState(user = result, isLoading = false, isLogout = false)
                }
            } catch (e: Exception) {
                _state.value = LoginState(error = e.message ?: "Unknown error")
            }
        }
    }

    fun getMe(token : String) {
        viewModelScope.launch {
            _currentUserState.value = UserCurrentState(isLoading = true)
            try {
                val result = authUseCase(token) // ini Result<UserEntity>
                val user = result.getOrNull()
                user?.let {
                    _currentUserState.value = UserCurrentState(user = result, isLoading = false, isLogout = false)
                }

            } catch (e: Exception) {
                _currentUserState.value = UserCurrentState(error = e.message ?: "Unknown error")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            dataStoreManager.clearTokens()
            _state.value = LoginState(isLogout = true)
        }
    }
}