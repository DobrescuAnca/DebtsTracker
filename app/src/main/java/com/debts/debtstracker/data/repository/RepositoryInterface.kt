package com.debts.debtstracker.data.repository

import androidx.lifecycle.LiveData
import com.debts.debtstracker.data.ResponseStatus
import com.debts.debtstracker.data.network.model.*

interface RepositoryInterface {

    val userProfile: LiveData<UserModel>

    val friendList: LiveData<List<UserModel>>

    val totalDebts: LiveData<HomeTotalDebtsModel>

    suspend fun login(username: String, password: String): ResponseStatus<*>

    suspend fun signUp(model: RegisterModel): ResponseStatus<*>

    suspend fun getUserTotalDebts(): ResponseStatus<*>

    suspend fun getFriendList(): ResponseStatus<*>

    suspend fun addDebt(debtModel: AddDebtModel): ResponseStatus<*>


    suspend fun getUserProfile(id: String): ResponseStatus<*>

    suspend fun getLoggedUserProfile(): ResponseStatus<*>

    suspend fun sendProfileAction(action: ProfileActionEnum, id: String): ResponseStatus<*>

    suspend fun logout(): ResponseStatus<*>
}