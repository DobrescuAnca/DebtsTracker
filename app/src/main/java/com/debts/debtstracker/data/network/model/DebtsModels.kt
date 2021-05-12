package com.debts.debtstracker.data.network.model

data class FriendDebtModel(
    val id: String,
    val sum: Float,
    val description: String,
    val creationDate: Long,
    val status: DebtStatus,
    val lenderId: String,
    val borrowerId: String
)

data class AddDebtModel(
    val borrowerSumList: List<BorrowerDebtModel>,
    val description: String,
    val lenderId: String
)

data class BorrowerDebtModel(
    val borrowerId: String,
    val sum: Float
)

enum class DebtStatus {
    PENDING_ACCEPTANCE,
    PENDING_PAYMENT_CONFIRMATION,
    ACCEPTED,
    DECLINED,
    PAYMENT_CONFIRMED,
    PAYMENT_DENIED
}

enum class DebtWithUserStatus {
    ALL, BORROWED, LENT
}