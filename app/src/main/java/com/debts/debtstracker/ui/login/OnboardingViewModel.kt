package com.debts.debtstracker.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.debts.debtstracker.data.ErrorCode
import com.debts.debtstracker.data.ResponseStatus
import com.debts.debtstracker.data.network.api.NoNetworkConnectionException
import com.debts.debtstracker.data.network.model.RegisterModel
import com.debts.debtstracker.data.repository.RepositoryInterface
import com.debts.debtstracker.util.Event
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis

class OnboardingViewModel(private val repository: RepositoryInterface): ViewModel() {

    private var _registerStatus: MutableLiveData<Event<ResponseStatus<*>>> = MutableLiveData(Event(ResponseStatus.None))
    val registerStatus: LiveData<Event<ResponseStatus<*>>> = _registerStatus

    private var _loginStatus: MutableLiveData<Event<ResponseStatus<*>>> = MutableLiveData(Event(ResponseStatus.None))
    val loginStatus: LiveData<Event<ResponseStatus<*>>> = _loginStatus

    private var _loading: MutableLiveData<Event<ResponseStatus<*>>> = MutableLiveData(Event(ResponseStatus.None))
    val loading: LiveData<Event<ResponseStatus<*>>> = _loading

    fun login(username: String, pass: String){
        viewModelScope.launch {
            _loading.value = Event(ResponseStatus.Loading)

            var result: ResponseStatus<*> = ResponseStatus.None
            measureTimeMillis {
                result = try {
                    repository.login(username, pass)
                } catch (e: NoNetworkConnectionException) {
                    ResponseStatus.Error(code = ErrorCode.NO_DATA_CONNECTION.code)
                }
            }

            _loading.value = Event(result)
            _loginStatus.value = Event(result)
        }
    }

    fun register(model: RegisterModel){
        viewModelScope.launch {
            _loading.value = Event(ResponseStatus.Loading)

            var result: ResponseStatus<*> = ResponseStatus.None
            measureTimeMillis {
                result = try {
                    repository.signUp(model)
                } catch (e: NoNetworkConnectionException) {
                    ResponseStatus.Error(code = ErrorCode.NO_DATA_CONNECTION.code)
                }
            }
            _loading.value = Event(result)
            _registerStatus.value = Event(result)
        }
    }

}