package com.gtg.electroshopandroid.ui.screen.returns

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gtg.electroshopandroid.data.model.ReturnDetailDto
import com.gtg.electroshopandroid.data.repository.ReturnHistoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ReturnDetailViewModel(
    private val returnHistoryRepository: ReturnHistoryRepository
) : ViewModel() {

    private val _returnDetail = MutableStateFlow<ReturnDetailDto?>(null)
    val returnDetail: StateFlow<ReturnDetailDto?> = _returnDetail

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadReturnDetail(returnId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val detail = returnHistoryRepository.getReturnDetail(returnId)
                _returnDetail.value = detail
            } catch (e: Exception) {
                _error.value = e.message ?: "Có lỗi xảy ra khi tải chi tiết hoàn trả"
                _returnDetail.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}
