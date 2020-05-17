package com.debts.debtstracker.ui.main.friends

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.debts.debtstracker.data.pagination.FriendListDataSource
import com.debts.debtstracker.data.pagination.getPagedListRequests

class FriendListViewModel: ViewModel() {

    private val refreshListener = MutableLiveData<Boolean>()

    private val repoResult = Transformations.map(refreshListener){
        getPagedListRequests(FriendListDataSource(viewModelScope))
    }

    val content = Transformations.switchMap(repoResult) { it.pagedList }
    val networkState = Transformations.switchMap(repoResult) { it.networkState }


    fun setRefreshListener(state: Boolean){
        refreshListener.value = state
    }
}