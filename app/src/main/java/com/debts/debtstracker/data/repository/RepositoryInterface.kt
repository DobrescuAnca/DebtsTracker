package com.debts.debtstracker.data.repository

import androidx.paging.PagingData
import com.debts.debtstracker.data.ResponseStatus
import com.debts.debtstracker.data.network.model.*
import kotlinx.coroutines.flow.Flow

interface RepositoryInterface {

    suspend fun login(username: String, password: String): ResponseStatus<SingleValueModel>

    suspend fun addDebt(debtModel: AddDebtModel): ResponseStatus<*>

    suspend fun getUserProfile(): ResponseStatus<ProfileModel>

    suspend fun getTotals(): ResponseStatus<TotalsModel>

    suspend fun equate(): ResponseStatus<Any>

    //pagination
    fun getHomeCardsStream(filter: String): Flow<PagingData<HomeCardModel>>
}