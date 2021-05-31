package com.debts.debtstracker.data.network.model

data class HomeCardModel(
    val id: String,
    val sum: Double,
    val description: String,
    val creationDate: Long,
    val paidDate: Long?,
    val status: DebtStatus,
    val isUserLender: Boolean
)

data class AddDebtModel(
    val sum: Double,
    val description: String,
    val date: String,
    val isUserLender: Boolean
)

data class TotalsModel(
    val totalLent: Double,
    val totalBorrowed: Double
)

enum class DebtStatus{
    UNPAID, PAID, DELETED
}