package com.debts.debtstracker.data.repository

import androidx.paging.PagingData
import com.debts.debtstracker.data.ResponseStatus
import com.debts.debtstracker.data.network.model.AddDebtModel
import com.debts.debtstracker.data.network.model.HomeCardModel
import kotlinx.coroutines.flow.Flow

interface RepositoryInterface {

    suspend fun login(username: String, password: String): ResponseStatus<String>

    suspend fun addDebt(debtModel: AddDebtModel): ResponseStatus<*>

    //pagination
    fun getHomeCardsStream(filter: String): Flow<PagingData<HomeCardModel>>
}