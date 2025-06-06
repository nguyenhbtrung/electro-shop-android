package com.gtg.electroshopandroid.ui.screen.profile

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class ProfileDetailViewModel() : ViewModel() {
    var userName = mutableStateOf("")
        private set

    var email = mutableStateOf("")
        private set

    var phoneNumber = mutableStateOf("")
        private set
    fun onPhoneNumberChanged(newValue: String) {
        phoneNumber.value = newValue
    }

    var avatarImg = mutableStateOf("")
        private set
    fun onAvatarImgChanged(newValue: String) {
        avatarImg.value = newValue
    }

    var fullName = mutableStateOf("")
        private set
    fun onFullNameChanged(newValue: String) {
        fullName.value = newValue
    }

    var address = mutableStateOf("")
        private set
    fun onAddressChange(newValue: String){
        address.value = newValue
    }

    fun onChangeClick(){}
}