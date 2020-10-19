package com.debts.debtstracker.data.network.model

data class FriendDebtModel(
    val id: Int,
    val sum: Float,
    val description: String,
    val creation: String,
    val status: DebtStatus
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
    PENDING, ACCEPTED, DECLINED, PAYED
}