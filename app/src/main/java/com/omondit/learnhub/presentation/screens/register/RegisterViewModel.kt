package com.omondit.learnhub.presentation.screens.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omondit.learnhub.domain.model.User
import com.omondit.learnhub.domain.model.UserRole
import com.omondit.learnhub.domain.usecase.auth.RegisterUseCase
import com.omondit.learnhub.presentation.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword: StateFlow<String> = _confirmPassword.asStateFlow()

    private val _selectedRole = MutableStateFlow(UserRole.STUDENT)
    val selectedRole: StateFlow<UserRole> = _selectedRole.asStateFlow()

    private val _registerState = MutableStateFlow<UiState<User>>(UiState.Idle)
    val registerState: StateFlow<UiState<User>> = _registerState.asStateFlow()

    private val _nameError = MutableStateFlow<String?>(null)
    val nameError: StateFlow<String?> = _nameError.asStateFlow()

    private val _emailError = MutableStateFlow<String?>(null)
    val emailError: StateFlow<String?> = _emailError.asStateFlow()

    private val _passwordError = MutableStateFlow<String?>(null)
    val passwordError: StateFlow<String?> = _passwordError.asStateFlow()

    private val _confirmPasswordError = MutableStateFlow<String?>(null)
    val confirmPasswordError: StateFlow<String?> = _confirmPasswordError.asStateFlow()

    fun onNameChange(newName: String) {
        _name.value = newName
        _nameError.value = null
    }

    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
        _emailError.value = null
    }

    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
        _passwordError.value = null
    }

    fun onConfirmPasswordChange(newConfirmPassword: String) {
        _confirmPassword.value = newConfirmPassword
        _confirmPasswordError.value = null
    }

    fun onRoleSelected(role: UserRole) {
        _selectedRole.value = role
    }

    fun register() {
        if (!validateInputs()) {
            return
        }

        viewModelScope.launch {
            _registerState.value = UiState.Loading

            val result = registerUseCase(
                RegisterUseCase.Params(
                    name = _name.value.trim(),
                    email = _email.value.trim(),
                    password = _password.value
                )
            )

            result.fold(
                onSuccess = { user ->
                    _registerState.value = UiState.Success(user)
                },
                onFailure = { exception ->
                    _registerState.value = UiState.Error(
                        exception.message ?: "Registration failed. Please try again."
                    )
                }
            )
        }
    }

    private fun validateInputs(): Boolean {
        var isValid = true

        // Validate name
        if (_name.value.trim().isBlank()) {
            _nameError.value = "Name is required"
            isValid = false
        } else if (_name.value.trim().length < 2) {
            _nameError.value = "Name must be at least 2 characters"
            isValid = false
        }

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
        }

        // Validate password
        when {
            _password.value.isBlank() -> {
                _passwordError.value = "Password is required"
                isValid = false
            }
            _password.value.length < 6 -> {
                _passwordError.value = "Password must be at least 6 characters"
                isValid = false
            }
        }

        // Validate confirm password
        if (_confirmPassword.value != _password.value) {
            _confirmPasswordError.value = "Passwords do not match"
            isValid = false
        }

        return isValid
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun resetRegisterState() {
        _registerState.value = UiState.Idle
    }
}
