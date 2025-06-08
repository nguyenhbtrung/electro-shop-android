package com.gtg.electroshopandroid.ui.screen.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gtg.electroshopandroid.data.model.*
import com.gtg.electroshopandroid.data.repository.OrderHistoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CreateReturnViewModel(
    private val orderHistoryRepository: OrderHistoryRepository
) : ViewModel() {

    private val _formState = MutableStateFlow(CreateReturnFormState())
    val formState: StateFlow<CreateReturnFormState> = _formState

    fun initializeForm(orderId: Int, orderDto: OrderDto) {
        val returnableItems = orderDto.orderItems.map { item ->
            ReturnableItem(
                orderItemId = item.orderItemId,
                productName = item.productName,
                productImage = item.productImage,
                maxQuantity = item.quantity,
                returnQuantity = 0,
                isSelected = false
            )
        }

        _formState.value = _formState.value.copy(
            orderId = orderId,
            orderItems = returnableItems
        )
    }

    fun updateDetail(detail: String) {
        _formState.value = _formState.value.copy(detail = detail)
        clearError("detail")
    }

    fun updateReturnMethod(method: Int) {
        _formState.value = _formState.value.copy(returnMethod = method)
        clearError("returnMethod")
    }

    fun toggleItemSelection(orderItemId: Int) {
        val updatedItems = _formState.value.orderItems.map { item ->
            if (item.orderItemId == orderItemId) {
                item.copy(
                    isSelected = !item.isSelected,
                    returnQuantity = if (!item.isSelected) 1 else 0
                )
            } else item
        }
        _formState.value = _formState.value.copy(orderItems = updatedItems)
        clearError("returnItems")
    }

    fun updateReturnQuantity(orderItemId: Int, quantity: Int) {
        val updatedItems = _formState.value.orderItems.map { item ->
            if (item.orderItemId == orderItemId) {
                item.copy(returnQuantity = quantity.coerceIn(0, item.maxQuantity))
            } else item
        }
        _formState.value = _formState.value.copy(orderItems = updatedItems)
    }

    fun addEvidenceImage(imagePath: String) {
        val currentImages = _formState.value.evidenceImages.toMutableList()
        currentImages.add(imagePath)
        _formState.value = _formState.value.copy(evidenceImages = currentImages)
    }

    fun removeEvidenceImage(imagePath: String) {
        val currentImages = _formState.value.evidenceImages.toMutableList()
        currentImages.remove(imagePath)
        _formState.value = _formState.value.copy(evidenceImages = currentImages)
    }

    private fun validateForm(): Boolean {
        val errors = mutableMapOf<String, String>()
        val state = _formState.value

        if (state.detail.isBlank()) {
            errors["detail"] = "Vui lòng nhập mô tả chi tiết"
        }

        val selectedItems = state.orderItems.filter { it.isSelected && it.returnQuantity > 0 }
        if (selectedItems.isEmpty()) {
            errors["returnItems"] = "Vui lòng chọn ít nhất một sản phẩm để hoàn trả"
        }

        _formState.value = _formState.value.copy(errors = errors)
        return errors.isEmpty()
    }

    fun submitReturnRequest() {
        if (!validateForm()) return

        viewModelScope.launch {
            _formState.value = _formState.value.copy(isLoading = true)

            try {
                val request = CreateReturnRequest(
                    orderId = _formState.value.orderId,
                    reason = _formState.value.reason,
                    detail = _formState.value.detail,
                    returnMethod = _formState.value.returnMethod,
                    returnItems = _formState.value.orderItems
                        .filter { it.isSelected && it.returnQuantity > 0 }
                        .map {
                            ReturnItemRequest(
                                orderItemId = it.orderItemId,
                                returnQuantity = it.returnQuantity
                            )
                        },
                    evidenceImages = _formState.value.evidenceImages
                )

                val success = orderHistoryRepository.createReturnRequest(request)
                if (success) {
                    _formState.value = _formState.value.copy(
                        submitSuccess = true,
                        isLoading = false
                    )
                } else {
                    _formState.value = _formState.value.copy(
                        errors = mapOf("submit" to "Không thể gửi yêu cầu hoàn trả"),
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _formState.value = _formState.value.copy(
                    errors = mapOf("submit" to (e.message ?: "Có lỗi xảy ra")),
                    isLoading = false
                )
            }
        }
    }

    private fun clearError(field: String) {
        val currentErrors = _formState.value.errors.toMutableMap()
        currentErrors.remove(field)
        _formState.value = _formState.value.copy(errors = currentErrors)
    }
}
