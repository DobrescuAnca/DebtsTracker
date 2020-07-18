package com.debts.debtstracker.data.repository

import androidx.lifecycle.LiveData
import com.debts.debtstracker.data.ResponseStatus
import com.debts.debtstracker.data.network.model.ProfileActionEnum
import com.debts.debtstracker.data.network.model.RegisterModel
import com.debts.debtstracker.data.network.model.UserModel

interface RepositoryInterface {

    val userProfile: LiveData<UserModel>

    suspend fun login(username: String, password: String): ResponseStatus<*>

    suspend fun signUp(model: RegisterModel): ResponseStatus<*>


    suspend fun getUserProfile(id: String): ResponseStatus<*>

    suspend fun sendProfileAction(action: ProfileActionEnum, id: String): ResponseStatus<*>
}