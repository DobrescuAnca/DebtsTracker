package com.debts.debtstracker.data.repository

import com.debts.debtstracker.data.ResponseStatus
import com.debts.debtstracker.data.network.model.*
import com.debts.debtstracker.data.pagination.PagedListServerModel

interface RepositoryInterface {

    suspend fun login(username: String, password: String): ResponseStatus<AuthModel>

    suspend fun signUp(model: RegisterModel): ResponseStatus<*>

    suspend fun getUserTotalDebts(): ResponseStatus<HomeTotalDebtsModel>

    suspend fun getFriendList(): ResponseStatus<PagedListServerModel<UserModel>>

    suspend fun addDebt(debtModel: AddDebtModel): ResponseStatus<*>


    suspend fun getUserProfile(id: String): ResponseStatus<UserModel>

    suspend fun getLoggedUserProfile(): ResponseStatus<UserModel>

    suspend fun updateProfile(profile: UpdateProfileModel): ResponseStatus<UserModel>

    suspend fun updatePassword(passwordModel: UpdatePasswordModel): ResponseStatus<*>

    suspend fun sendProfileAction(action: ProfileActionEnum, id: String): ResponseStatus<UserModel>

    suspend fun logout(): ResponseStatus<*>
}