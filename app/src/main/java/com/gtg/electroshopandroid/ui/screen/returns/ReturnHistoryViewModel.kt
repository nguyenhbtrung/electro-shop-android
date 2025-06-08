package com.gtg.electroshopandroid.ui.screen.returns

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gtg.electroshopandroid.data.model.ReturnDto
import com.gtg.electroshopandroid.data.repository.ReturnHistoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ReturnHistoryViewModel(
    private val returnHistoryRepository: ReturnHistoryRepository
) : ViewModel() {

    private val _returns = MutableStateFlow<List<ReturnDto>>(emptyList())
    val returns: StateFlow<List<ReturnDto>> = _returns

    fun loadReturns() {
        viewModelScope.launch {
            _returns.value = returnHistoryRepository.getAllReturns()
        }
    }
}
