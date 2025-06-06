package com.gtg.electroshopandroid.ui.screen.auth

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.gtg.electroshopandroid.data.model.AuthResponse
import com.gtg.electroshopandroid.data.model.LoginRequest
import com.gtg.electroshopandroid.data.repository.AuthRepository
import com.gtg.electroshopandroid.preferences.TokenPreferences
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val tokenPreferences: TokenPreferences

) : ViewModel() {
    var userName = mutableStateOf("")
        private set

    var password = mutableStateOf("")
        private set

    var passwordVisible = mutableStateOf(false)
        private set

    fun onUserNameChanged(newValue: String) {
        userName.value = newValue
    }

    fun onPasswordChanged(newValue: String) {
        password.value = newValue
    }

    fun togglePasswordVisibility() {
        passwordVisible.value = !passwordVisible.value
    }

    var loginSuccess = mutableStateOf(false)
    var loginError = mutableStateOf<String?>(null)


    fun onLoginClick() {
        // 1. Set state về null / loading (nếu muốn)
        loginError.value = null
        viewModelScope.launch {
            try {
                // 2. Gọi API login và nhận AuthResponse
                val response: AuthResponse = authRepository.login(
                    LoginRequest(
                        userName = userName.value,
                        password = password.value
                    )
                )
                // 3. Lưu token vào DataStore qua TokenPreferences
                tokenPreferences.updateAccessToken(response.token)
                Log.d("LoginViewModel", "Login thành công, token: ${response.token}")

                // 4. Đánh dấu login thành công
                loginSuccess.value = true
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Login thất bại: ${e.localizedMessage}")
                // 5. Nếu có lỗi (mạng, 401, v.v.), set loginError
                loginError.value = "Sai tài khoản hoặc mật khẩu"
            }
        }
    }
    class LoginViewModelFactory(
        private val authRepository: AuthRepository,
        private val tokenPreferences: TokenPreferences
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(authRepository, tokenPreferences) as T
        }
    }
}