package com.debts.debtstracker.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.debts.debtstracker.data.ErrorCode
import com.debts.debtstracker.data.ResponseStatus
import com.debts.debtstracker.data.network.api.ExceptionTypes
import com.debts.debtstracker.data.network.api.TokenAuthenticatorException
import com.debts.debtstracker.util.Event
import com.squareup.moshi.JsonDataException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.plus
import java.net.SocketTimeoutException
import java.net.UnknownHostException

open class BaseViewModel: ViewModel() {

    protected var _loading: MutableLiveData<Event<ResponseStatus<*>>> = MutableLiveData(Event(ResponseStatus.None))
    val loading: LiveData<Event<ResponseStatus<*>>> = _loading


    private var handler = CoroutineExceptionHandler { _, exception ->
        val responseStatus: ResponseStatus.Error = when(exception){
            is TokenAuthenticatorException -> {
                if(exception.message == ExceptionTypes.INVALID_REFRESH_TOKEN.toString())
                    ResponseStatus.Error(code = ErrorCode.INVALID_TOKEN.code)
                else
                    ResponseStatus.Error(code = ErrorCode.NO_DATA_CONNECTION.code)
            }
            is JsonDataException -> {
                ResponseStatus.Error(code = ErrorCode.JSON_PARSING_ERROR.code)
            }
            is UnknownHostException,
            is SocketTimeoutException ->{
                ResponseStatus.Error(code = ErrorCode.NO_DATA_CONNECTION.code)
            }
            else -> {
                ResponseStatus.Error(code = ErrorCode.INVALID_DATA.code)
            }
        }

        _loading.value = Event(responseStatus)
    }

    val baseScope = viewModelScope + handler
}