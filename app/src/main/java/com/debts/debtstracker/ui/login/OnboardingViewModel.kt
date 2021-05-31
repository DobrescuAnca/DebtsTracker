package com.debts.debtstracker.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.debts.debtstracker.data.ResponseStatus
import com.debts.debtstracker.data.repository.RepositoryInterface
import com.debts.debtstracker.ui.base.BaseViewModel
import com.debts.debtstracker.util.Event
import kotlinx.coroutines.launch

class OnboardingViewModel(private val repository: RepositoryInterface): BaseViewModel() {

    private var _loginStatus: MutableLiveData<Event<String>> = MutableLiveData()
    val loginStatus: LiveData<Event<String>> = _loginStatus

    fun login(username: String, pass: String){
        baseScope.launch {
            _loading.value = Event(ResponseStatus.Loading)

            val result = repository.login(username, pass)

            _loading.value = Event(result)
            if(result is ResponseStatus.Success)
                _loginStatus.value = Event(result.data)
        }
    }
}