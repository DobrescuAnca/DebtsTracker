package com.debts.debtstracker.data.repository

import com.debts.debtstracker.data.ResponseStatus
import com.debts.debtstracker.data.network.model.RegisterModel

interface RepositoryInterface {

    suspend fun login(username: String, password: String): ResponseStatus<*>

    suspend fun signUp(model: RegisterModel): ResponseStatus<*>
}