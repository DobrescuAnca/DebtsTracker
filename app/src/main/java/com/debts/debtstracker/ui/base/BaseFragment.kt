package com.debts.debtstracker.ui.base

import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.debts.debtstracker.R
import com.debts.debtstracker.data.ErrorCode
import com.debts.debtstracker.data.ResponseStatus
import com.debts.debtstracker.util.Event

abstract class BaseFragment: Fragment() {

    var loadingObserver: Observer<Event<ResponseStatus<*>>>

    init {
        loadingObserver = createLoadingObserver()
    }

    private fun createLoadingObserver(): Observer<Event<ResponseStatus<*>>> {
        return Observer { result ->
            when (result.getContentIfNotHandled()) {
                is ResponseStatus.Success ->
                    setLoading(false)
                is ResponseStatus.Loading ->
                    setLoading(true)
                is ResponseStatus.Error -> {
                    setLoading(false)
                    handleError(result.peekContent() as ResponseStatus.Error)
                }
                is ResponseStatus.None -> setLoading(false)
            }
        }
    }

    private fun handleError(resultError: ResponseStatus.Error) {
        when (resultError.code) {
            ErrorCode.NO_DATA_CONNECTION.code -> {
                (activity as BaseActivity).showErrorDialog(R.string.internet_error)
            }
            ErrorCode.SERVER_ERROR.code -> {
                (activity as BaseActivity).showErrorDialog(R.string.server_error)
            }
            ErrorCode.JSON_PARSING_ERROR.code -> {
                (activity as BaseActivity).showErrorDialog(R.string.json_parsing_error)
            }
            ErrorCode.INVALID_DATA.code ->
                if(resultError.errorObject.isNullOrEmpty() && resultError.message.isNullOrEmpty())
                    (activity as BaseActivity).showErrorDialog(R.string.error_occurred)
//                else
//                    resultError.errorObject?.let {  handleErrorList(it)}
        }
    }

    abstract fun setLoading(loading: Boolean)

//    open fun handleErrorList(errorList: List<ErrorModel>){
//        //method for handling specific errors in the fragment class that extends this base fragment class
//    }
}