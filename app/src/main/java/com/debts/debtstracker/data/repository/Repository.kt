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

    private val _userProfile = MutableLiveData<UserModel>()
    override val userProfile: LiveData<UserModel> = _userProfile

    private val _friendList = MutableLiveData<List<UserModel>>()
    override val friendList: LiveData<List<UserModel>> = _friendList

    private val _totalDebts = MutableLiveData<HomeTotalDebtsModel>()
    override val totalDebts: LiveData<HomeTotalDebtsModel> = _totalDebts

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

    override suspend fun getFriendList(): ResponseStatus<*> {
        var response: Response<PagedListServerModel<UserModel>>? = null

        withContext(ioDispatcher){
            try{
                response = apiService.RETROFIT_SERVICE.getFriendsList()
            } catch (e: Exception){
                throw NoNetworkConnectionException()
            }
        }

        response?.let {
            return if(it.isSuccessful){
                _friendList.value = it.body()?.content
                ResponseStatus.Success(friendList.value)
            } else {
                ResponseStatus.Error(
                    code = it.code(),
                    errorObject = it.message()
                )
            }
        }
        return ResponseStatus.None
    }

    override suspend fun addDebt(debtModel: AddDebtModel): ResponseStatus<*> {
        var responseStatus: Response<NetworkState>? = null

        withContext(ioDispatcher){
            try{
                responseStatus = apiService.RETROFIT_SERVICE.addDebt(debtModel)
            } catch (e: Exception){
                throw NoNetworkConnectionException()
            }
        }

        responseStatus?.let {
            return if(it.isSuccessful)
                ResponseStatus.Success(it.body())
            else {
                ResponseStatus.Error(
                    code = it.code(),
                    errorObject = it.message()
                )
            }
        }

        return ResponseStatus.None
    }

    override suspend fun getUserProfile(id: String): ResponseStatus<*>{
        var response: Response<UserModel>? = null

        withContext(ioDispatcher){
            try {
                response = apiService.RETROFIT_SERVICE.getUserProfile(id)
            } catch (e: Exception){
                throw  NoNetworkConnectionException()
            }
        }

        response?.let {
            return if(it.isSuccessful){
                _userProfile.value = it.body()
                ResponseStatus.Success(userProfile.value)
            } else {
                ResponseStatus.Error(
                    code = it.code(),
                    errorObject = it.message()
                )
            }
        }
        return ResponseStatus.None
    }


    override suspend fun getLoggedUserProfile(): ResponseStatus<*>{
        var response: Response<UserModel>? = null

        withContext(ioDispatcher){
            try {
                response = apiService.RETROFIT_SERVICE.getLoggedUserProfile()
            } catch (e: Exception){
                throw  NoNetworkConnectionException()
            }
        }

        response?.let {
            return if(it.isSuccessful){
                _userProfile.value = it.body()
                ResponseStatus.Success(userProfile.value)
            } else {
                ResponseStatus.Error(
                    code = it.code(),
                    errorObject = it.message()
                )
            }
        }
        return ResponseStatus.None
    }

    override suspend fun updateProfile(profile: UpdateProfileModel): ResponseStatus<*>{
        var response: Response<NetworkState>? = null

        withContext(ioDispatcher){
            try {
                response = apiService.RETROFIT_SERVICE.updateProfile(profile)
            } catch (e: Exception){
                throw  NoNetworkConnectionException()
            }
        }

        response?.let {
            return if(it.isSuccessful)
                ResponseStatus.Success(it.body())
            else {
                ResponseStatus.Error(
                    code = it.code(),
                    errorObject = it.message()
                )
            }
        }
        return ResponseStatus.None
    }

    override suspend fun updatePassword(passwordModel: UpdatePasswordModel): ResponseStatus<*>{
        var response: Response<NetworkState>? = null

        withContext(ioDispatcher){
            try {
                response = apiService.RETROFIT_SERVICE.updatePassword(passwordModel)
            } catch (e: Exception){
                throw  NoNetworkConnectionException()
            }
        }

        response?.let {
            return if(it.isSuccessful)
                ResponseStatus.Success(it.body())
            else {
                ResponseStatus.Error(
                    code = it.code(),
                    errorObject = it.message()
                )
            }
        }
        return ResponseStatus.None
    }

    override suspend fun sendProfileAction(action: ProfileActionEnum, id: String): ResponseStatus<*>{
        var response: Response<UserModel>? = null

        withContext(ioDispatcher){
            try {
                response = apiService.RETROFIT_SERVICE.sendProfileAction(action, id)
            } catch (e: Exception){
                throw  NoNetworkConnectionException()
            }
        }

        response?.let {
            return if(it.isSuccessful){
                _userProfile.value = it.body()
                ResponseStatus.Success(userProfile.value)
            } else {
                ResponseStatus.Error(
                    code = it.code(),
                    errorObject = it.message()
                )
            }
        }
        return ResponseStatus.None
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

        return if(response.isSuccessful){
            _totalDebts.value = response.body()
            ResponseStatus.Success(totalDebts.value)
        } else {
            ResponseStatus.Error(
                code = response.code(),
                errorObject = response.message()
            )
        }

    }

    override suspend fun logout(): ResponseStatus<*> {
        var response: Response<NetworkState>? = null

        withContext(ioDispatcher){
            try {
                response = apiService.RETROFIT_SERVICE.logout()
            } catch (e: Exception){
                throw  NoNetworkConnectionException()
            }
        }

        response?.let {
            return if(it.isSuccessful)
                ResponseStatus.Success(it.body())
            else {
                ResponseStatus.Error(
                    code = it.code(),
                    errorObject = it.message()
                )
            }
        }
        return ResponseStatus.None
    }

}