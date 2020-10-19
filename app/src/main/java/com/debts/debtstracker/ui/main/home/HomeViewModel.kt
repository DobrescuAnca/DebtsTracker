package com.debts.debtstracker.ui.main.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.debts.debtstracker.data.pagination.HomeDataSource
import com.debts.debtstracker.data.pagination.getPagedListRequests

class HomeViewModel: ViewModel() {

    private val filter = MutableLiveData<String>()

    private val repoResult = Transformations.map(filter){
        getPagedListRequests(HomeDataSource(viewModelScope))
    }

    val content = Transformations.switchMap(repoResult) { it.pagedList }
    val networkState = Transformations.switchMap(repoResult) { it.networkState }

    init {
        filter.value = "init"
    }


}