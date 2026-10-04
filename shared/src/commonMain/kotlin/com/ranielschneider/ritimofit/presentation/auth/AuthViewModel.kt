package com.ranielschneider.ritimofit.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ranielschneider.ritimofit.core.design.theme.ThemeMode
import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.User
import com.ranielschneider.ritimofit.domain.model.UserRole
import com.ranielschneider.ritimofit.domain.usecase.ForgotPasswordUseCase
import com.ranielschneider.ritimofit.domain.usecase.LoginUseCase
import com.ranielschneider.ritimofit.domain.usecase.RegisterUserUseCase
import com.ranielschneider.ritimofit.presentation.base.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class AuthSubScreen {
    LOGIN,
    FORGOT_PASSWORD,
    ROLE_SELECTION,
    REGISTER_INITIAL
}

class AuthViewModel(
    private val loginUseCase: LoginUseCase,
    private val forgotPasswordUseCase: ForgotPasswordUseCase,
    private val registerUserUseCase: RegisterUserUseCase
) : ViewModel() {

    private val _currentScreen = MutableStateFlow(AuthSubScreen.LOGIN)
    val currentScreen: StateFlow<AuthSubScreen> = _currentScreen.asStateFlow()

    private val _themeMode = MutableStateFlow(ThemeMode.DARK)
    val themeMode: StateFlow<ThemeMode> = _themeMode.asStateFlow()

    // Login Form
    var loginEmail = MutableStateFlow("")
        private set
    var loginPassword = MutableStateFlow("")
        private set
    var loginEmailError = MutableStateFlow<String?>(null)
        private set
    var loginPasswordError = MutableStateFlow<String?>(null)
        private set
    private val _loginUiState = MutableStateFlow<UiState<User>>(UiState.Idle)
    val loginUiState: StateFlow<UiState<User>> = _loginUiState.asStateFlow()

    // Forgot Password Form
    var forgotEmail = MutableStateFlow("")
        private set
    var forgotEmailError = MutableStateFlow<String?>(null)
        private set
    private val _forgotUiState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val forgotUiState: StateFlow<UiState<Unit>> = _forgotUiState.asStateFlow()

    // Role Selection
    private val _selectedRole = MutableStateFlow(UserRole.STUDENT)
    val selectedRole: StateFlow<UserRole> = _selectedRole.asStateFlow()

    // Registration Form
    var registerName = MutableStateFlow("")
        private set
    var registerEmail = MutableStateFlow("")
        private set
    var registerPassword = MutableStateFlow("")
        private set
    var registerConfirmPassword = MutableStateFlow("")
        private set
    var registerNameError = MutableStateFlow<String?>(null)
        private set
    var registerEmailError = MutableStateFlow<String?>(null)
        private set
    var registerPasswordError = MutableStateFlow<String?>(null)
        private set
    var registerConfirmError = MutableStateFlow<String?>(null)
        private set
    private val _registerUiState = MutableStateFlow<UiState<User>>(UiState.Idle)
    val registerUiState: StateFlow<UiState<User>> = _registerUiState.asStateFlow()

    fun toggleThemeMode() {
        _themeMode.value = if (_themeMode.value == ThemeMode.DARK) ThemeMode.LIGHT else ThemeMode.DARK
    }

    fun navigateTo(screen: AuthSubScreen) {
        _currentScreen.value = screen
    }

    fun navigateBack() {
        when (_currentScreen.value) {
            AuthSubScreen.FORGOT_PASSWORD -> navigateTo(AuthSubScreen.LOGIN)
            AuthSubScreen.ROLE_SELECTION -> navigateTo(AuthSubScreen.LOGIN)
            AuthSubScreen.REGISTER_INITIAL -> navigateTo(AuthSubScreen.ROLE_SELECTION)
            AuthSubScreen.LOGIN -> {}
        }
    }

    fun onLoginEmailChanged(email: String) {
        loginEmail.value = email
        if (loginEmailError.value != null) loginEmailError.value = null
    }

    fun onLoginPasswordChanged(password: String) {
        loginPassword.value = password
        if (loginPasswordError.value != null) loginPasswordError.value = null
    }

    fun submitLogin() {
        val email = loginEmail.value.trim()
        val password = loginPassword.value

        var isValid = true
        if (email.isBlank() || !email.contains("@") || !email.contains(".")) {
            loginEmailError.value = "Informe um e-mail válido"
            isValid = false
        }
        if (password.length < 6) {
            loginPasswordError.value = "A senha deve conter no mínimo 6 caracteres"
            isValid = false
        }

        if (!isValid) return

        viewModelScope.launch {
            _loginUiState.value = UiState.Loading
            when (val result = loginUseCase(email, password)) {
                is AppResult.Success -> {
                    _loginUiState.value = UiState.Success(result.data)
                }
                is AppResult.Error -> {
                    _loginUiState.value = UiState.Error(
                        error = result.error,
                        message = "E-mail ou senha inválidos."
                    )
                }
            }
        }
    }

    fun onForgotEmailChanged(email: String) {
        forgotEmail.value = email
        if (forgotEmailError.value != null) forgotEmailError.value = null
    }

    fun submitForgotPassword() {
        val email = forgotEmail.value.trim()
        if (email.isBlank() || !email.contains("@") || !email.contains(".")) {
            forgotEmailError.value = "Informe um e-mail válido"
            return
        }

        viewModelScope.launch {
            _forgotUiState.value = UiState.Loading
            when (val result = forgotPasswordUseCase(email)) {
                is AppResult.Success -> {
                    _forgotUiState.value = UiState.Success(Unit)
                }
                is AppResult.Error -> {
                    _forgotUiState.value = UiState.Error(
                        error = result.error,
                        message = "E-mail não encontrado no sistema."
                    )
                }
            }
        }
    }

    fun onRoleSelected(role: UserRole) {
        _selectedRole.value = role
    }

    fun onRegisterNameChanged(name: String) {
        registerName.value = name
        if (registerNameError.value != null) registerNameError.value = null
    }

    fun onRegisterEmailChanged(email: String) {
        registerEmail.value = email
        if (registerEmailError.value != null) registerEmailError.value = null
    }

    fun onRegisterPasswordChanged(password: String) {
        registerPassword.value = password
        if (registerPasswordError.value != null) registerPasswordError.value = null
    }

    fun onRegisterConfirmPasswordChanged(confirm: String) {
        registerConfirmPassword.value = confirm
        if (registerConfirmError.value != null) registerConfirmError.value = null
    }

    fun submitRegister() {
        val name = registerName.value.trim()
        val email = registerEmail.value.trim()
        val password = registerPassword.value
        val confirm = registerConfirmPassword.value

        var isValid = true
        if (name.length < 2) {
            registerNameError.value = "Informe seu nome completo"
            isValid = false
        }
        if (email.isBlank() || !email.contains("@") || !email.contains(".")) {
            registerEmailError.value = "Informe um e-mail válido"
            isValid = false
        }
        if (password.length < 6) {
            registerPasswordError.value = "A senha deve ter pelo menos 6 caracteres"
            isValid = false
        }
        if (confirm != password) {
            registerConfirmError.value = "As senhas não coincidem"
            isValid = false
        }

        if (!isValid) return

        viewModelScope.launch {
            _registerUiState.value = UiState.Loading
            when (val result = registerUserUseCase(name, email, password, _selectedRole.value)) {
                is AppResult.Success -> {
                    _registerUiState.value = UiState.Success(result.data)
                }
                is AppResult.Error -> {
                    _registerUiState.value = UiState.Error(
                        error = result.error,
                        message = "Erro ao criar conta. Tente novamente."
                    )
                }
            }
        }
    }

    fun resetForgotState() {
        _forgotUiState.value = UiState.Idle
        forgotEmail.value = ""
        forgotEmailError.value = null
    }
}
