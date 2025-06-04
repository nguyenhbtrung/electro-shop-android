package com.gtg.electroshopandroid.ui.screen.auth

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class SignUpViewModel : ViewModel() {
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

    var signUpSuccess = mutableStateOf(false)

    fun onSignUpClick() {
        signUpSuccess.value = true
//        if (userName.value == "admin" && password.value == "123") {
//
//        } else {
//            // Báo lỗi nếu cần
//        }
        // Xử lý đăng nhập (gọi API, validate, log, v.v.)
        println("Đăng ký với: ${userName.value} / ${password.value}")
    }
}