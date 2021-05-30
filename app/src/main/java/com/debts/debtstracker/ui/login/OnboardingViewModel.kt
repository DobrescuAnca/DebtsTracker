package com.debts.debtstracker.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.debts.debtstracker.data.ResponseStatus
import com.debts.debtstracker.data.repository.RepositoryInterface
import com.debts.debtstracker.ui.base.BaseViewModel
import com.debts.debtstracker.util.Event
import kotlinx.coroutines.launch

class OnboardingViewModel(private val repository: RepositoryInterface): BaseViewModel() {

    private var _loginStatus: MutableLiveData<Event<ResponseStatus<*>>> = MutableLiveData(Event(ResponseStatus.None))
    val loginStatus: LiveData<Event<ResponseStatus<*>>> = _loginStatus

    fun login(username: String, pass: String){
        baseScope.launch {
            _loading.value = Event(ResponseStatus.Loading)

            val result = repository.login(username, pass)

            _loading.value = Event(result)
            _loginStatus.value = Event(result)
        }
    }
}