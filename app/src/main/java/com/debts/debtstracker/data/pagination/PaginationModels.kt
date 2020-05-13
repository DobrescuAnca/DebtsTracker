package com.debts.debtstracker.data.pagination

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.debts.debtstracker.data.NetworkState
import com.debts.debtstracker.util.Event

data class PagedListServerModel<T>(
    val content: List<T>
)

class PagedListModel <T>(
    // the LiveData of paged lists for the UI to observe
    val pagedList: LiveData<PagedList<T>>,
    // represents the network request status to show to the user
    val networkState: LiveData<Event<NetworkState>>
)

const val NETWORK_PAGE_SIZE = 10