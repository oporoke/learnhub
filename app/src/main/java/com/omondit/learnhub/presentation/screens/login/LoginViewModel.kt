package com.omondit.learnhub.presentation.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omondit.learnhub.domain.model.User
import com.omondit.learnhub.domain.usecase.auth.LoginUseCase
import com.omondit.learnhub.presentation.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _loginState = MutableStateFlow<UiState<User>>(UiState.Idle)
    val loginState: StateFlow<UiState<User>> = _loginState.asStateFlow()

    private val _emailError = MutableStateFlow<String?>(null)
    val emailError: StateFlow<String?> = _emailError.asStateFlow()

    private val _passwordError = MutableStateFlow<String?>(null)
    val passwordError: StateFlow<String?> = _passwordError.asStateFlow()

    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
        _emailError.value = null // Clear error on typing
    }

    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
        _passwordError.value = null // Clear error on typing
    }

    fun login() {
        // Validate inputs
        if (!validateInputs()) {
            return
        }

        viewModelScope.launch {
            _loginState.value = UiState.Loading

            val result = loginUseCase(
                LoginUseCase.Params(
                    email = _email.value.trim(),
                    password = _password.value
                )
            )

            result.fold(
                onSuccess = { user ->
                    _loginState.value = UiState.Success(user)
                },
                onFailure = { exception ->
                    _loginState.value = UiState.Error(
                        exception.message ?: "Login failed. Please try again."
                    )
                }
            )
        }
    }

    private fun validateInputs(): Boolean {
        var isValid = true

        // Validate email
        val emailValue = _email.value.trim()
        when {
            emailValue.isBlank() -> {
                _emailError.value = "Email is required"
                isValid = false
            }
            !isValidEmail(emailValue) -> {
                _emailError.value = "Invalid email format"
                isValid = false
            }
            else -> {
                _emailError.value = null
            }
        }

        // Validate password
        val passwordValue = _password.value
        when {
            passwordValue.isBlank() -> {
                _passwordError.value = "Password is required"
                isValid = false
            }
            passwordValue.length < 6 -> {
                _passwordError.value = "Password must be at least 6 characters"
                isValid = false
            }
            else -> {
                _passwordError.value = null
            }
        }

        return isValid
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun resetLoginState() {
        _loginState.value = UiState.Idle
    }
}
