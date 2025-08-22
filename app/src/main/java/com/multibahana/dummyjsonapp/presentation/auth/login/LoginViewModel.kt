package com.multibahana.dummyjsonapp.presentation.auth.login

// presentation/auth/login/LoginViewModel.kt
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.multibahana.dummyjsonapp.data.local.DataStoreManager
import com.multibahana.dummyjsonapp.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state

    val accessToken: StateFlow<String?> = dataStoreManager.accessToken
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    val refreshToken: StateFlow<String?> = dataStoreManager.refreshToken
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _state.value = LoginState(isLoading = true)
            try {
                val result = loginUseCase(email, password) // ini Result<UserEntity>
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

    fun logout() {
        viewModelScope.launch {
            dataStoreManager.clearTokens()
            _state.value = LoginState(isLogout = true)
        }
    }
}
