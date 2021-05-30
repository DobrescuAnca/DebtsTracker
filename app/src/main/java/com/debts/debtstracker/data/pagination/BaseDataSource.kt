package com.debts.debtstracker.data.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.Response

abstract class BaseDataSource<T: Any>: PagingSource<Int, T>() {

    abstract suspend fun requestData(page: Int): Response<List<T>>?

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        val position = params.key ?: 0

        val response = requestData(position)
        val responseData = response?.body().orEmpty()

        val nextKey = if(responseData.isNullOrEmpty())
                    null
                else position + (params.loadSize / NETWORK_PAGE_SIZE)

        return LoadResult.Page(
            data = responseData,
            prevKey =  if (position == 0) null else position - 1,
            nextKey = nextKey
        )
    }

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 10
    }
}