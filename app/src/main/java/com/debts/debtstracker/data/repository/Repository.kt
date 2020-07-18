package com.debts.debtstracker.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.debts.debtstracker.data.ResponseStatus
import com.debts.debtstracker.data.local.LocalPreferencesInterface
import com.debts.debtstracker.data.network.api.NoNetworkConnectionException
import com.debts.debtstracker.data.network.model.AuthModel
import com.debts.debtstracker.data.network.model.ProfileActionEnum
import com.debts.debtstracker.data.network.model.RegisterModel
import com.debts.debtstracker.data.network.model.UserModel
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

}