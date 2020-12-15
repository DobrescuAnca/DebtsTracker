package com.debts.debtstracker.data.pagination

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.debts.debtstracker.data.NetworkState
import com.debts.debtstracker.util.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import retrofit2.Response

abstract class BaseDataSource<T>(
    private val scope: CoroutineScope
) : PageKeyedDataSource<Int, T>(), KoinComponent {

    val networkState = MutableLiveData<Event<NetworkState>>()

    abstract suspend fun requestData(page: Int): Response<PagedListServerModel<T>>?

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, T>
    ) {
        networkState.postValue(Event(NetworkState.LOADING))

        scope.launch {
            try {
                val response: Response<PagedListServerModel<T>>? = requestData(0)
                networkState.postValue(Event(NetworkState.LOADED))

                response?.let {
                    if(response.isSuccessful) {
                            val data = response.body()
                            val list = data?.content ?: emptyList()
                            callback.onResult(list, null, 1)
                            if(list.isEmpty())
                                networkState.postValue(Event(NetworkState.EMPTY_LIST))
                    }
                }
            } catch (ioException: Exception) {
                val error = NetworkState.error(ioException.message ?: "unknown error")
                networkState.postValue(Event(error))
            }
        }
    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, T>
    ) {
        networkState.postValue(Event(NetworkState.LOADING))

        scope.launch {
            try {
                val page = params.key
                val response: Response<PagedListServerModel<T>>? = requestData(page)

                response?.let {
                    if(response.isSuccessful){
                            val data = response.body()
                            val list = data?.content ?: emptyList()
                            callback.onResult(list, page + 1)
                            networkState.postValue(Event(NetworkState.LOADED))
                    }
                    else networkState.postValue(
                        Event(NetworkState.error("error code: ${response.code()}"))
                    )
                }
            } catch (exception: Exception) {
                networkState.postValue(Event(NetworkState.error(exception.message ?: "unknown err")))
            }
        }
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, T>
    ) {}
}