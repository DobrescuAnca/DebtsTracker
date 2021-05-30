package com.debts.debtstracker.ui.main

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.debts.debtstracker.data.network.model.HomeCardModel
import com.debts.debtstracker.data.repository.RepositoryInterface
import com.debts.debtstracker.ui.base.BaseViewModel
import kotlinx.coroutines.flow.Flow

class MainViewModel(private val repository: RepositoryInterface): BaseViewModel() {

    private var currentSearchResult: Flow<PagingData<HomeCardModel>>? = null
    private var filter: String? = null


    fun filterHomeCards(filterString: String): Flow<PagingData<HomeCardModel>> {
        val lastResult = currentSearchResult
        if (filterString == filter && lastResult != null) {
            return lastResult
        }
        filter = filterString

        val newResult: Flow<PagingData<HomeCardModel>> = repository.getHomeCardsStream(filterString).cachedIn(viewModelScope)
        currentSearchResult = newResult
        return newResult
    }
}