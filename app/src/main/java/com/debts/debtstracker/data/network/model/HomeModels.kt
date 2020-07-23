package com.debts.debtstracker.data.network.model

data class HomeCardModel(
    val id: String,
    val creationDate: Long,
    val userId: String,
    val homeCardType: HomeCardTypeEnum,
    val text: String,
    val otherUserId: String,
    val debtId: String? = "",
    val sum: Double? = 0.0
)

enum class HomeCardTypeEnum {
    FRIEND_REQUEST_SENT,
    FRIEND_REQUEST_RECEIVED,
    FRIEND_REQUEST_ACCEPTED,
    DEBT_REQUEST_SENT,
    DEBT_REQUEST_RECEIVED,
    DEBT_ACCEPTED,
    DEBT_DECLINED,
    DEBT_PAID
}