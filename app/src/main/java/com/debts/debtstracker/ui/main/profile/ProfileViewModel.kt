package com.debts.debtstracker.ui.main.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.debts.debtstracker.data.ErrorCode
import com.debts.debtstracker.data.ResponseStatus
import com.debts.debtstracker.data.network.api.NoNetworkConnectionException
import com.debts.debtstracker.data.network.model.*
import com.debts.debtstracker.data.pagination.DebtsWithUserDataSource
import com.debts.debtstracker.data.pagination.UserListDataSource
import com.debts.debtstracker.data.pagination.getPagedListRequests
import com.debts.debtstracker.data.repository.RepositoryInterface
import com.debts.debtstracker.ui.base.BaseViewModel
import com.debts.debtstracker.util.Event
import kotlinx.coroutines.launch

class ProfileViewModel(private val repositoryInterface: RepositoryInterface): BaseViewModel() {

    val userProfile = MutableLiveData<UserModel>()

    private val debtWithUserStatus = MutableLiveData(DebtWithUserStatus.ALL)

    private val repoResult = Transformations.map(debtWithUserStatus){
        getPagedListRequests(DebtsWithUserDataSource(it, userProfile.value?.id ?: "0", viewModelScope))
    }

    val content = Transformations.switchMap(repoResult) { it.pagedList }
    val networkState = Transformations.switchMap(repoResult) { it.networkState }


    private var _logout: MutableLiveData<Event<ResponseStatus<*>>> = MutableLiveData(Event(ResponseStatus.None))
    val logout: LiveData<Event<ResponseStatus<*>>> = _logout

    private var _updateResponse: MutableLiveData<Event<ResponseStatus<*>>> = MutableLiveData(Event(ResponseStatus.None))
    val updateResponse: LiveData<Event<ResponseStatus<*>>> = _updateResponse


    fun getUserProfile(userId: String){
        viewModelScope.launch {
            _loading.value = Event(ResponseStatus.Loading)

            val result = try {
                repositoryInterface.getUserProfile(userId)
            } catch (e: NoNetworkConnectionException) {
                ResponseStatus.Error(code = ErrorCode.NO_DATA_CONNECTION.code)
            }
            if(result is ResponseStatus.Success)
                userProfile.value = result.data
            _loading.value = Event(result)
        }
    }

    fun getLoggedUser(){
        viewModelScope.launch {
            _loading.value = Event(ResponseStatus.Loading)

            val result = try {
                repositoryInterface.getLoggedUserProfile()
            } catch (e: NoNetworkConnectionException){
                ResponseStatus.Error(code = ErrorCode.NO_DATA_CONNECTION.code)
            }

            if(result is ResponseStatus.Success)
                userProfile.value = result.data
            _loading.value = Event(result)
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
            _updateResponse.value = Event(result)
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
            _updateResponse.value = Event(result)
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

    fun updateDebtsWithUserStatus(status: DebtWithUserStatus){
        debtWithUserStatus.value = status
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