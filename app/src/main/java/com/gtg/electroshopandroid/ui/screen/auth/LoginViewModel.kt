package com.gtg.electroshopandroid.ui.screen.auth

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {
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

    fun onLoginClick() {
        // Xử lý đăng nhập (gọi API, validate, log, v.v.)
        println("Đăng nhập với: ${userName.value} / ${password.value}")
    }
}