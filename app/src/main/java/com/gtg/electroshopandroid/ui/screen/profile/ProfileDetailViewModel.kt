package com.gtg.electroshopandroid.ui.screen.profile

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class ProfileDetailViewModel() : ViewModel() {
    var userName = mutableStateOf("")
        private set
    fun onUserNameChanged(newValue: String) {
        userName.value = newValue
    }
    var email = mutableStateOf("")
        private set
    var phoneNumber = mutableStateOf("")
        private set
    var avatarImg = mutableStateOf("")
        private set
    var fullName = mutableStateOf("")
        private set


}