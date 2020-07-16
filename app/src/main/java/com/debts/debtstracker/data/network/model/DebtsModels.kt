package com.debts.debtstracker.data.network.model

data class FriendDebtModel(
    val id: Int,
    val sum: Float,
    val description: String,
    val creation: String,
    val status: DebtStatus
)

enum class DebtStatus {
    PENDING, ACCEPTED, DECLINED, PAYED
}