package com.debts.debtstracker.data.repository

import com.debts.debtstracker.data.ResponseStatus
import com.debts.debtstracker.data.local.LocalPreferencesInterface
import com.debts.debtstracker.data.network.api.NoNetworkConnectionException
import com.debts.debtstracker.data.network.model.AuthModel
import com.debts.debtstracker.data.network.model.RegisterModel
import com.debts.debtstracker.injection.ApiServiceObject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class Repository(
    private val apiService: ApiServiceObject,
    private val sharedPrefs: LocalPreferencesInterface
): RepositoryInterface {

    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    override suspend fun login(username: String, password: String): ResponseStatus<*>{
        var response: Response<AuthModel>? = null

        withContext(ioDispatcher){
            try {
                response = apiService.RETROFIT_SERVICE.login(
                    username = username,
                    password = password
                )
            } catch (e: Exception){
                throw  NoNetworkConnectionException()
            }
        }

        response?.let {
            return if(it.isSuccessful){
                ResponseStatus.Success(it.body())
            } else {
                ResponseStatus.Error(
                    code = it.code(),
                    errorObject = it.message()
                )
            }
        }
        return ResponseStatus.None
    }

    override suspend fun signUp(model: RegisterModel): ResponseStatus<*>{
        var response: Response<Any>? = null

        withContext(ioDispatcher){
            try {
                response = apiService.RETROFIT_SERVICE.register(
                    model
                )
            } catch (e: Exception){
                throw  NoNetworkConnectionException()
            }
        }

        response?.let {
            return if(it.isSuccessful){
                ResponseStatus.Success(it.body())
            } else {
                ResponseStatus.Error(
                    code = it.code(),
                    errorObject = it.message()
                )
            }
        }
        return ResponseStatus.None
    }

}