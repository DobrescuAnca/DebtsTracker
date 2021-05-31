package com.debts.debtstracker.data.network.model

data class HomeCardModel(
    val id: String,
    val sum: Float,
    val description: String,
    val creationDate: Long,
    val status: DebtStatus,
    val isUserLender: Boolean
)

data class AddDebtModel(
    val sum: Float,
    val description: String,
    val isUserLender: Boolean
)

enum class DebtStatus{
    IN_PROGRESS, PAID, DELETED
}