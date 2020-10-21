package com.debts.debtstracker.ui.main.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.debts.debtstracker.data.ErrorCode
import com.debts.debtstracker.data.ResponseStatus
import com.debts.debtstracker.data.network.api.NoNetworkConnectionException
import com.debts.debtstracker.data.network.model.ProfileActionEnum
import com.debts.debtstracker.data.network.model.UpdatePasswordModel
import com.debts.debtstracker.data.network.model.UpdateProfileModel
import com.debts.debtstracker.data.repository.RepositoryInterface
import com.debts.debtstracker.ui.base.BaseViewModel
import com.debts.debtstracker.util.Event
import kotlinx.coroutines.launch

class ProfileViewModel(private val repositoryInterface: RepositoryInterface): BaseViewModel() {

    val userProfile = repositoryInterface.userProfile

    private var _logout: MutableLiveData<Event<ResponseStatus<*>>> = MutableLiveData(Event(ResponseStatus.None))
    val logout: LiveData<Event<ResponseStatus<*>>> = _logout

    private var _response: MutableLiveData<Event<ResponseStatus<*>>> = MutableLiveData(Event(ResponseStatus.None))
    val response: LiveData<Event<ResponseStatus<*>>> = _response

    fun getUserProfile(userId: String){
        viewModelScope.launch {
            _loading.value = Event(ResponseStatus.Loading)
            try {
                repositoryInterface.getUserProfile(userId)
            } catch (e: NoNetworkConnectionException) {
                _loading.value = Event( ResponseStatus.Error(code = ErrorCode.NO_DATA_CONNECTION.code))
            }
            _loading.value = Event(ResponseStatus.Success(""))
        }
    }

    fun getLoggedUser(){
        viewModelScope.launch {
            _loading.value = Event(ResponseStatus.Loading)

            try {
                repositoryInterface.getLoggedUserProfile()
            } catch (e: NoNetworkConnectionException){
                _loading.value = Event(ResponseStatus.Error(code = ErrorCode.NO_DATA_CONNECTION.code))
            }

            _loading.value = Event(ResponseStatus.Success(""))
        }
    }

    fun updateLoggedUserProfile(profileModel: UpdateProfileModel){
        viewModelScope.launch {
            _loading.value = Event(ResponseStatus.Loading)

            val result: ResponseStatus<*> = try {
                repositoryInterface.updateProfile(profileModel)
            } catch (e: NoNetworkConnectionException){
                ResponseStatus.Error(code = ErrorCode.NO_DATA_CONNECTION.code)
            }

            _loading.value = Event(result)
            _response.value = Event(result)
        }
    }

    fun updateLoggedUserPassword(passwordModel: UpdatePasswordModel){
        viewModelScope.launch {
            _loading.value = Event(ResponseStatus.Loading)

            val result: ResponseStatus<*> = try {
                repositoryInterface.updatePassword(passwordModel)
            } catch (e: NoNetworkConnectionException){
                ResponseStatus.Error(code = ErrorCode.NO_DATA_CONNECTION.code)
            }

            _loading.value = Event(result)
            _response.value = Event(result)
        }
    }

    fun sendProfileAction(action: ProfileActionEnum, userId: String){
        viewModelScope.launch {
            _loading.value= Event(ResponseStatus.Loading)

            val result: ResponseStatus<*> = try {
                repositoryInterface.sendProfileAction(action, userId)
            } catch (e:NoNetworkConnectionException) {
                ResponseStatus.Error(code = ErrorCode.NO_DATA_CONNECTION.code)
            }

            _loading.value = Event(result)
        }
    }

    fun logout(){
        viewModelScope.launch {
            _loading.value = Event(ResponseStatus.Loading)

            val result: ResponseStatus<*> = try {
                repositoryInterface.logout()
            } catch (e:NoNetworkConnectionException) {
                ResponseStatus.Error(code = ErrorCode.NO_DATA_CONNECTION.code)
            }

            _loading.value = Event(result)
            _logout.value = Event(result)
        }
    }

}