package com.omondit.learnhub.presentation.screens.register


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.omondit.learnhub.domain.model.UserRole
import com.omondit.learnhub.presentation.util.UiState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onNavigateBack: () -> Unit,
    onNavigateToHome: () -> Unit,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val name by viewModel.name.collectAsStateWithLifecycle()
    val email by viewModel.email.collectAsStateWithLifecycle()
    val password by viewModel.password.collectAsStateWithLifecycle()
    val confirmPassword by viewModel.confirmPassword.collectAsStateWithLifecycle()
    val selectedRole by viewModel.selectedRole.collectAsStateWithLifecycle()
    val registerState by viewModel.registerState.collectAsStateWithLifecycle()

    val nameError by viewModel.nameError.collectAsStateWithLifecycle()
    val emailError by viewModel.emailError.collectAsStateWithLifecycle()
    val passwordError by viewModel.passwordError.collectAsStateWithLifecycle()
    val confirmPasswordError by viewModel.confirmPasswordError.collectAsStateWithLifecycle()

    val focusManager = LocalFocusManager.current
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    // Navigate on success
    LaunchedEffect(registerState) {
        if (registerState is UiState.Success) {
            onNavigateToHome()
            viewModel.resetRegisterState()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create Account") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Join LearnHub",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "Start your learning journey today",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Name field
            OutlinedTextField(
                value = name,
                onValueChange = viewModel::onNameChange,
                label = { Text("Full Name") },
                leadingIcon = {
                    Icon(Icons.Default.Person, contentDescription = "Name")
                },
                isError = nameError != null,
                supportingText = nameError?.let { { Text(it) } },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Email field
            OutlinedTextField(
                value = email,
                onValueChange = viewModel::onEmailChange,
                label = { Text("Email") },
                leadingIcon = {
                    Icon(Icons.Default.Email, contentDescription = "Email")
                },
                isError = emailError != null,
                supportingText = emailError?.let { { Text(it) } },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Password field
            OutlinedTextField(
                value = password,
                onValueChange = viewModel::onPasswordChange,
                label = { Text("Password") },
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = "Password")
                },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible)
                                Icons.Default.Visibility
                            else
                                Icons.Default.VisibilityOff,
                            contentDescription = if (passwordVisible)
                                "Hide password"
                            else
                                "Show password"
                        )
                    }
                },
                visualTransformation = if (passwordVisible)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
                isError = passwordError != null,
                supportingText = passwordError?.let { { Text(it) } },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Confirm password field
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = viewModel::onConfirmPasswordChange,
                label = { Text("Confirm Password") },
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = "Confirm Password")
                },
                trailingIcon = {
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(
                            imageVector = if (confirmPasswordVisible)
                                Icons.Default.Visibility
                            else
                                Icons.Default.VisibilityOff,
                            contentDescription = if (confirmPasswordVisible)
                                "Hide password"
                            else
                                "Show password"
                        )
                    }
                },
                visualTransformation = if (confirmPasswordVisible)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
                isError = confirmPasswordError != null,
                supportingText = confirmPasswordError?.let { { Text(it) } },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        viewModel.register()
                    }
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Role selection
            Text(
                text = "I am a:",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FilterChip(
                    selected = selectedRole == UserRole.STUDENT,
                    onClick = { viewModel.onRoleSelected(UserRole.STUDENT) },
                    label = { Text("👨‍🎓 Student") },
                    modifier = Modifier.weight(1f)
                )

                FilterChip(
                    selected = selectedRole == UserRole.TEACHER,
                    onClick = { viewModel.onRoleSelected(UserRole.TEACHER) },
                    label = { Text("👨‍🏫 Teacher") },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Register button
            Button(
                onClick = { viewModel.register() },
                enabled = registerState !is UiState.Loading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                if (registerState is UiState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(
                        text = "Create Account",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Error message
            if (registerState is UiState.Error) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = (registerState as UiState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Login link
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Already have an account?",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                TextButton(onClick = onNavigateBack) {
                    Text(
                        text = "Login",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
