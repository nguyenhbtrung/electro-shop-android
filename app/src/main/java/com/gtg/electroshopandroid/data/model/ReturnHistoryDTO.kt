package com.gtg.electroshopandroid.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReturnProductDto(
    @SerialName("productId")
    val productId: Int = 0,
    @SerialName("name")
    val name: String = "",
    @SerialName("image")
    val image: String? = null,
    @SerialName("returnQuantity")
    val returnQuantity: Int = 0
)

@Serializable
data class ReturnDto(
    @SerialName("returnId")
    val returnId: Int = 0,
    @SerialName("status")
    val status: String = "",
    @SerialName("returnMethod")
    val returnMethod: String = "",
    @SerialName("timeStamp")
    val timeStamp: String = "",
    @SerialName("returnProducts")
    val returnProducts: List<ReturnProductDto> = emptyList()
)

@Serializable
data class ReturnDetailDto(
    @SerialName("returnId")
    val returnId: Int,

    @SerialName("orderId")
    val orderId: Int,

    @SerialName("reason")
    val reason: String,

    @SerialName("detail")
    val detail: String? = null,

    @SerialName("status")
    val status: String,

    @SerialName("returnMethod")
    val returnMethod: String,

    @SerialName("adminComment")
    val adminComment: String? = null,

    @SerialName("createdAt")
    val createdAt: String,

    @SerialName("returnHistories")
    val returnHistories: List<ReturnHistoryDto> = emptyList(),

    @SerialName("returnProducts")
    val returnProducts: List<ReturnProductDetailDto> = emptyList()
)

@Serializable
data class ReturnHistoryDto(
    @SerialName("status")
    val status: String,

    @SerialName("changedAt")
    val changedAt: String
)

@Serializable
data class ReturnProductDetailDto(
    @SerialName("productId")
    val productId: Int,

    @SerialName("name")
    val name: String,

    @SerialName("image")
    val image: String,

    @SerialName("returnQuantity")
    val returnQuantity: Int
)

fun ReturnDetailDto.getStatusDisplayName(): String {
    return when(this.status.lowercase()) {
        "pending" -> "Chờ xử lý"
        "approved" -> "Đã duyệt"
        "processing" -> "Đang xử lý"
        "rejected" -> "Từ chối"
        "completed" -> "Hoàn thành"
        "canceled" -> "Đã hủy"
        else -> this.status
    }
}

fun ReturnDetailDto.getWorkflowPath(): WorkflowPath {
    return when(this.status.lowercase()) {
        "pending" -> WorkflowPath.PENDING
        "approved" -> WorkflowPath.APPROVED
        "processing" -> WorkflowPath.PROCESSING
        "rejected" -> WorkflowPath.REJECTED
        "completed" -> WorkflowPath.COMPLETED
        "canceled" -> WorkflowPath.CANCELED
        else -> WorkflowPath.PENDING
    }
}

enum class WorkflowPath {
    PENDING,    // Chặng 1
    APPROVED,   // Chặng 2 - path tích cực
    REJECTED,   // Chặng 2 - path tiêu cực
    PROCESSING, // Chặng 3
    COMPLETED,  // Chặng 4 - kết thúc tích cực
    CANCELED    // Chặng 4 - kết thúc tiêu cực
}

fun ReturnDetailDto.getReturnMethodDisplayName(): String {
    return when(this.returnMethod.lowercase()) {
        "refund" -> "Hoàn tiền"
        "exchange" -> "Đổi sản phẩm"
        "repair" -> "Sửa chữa"
        else -> this.returnMethod
    }
}

fun ReturnDetailDto.getFormattedDate(): String {
    return this.createdAt.take(10)
}

