package com.debts.debtstracker.data.network.model

data class HomeCardModel(
    val id: Int,
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
    val creationDate: Long,
    val isUserLender: Boolean
)

data class ListModel<T>(
    val content: List<T>,
    val currentPage: Int,
    val pageSize: Int,
    val totalPages: Int,
    val totalResults: Int
)

data class TotalsModel(
    val totalLent: Double,
    val totalBorrowed: Double
)

enum class DebtStatus{
    UNPAID, PAID, DELETED
}