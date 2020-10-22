package com.debts.debtstracker.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    private val _friendList = MutableLiveData<List<UserModel>>()
    override val friendList: LiveData<List<UserModel>> = _friendList

    private fun <T>getResponseCall(response: Response<T>): ResponseStatus<*>{
        return if(response.isSuccessful){
            ResponseStatus.Success(response.body())
        } else {
            ResponseStatus.Error(
                code = response.code(),
                errorObject = response.message()
            )
        }
    }

    override suspend fun login(username: String, password: String): ResponseStatus<*>{
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

    override suspend fun signUp(model: RegisterModel): ResponseStatus<*>{
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

    override suspend fun getFriendList(): ResponseStatus<*> {
        var response: Response<PagedListServerModel<UserModel>>

        withContext(ioDispatcher){
            try{
                response = apiService.RETROFIT_SERVICE.getFriendsList()
            } catch (e: Exception){
                throw NoNetworkConnectionException()
            }
        }

        //todo get rid of _friendList on AddView modification
        return if(response.isSuccessful){
                _friendList.value = response.body()?.content
                ResponseStatus.Success(response.body()?.content)
            } else {
                ResponseStatus.Error(
                    code = response.code(),
                    errorObject = response.message()
                )
            }
    }

    override suspend fun addDebt(debtModel: AddDebtModel): ResponseStatus<*> {
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

    override suspend fun getUserProfile(id: String): ResponseStatus<*>{
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


    override suspend fun getLoggedUserProfile(): ResponseStatus<*>{
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

    override suspend fun updateProfile(profile: UpdateProfileModel): ResponseStatus<*>{
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

    override suspend fun updatePassword(passwordModel: UpdatePasswordModel): ResponseStatus<*>{
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

    override suspend fun sendProfileAction(action: ProfileActionEnum, id: String): ResponseStatus<*>{
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

    override suspend fun getUserTotalDebts(): ResponseStatus<*>{
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

    override suspend fun logout(): ResponseStatus<*> {
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