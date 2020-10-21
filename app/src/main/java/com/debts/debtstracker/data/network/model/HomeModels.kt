package com.debts.debtstracker.data.network.model

data class HomeCardModel(
    val id: String,
    val userId: String,
    val otherUserId: String,
    val otherUserProfilePictureUrl: String,
    val debtId: String? = "",
    val sum: Double? = 0.0,
    val text: String,
    val creationDate: Long,
    val homeCardType: HomeCardTypeEnum
)

data class HomeTotalDebtsModel(
    val totalBorrowed: Float,
    val totalLent: Float
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

enum class HomeCardFilterTypeEnum {
    DEBTS, FRIENDS, ALL
}