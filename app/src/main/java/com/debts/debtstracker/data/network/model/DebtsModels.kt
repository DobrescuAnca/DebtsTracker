package com.debts.debtstracker.data.network.model

data class HomeCardModel(
    val id: String,
    val sum: Float,
    val description: String,
    val creationDate: Long,
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