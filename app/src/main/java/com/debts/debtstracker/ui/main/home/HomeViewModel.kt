package com.debts.debtstracker.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.debts.debtstracker.data.NetworkState
import com.debts.debtstracker.data.ResponseStatus
import com.debts.debtstracker.data.network.model.HomeCardFilterTypeEnum
import com.debts.debtstracker.data.network.model.HomeCardModel
import com.debts.debtstracker.data.network.model.HomeTotalDebtsModel
import com.debts.debtstracker.data.pagination.DataSourceFactory
import com.debts.debtstracker.data.pagination.HomeDataSource
import com.debts.debtstracker.data.pagination.getPagedListRequests
import com.debts.debtstracker.data.pagination.pagedListConfig
import com.debts.debtstracker.data.repository.RepositoryInterface
import com.debts.debtstracker.ui.base.BaseViewModel
import com.debts.debtstracker.util.Event
import kotlinx.coroutines.launch

class HomeViewModel(private val repositoryInterface: RepositoryInterface): BaseViewModel() {

    val totalDebts = MutableLiveData<ResponseStatus<HomeTotalDebtsModel>>()
    private val filter = MutableLiveData(HomeCardFilterTypeEnum.ALL.toString())

    private val repoResult = Transformations.map(filter){
        getPagedListRequests(HomeDataSource(filter.value.toString(), baseScope))
    }

    val content = Transformations.switchMap(repoResult) { it.pagedList }
    val networkState = Transformations.switchMap(repoResult) { it.networkState }


    var cardList: LiveData<PagedList<HomeCardModel>>
    var responseStatus: LiveData<Event<NetworkState>>                                 
    private val dataSourceFactory: DataSourceFactory<HomeCardModel>


    init {
        filter.value = HomeCardFilterTypeEnum.ALL.toString()

        dataSourceFactory = DataSourceFactory(HomeDataSource(filter.value.toString(), baseScope))
        cardList = LivePagedListBuilder(dataSourceFactory, pagedListConfig).build()
        responseStatus = Transformations.switchMap(dataSourceFactory.sourceLiveData) { it.networkState }
    }

    fun getTotalDebts(){
        baseScope.launch {
            _loading.value = Event(ResponseStatus.Loading)

            val result = repositoryInterface.getUserTotalDebts()

            totalDebts.value = result
            _loading.value = Event(result)
        }
    }

    private fun setupPaging(){
        cardList = LivePagedListBuilder(dataSourceFactory, pagedListConfig).build()
        responseStatus = Transformations.switchMap(dataSourceFactory.sourceLiveData) { it.networkState }
    }

    fun setFilter(filterTypeEnum: HomeCardFilterTypeEnum){
        this.filter.value = filterTypeEnum.toString()
    }

    fun refreshFilter(){
        this.filter.value = filter.value
    }

}