package com.debts.debtstracker.data.repository

import com.debts.debtstracker.data.NetworkState
import com.debts.debtstracker.data.ResponseStatus
import com.debts.debtstracker.data.network.model.*
import com.debts.debtstracker.data.pagination.PagedListServerModel
import com.debts.debtstracker.injection.ApiServiceObject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class Repository(
    private val apiService: ApiServiceObject
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
            response = apiService.RETROFIT_SERVICE.login(
                    username = username,
                    password = password
                )
        }
        return getResponseCall(response)
    }

    override suspend fun signUp(model: RegisterModel): ResponseStatus<NetworkState>{
        var response: Response<NetworkState>

        withContext(ioDispatcher){
            response = apiService.RETROFIT_SERVICE.register(model)
        }
        return getResponseCall(response)
    }

    override suspend fun getFriendList(): ResponseStatus<PagedListServerModel<UserModel>> {
        var response: Response<PagedListServerModel<UserModel>>

        withContext(ioDispatcher){
            response = apiService.RETROFIT_SERVICE.getFriendsList()
        }
        return getResponseCall(response)
    }

    override suspend fun addDebt(debtModel: AddDebtModel): ResponseStatus<NetworkState> {
        var response: Response<NetworkState>

        withContext(ioDispatcher){
            response = apiService.RETROFIT_SERVICE.addDebt(debtModel)
        }
        return getResponseCall(response)
    }

    override suspend fun getUserProfile(id: String): ResponseStatus<UserModel>{
        var response: Response<UserModel>

        withContext(ioDispatcher){
            response = apiService.RETROFIT_SERVICE.getUserProfile(id)
        }
        return getResponseCall(response)
    }


    override suspend fun getLoggedUserProfile(): ResponseStatus<UserModel>{
        var response: Response<UserModel>

        withContext(ioDispatcher){
            response = apiService.RETROFIT_SERVICE.getLoggedUserProfile()
        }
        return getResponseCall(response)
    }

    override suspend fun updateProfile(profile: UpdateProfileModel): ResponseStatus<UserModel>{
        var response: Response<UserModel>

        withContext(ioDispatcher){
            response = apiService.RETROFIT_SERVICE.updateProfile(profile)
        }
        return getResponseCall(response)
    }

    override suspend fun updatePassword(passwordModel: UpdatePasswordModel): ResponseStatus<NetworkState>{
        var response: Response<NetworkState>

        withContext(ioDispatcher){
            response = apiService.RETROFIT_SERVICE.updatePassword(passwordModel)
        }

        return getResponseCall(response)
    }

    override suspend fun sendProfileAction(action: ProfileActionEnum, id: String): ResponseStatus<UserModel>{
        var response: Response<UserModel>

        withContext(ioDispatcher){
            response = apiService.RETROFIT_SERVICE.sendProfileAction(action, id)
        }
        return getResponseCall(response)
    }

    override suspend fun getUserTotalDebts(): ResponseStatus<HomeTotalDebtsModel>{
        val response: Response<HomeTotalDebtsModel>

        withContext(ioDispatcher){
            response = apiService.RETROFIT_SERVICE.getUserTotalDebts()
        }
        return getResponseCall(response)
    }

    override suspend fun logout(): ResponseStatus<NetworkState> {
        var response: Response<NetworkState>

        withContext(ioDispatcher){
            response = apiService.RETROFIT_SERVICE.logout()
        }

        return getResponseCall(response)
    }

}