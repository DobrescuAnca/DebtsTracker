package com.debts.debtstracker.ui.main.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.debts.debtstracker.data.ErrorCode
import com.debts.debtstracker.data.ResponseStatus
import com.debts.debtstracker.data.network.api.NoNetworkConnectionException
import com.debts.debtstracker.data.network.model.HomeCardFilterTypeEnum
import com.debts.debtstracker.data.pagination.HomeDataSource
import com.debts.debtstracker.data.pagination.getPagedListRequests
import com.debts.debtstracker.data.repository.RepositoryInterface
import com.debts.debtstracker.ui.base.BaseViewModel
import com.debts.debtstracker.util.Event
import kotlinx.coroutines.launch

class HomeViewModel(private val repositoryInterface: RepositoryInterface): BaseViewModel() {

    val totalDebts = MutableLiveData<ResponseStatus<*>>()

    private val filter = MutableLiveData(HomeCardFilterTypeEnum.ALL.toString())

    private val repoResult = Transformations.map(filter){
        getPagedListRequests(HomeDataSource(filter.value.toString(), viewModelScope))
    }

    val content = Transformations.switchMap(repoResult) { it.pagedList }
    val networkState = Transformations.switchMap(repoResult) { it.networkState }

    init {
        filter.value = HomeCardFilterTypeEnum.ALL.toString()
    }

    fun getTotalDebts(){
        viewModelScope.launch {
            _loading.value = Event(ResponseStatus.Loading)

            val result = try {
                 repositoryInterface.getUserTotalDebts()
            } catch (e: NoNetworkConnectionException) {
                ResponseStatus.Error(code = ErrorCode.NO_DATA_CONNECTION.code)
            }

            totalDebts.value = result
            _loading.value = Event(result)
        }
    }

    fun setFilter(filterTypeEnum: HomeCardFilterTypeEnum){
        this.filter.value = filterTypeEnum.toString()
    }

}