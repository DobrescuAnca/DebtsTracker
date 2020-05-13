package com.debts.debtstracker.data.pagination

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import org.koin.core.KoinComponent

class DataSourceFactory<T>(
    private val dataSource: BaseDataSource<T>
): DataSource.Factory<Int, T>(), KoinComponent {
    val sourceLiveData = MutableLiveData<BaseDataSource<T>>()

    override fun create(): BaseDataSource<T> {
        this.sourceLiveData.postValue(dataSource)
        return dataSource
    }
}

fun <T>getPagedListRequests(dataSource: BaseDataSource<T>): PagedListModel<T> {
    val config = PagedList.Config.Builder()
        .setInitialLoadSizeHint(NETWORK_PAGE_SIZE)
        .setPageSize(NETWORK_PAGE_SIZE)
        .setEnablePlaceholders(false)
        .build()

    val dataFactory: DataSourceFactory<T> = DataSourceFactory(dataSource)
    val requestsLiveData =
        LivePagedListBuilder<Int, T>(dataFactory, config).build()

    return PagedListModel(
        pagedList = requestsLiveData,
        networkState = Transformations.switchMap(dataFactory.sourceLiveData) { it.networkState }
    )
}