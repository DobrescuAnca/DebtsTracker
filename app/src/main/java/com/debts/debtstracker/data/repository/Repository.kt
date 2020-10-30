package com.debts.debtstracker.data.repository

import com.debts.debtstracker.data.NetworkState
import com.debts.debtstracker.data.ResponseStatus
import com.debts.debtstracker.data.local.LocalPreferencesInterface
import com.debts.debtstracker.data.network.api.NoNetworkConnectionException
import com.debts.debtstracker.data.network.model.*
import com.debts.debtstracker.data.pagination.PagedListServerModel
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

    private fun <T>getResponseCall(response: Response<T>): ResponseStatus<T>{
        if(response.isSuccessful) {
            response.body()?.let {
                return ResponseStatus.Success(it)
            }
            return ResponseStatus.None
        }
        else {
            return ResponseStatus.Error(
                code = response.code(),
                errorObject = response.message()
            )
        }
    }

    override suspend fun login(username: String, password: String): ResponseStatus<AuthModel>{
        var response: Response<AuthModel>

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
        return getResponseCall(response)
    }

    override suspend fun signUp(model: RegisterModel): ResponseStatus<NetworkState>{
        var response: Response<NetworkState>

        withContext(ioDispatcher){
            try {
                response = apiService.RETROFIT_SERVICE.register(model)
            } catch (e: Exception){
                throw  NoNetworkConnectionException()
            }
        }
        return getResponseCall(response)
    }

    override suspend fun getFriendList(): ResponseStatus<PagedListServerModel<UserModel>> {
        var response: Response<PagedListServerModel<UserModel>>

        withContext(ioDispatcher){
            try{
                response = apiService.RETROFIT_SERVICE.getFriendsList()
            } catch (e: Exception){
                throw NoNetworkConnectionException()
            }
        }
        return getResponseCall(response)
    }

    override suspend fun addDebt(debtModel: AddDebtModel): ResponseStatus<NetworkState> {
        var response: Response<NetworkState>

        withContext(ioDispatcher){
            try{
                response = apiService.RETROFIT_SERVICE.addDebt(debtModel)
            } catch (e: Exception){
                throw NoNetworkConnectionException()
            }
        }
        return getResponseCall(response)
    }

    override suspend fun getUserProfile(id: String): ResponseStatus<UserModel>{
        var response: Response<UserModel>

        withContext(ioDispatcher){
            try {
                response = apiService.RETROFIT_SERVICE.getUserProfile(id)
            } catch (e: Exception){
                throw  NoNetworkConnectionException()
            }
        }
        return getResponseCall(response)
    }


    override suspend fun getLoggedUserProfile(): ResponseStatus<UserModel>{
        var response: Response<UserModel>

        withContext(ioDispatcher){
            try {
                response = apiService.RETROFIT_SERVICE.getLoggedUserProfile()
            } catch (e: Exception){
                throw  NoNetworkConnectionException()
            }
        }
        return getResponseCall(response)
    }

    override suspend fun updateProfile(profile: UpdateProfileModel): ResponseStatus<UserModel>{
        var response: Response<UserModel>

        withContext(ioDispatcher){
            try {
                response = apiService.RETROFIT_SERVICE.updateProfile(profile)
            } catch (e: Exception){
                throw  NoNetworkConnectionException()
            }
        }
        return getResponseCall(response)
    }

    override suspend fun updatePassword(passwordModel: UpdatePasswordModel): ResponseStatus<NetworkState>{
        var response: Response<NetworkState>

        withContext(ioDispatcher){
            try {
                response = apiService.RETROFIT_SERVICE.updatePassword(passwordModel)
            } catch (e: Exception){
                throw  NoNetworkConnectionException()
            }
        }

        return getResponseCall(response)
    }

    override suspend fun sendProfileAction(action: ProfileActionEnum, id: String): ResponseStatus<UserModel>{
        var response: Response<UserModel>

        withContext(ioDispatcher){
            try {
                response = apiService.RETROFIT_SERVICE.sendProfileAction(action, id)
            } catch (e: Exception){
                throw  NoNetworkConnectionException()
            }
        }
        return getResponseCall(response)
    }

    override suspend fun getUserTotalDebts(): ResponseStatus<HomeTotalDebtsModel>{
        val response: Response<HomeTotalDebtsModel>

        withContext(ioDispatcher){
            try {
                response = apiService.RETROFIT_SERVICE.getUserTotalDebts()
            } catch (e: Exception){
                throw NoNetworkConnectionException()
            }
        }
        return getResponseCall(response)
    }

    override suspend fun logout(): ResponseStatus<NetworkState> {
        var response: Response<NetworkState>

        withContext(ioDispatcher){
            try {
                response = apiService.RETROFIT_SERVICE.logout()
            } catch (e: Exception){
                throw  NoNetworkConnectionException()
            }
        }

        return getResponseCall(response)
    }

}