package com.debts.debtstracker.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.debts.debtstracker.data.ResponseStatus
import com.debts.debtstracker.util.Event

open class BaseViewModel: ViewModel() {

    protected var _loading: MutableLiveData<Event<ResponseStatus<*>>> = MutableLiveData(Event(ResponseStatus.None))
    val loading: LiveData<Event<ResponseStatus<*>>> = _loading
}