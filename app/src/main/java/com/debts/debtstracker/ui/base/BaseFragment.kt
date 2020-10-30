package com.debts.debtstracker.ui.base

import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
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
                is ResponseStatus.Success -> {
                    setLoading(false)
                }
                is ResponseStatus.Loading -> setLoading(true)
                is ResponseStatus.Error -> {
                    setLoading(false)
//                    handleError(result.peekContent() as ResponseStatus.Error)
                }
            }
        }
    }

    fun handleError(resultError: ResponseStatus.Error): Boolean {
        return when (resultError.code) {

            ErrorCode.NO_DATA_CONNECTION.code -> {
                true
            }

            else -> false
        }
    }

    abstract fun setLoading(loading: Boolean)
}