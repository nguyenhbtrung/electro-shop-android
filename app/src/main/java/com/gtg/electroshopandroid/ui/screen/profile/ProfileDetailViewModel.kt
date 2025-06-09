package com.gtg.electroshopandroid.ui.screen.profile

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.gtg.electroshopandroid.ElectroShopApplication
import com.gtg.electroshopandroid.convertBaseUrl
import com.gtg.electroshopandroid.data.model.EditProfileRequest
import com.gtg.electroshopandroid.data.repository.ProfileRepository
import com.gtg.electroshopandroid.ui.screen.product.ProductViewModel
import kotlinx.coroutines.launch

class ProfileDetailViewModel(
    private val profileRepository: ProfileRepository
) : ViewModel() {
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

    fun loadProfile() {
        viewModelScope.launch {
            try {
                val profile = profileRepository.getProfile()
                Log.d("Profile", "Avatar URL: ${convertBaseUrl(profile.avatarImg)}")
                userName.value = profile.userName
                email.value = profile.email
                fullName.value = profile.fullName
                phoneNumber.value = profile.phoneNumber
                address.value = profile.address
                avatarImg.value = convertBaseUrl(profile.avatarImg)
            } catch (e: Exception) {
                // Xử lý lỗi nếu cần
            }
        }

    }

    fun onChangeClick(onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                val request = EditProfileRequest(
                    fullName = fullName.value,
                    phoneNumber = phoneNumber.value,
                    address = address.value,
                    avatarImg = convertBaseUrl(avatarImg.value)
                )
                profileRepository.updateUser(request)
                // Gọi lại getProfile sau khi update
                loadProfile()
                onSuccess()
            } catch (e: Exception) {
                // Xử lý lỗi nếu cần
            }
        }
    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as ElectroShopApplication)
                val profileRepository = application.container.profileRepository
                ProfileDetailViewModel(profileRepository = profileRepository)
            }
        }
    }
}

