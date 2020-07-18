package com.debts.debtstracker.ui.main.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.debts.debtstracker.data.ErrorCode
import com.debts.debtstracker.data.ResponseStatus
import com.debts.debtstracker.data.network.api.NoNetworkConnectionException
import com.debts.debtstracker.data.network.model.ProfileActionEnum
import com.debts.debtstracker.data.repository.RepositoryInterface
import com.debts.debtstracker.util.Event
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis

class ProfileViewModel(private val repositoryInterface: RepositoryInterface): ViewModel() {

    val userProfile = repositoryInterface.userProfile

    private var _loading: MutableLiveData<Event<ResponseStatus<*>>> = MutableLiveData(Event(ResponseStatus.None))
    val loading: LiveData<Event<ResponseStatus<*>>> = _loading


    fun makeRequestForSupportIncidentsList(userId: String){
        viewModelScope.launch {
            _loading.value = Event(ResponseStatus.Loading)
            measureTimeMillis {
                try {
                    repositoryInterface.getUserProfile(userId)
                } catch (e: NoNetworkConnectionException) {
                    _loading.value = Event( ResponseStatus.Error(code = ErrorCode.NO_DATA_CONNECTION.code))
                }
            }
        }
    }

    fun sendProfileAction(action: ProfileActionEnum, userId: String){
        viewModelScope.launch {
            _loading.value= Event(ResponseStatus.Loading)

            var result: ResponseStatus<*> = ResponseStatus.None
            result = try {
                repositoryInterface.sendProfileAction(action, userId)
            } catch (e:NoNetworkConnectionException) {
                 ResponseStatus.Error(code = ErrorCode.NO_DATA_CONNECTION.code)
            }

            _loading.value = Event(result)
        }
    }



}