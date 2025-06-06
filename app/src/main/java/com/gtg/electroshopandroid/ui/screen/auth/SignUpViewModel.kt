package com.gtg.electroshopandroid.ui.screen.auth

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.gtg.electroshopandroid.ElectroShopApplication
import com.gtg.electroshopandroid.data.model.AuthResponse
import com.gtg.electroshopandroid.data.model.RegisterRequest
import com.gtg.electroshopandroid.data.repository.AuthRepository
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {
    var email = mutableStateOf("")
        private set

    var userName = mutableStateOf("")
        private set

    var password = mutableStateOf("")
        private set

    var passwordVisible = mutableStateOf(false)
        private set

    var confirmPassword = mutableStateOf("")
        private set

    fun onConfirmPasswordChanged(newValue: String) {
        confirmPassword.value = newValue
    }

    fun onUserNameChanged(newValue: String) {
        userName.value = newValue
    }

    fun onEmailChanged(newValue: String) {
        email.value = newValue
    }

    fun onPasswordChanged(newValue: String) {
        password.value = newValue
    }

    fun togglePasswordVisibility() {
        passwordVisible.value = !passwordVisible.value
    }

    var registerSuccess = mutableStateOf(false)

    var registerError = mutableStateOf<String?>(null)

    fun onRegisterClick() {
        if (password.value != confirmPassword.value) {
            registerError.value = "Mật khẩu không khớp"
            return
        }
        registerError.value = null
        viewModelScope.launch {
            try {
                val response: AuthResponse = authRepository.register(
                    RegisterRequest(
                        userName = userName.value,
                        email = email.value,
                        password = password.value
                    )
                )
                Log.d("Register", "Đăng ký thành công, token: ${response.token}")
                registerSuccess.value = true
            } catch (e: Exception) {
                Log.e("Register", "Đăng ký thất bại: ${e.localizedMessage}")
                registerError.value = "Đăng ký thất bại"
            }
        }
    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as ElectroShopApplication)
                val authRepository = application.container.authRepository
                SignUpViewModel(authRepository = authRepository)
            }
        }
    }
}
