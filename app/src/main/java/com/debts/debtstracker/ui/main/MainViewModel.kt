package com.debts.debtstracker.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.debts.debtstracker.data.ResponseStatus
import com.debts.debtstracker.data.network.model.HomeCardModel
import com.debts.debtstracker.data.network.model.ProfileModel
import com.debts.debtstracker.data.network.model.TotalsModel
import com.debts.debtstracker.data.repository.RepositoryInterface
import com.debts.debtstracker.ui.base.BaseViewModel
import com.debts.debtstracker.util.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MainViewModel(private val repository: RepositoryInterface): BaseViewModel() {

    private var currentSearchResult: Flow<PagingData<HomeCardModel>>? = null
    private var filter: String? = null

    private var _totals: MutableLiveData<TotalsModel> = MutableLiveData()
    val totals: LiveData<TotalsModel> = _totals

    private var _profileData: MutableLiveData<ProfileModel> = MutableLiveData()
    val profileData: LiveData<ProfileModel> = _profileData


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

    fun getProfile(){
        baseScope.launch {
            _loading.value = Event(ResponseStatus.Loading)

            val result = repository.getUserProfile()

            if(result is ResponseStatus.Success)
                _profileData.value = result.data
            _loading.value = Event(result)
        }
    }

    fun getTotals(){
        baseScope.launch {
            _loading.value = Event(ResponseStatus.Loading)

            val result = repository.getTotals()

            if(result is ResponseStatus.Success)
                _totals.value = result.data
            _loading.value = Event(result)
        }
    }

    fun equate(){
        baseScope.launch {
            _loading.value = Event(ResponseStatus.Loading)

            val result = repository.equate()

            if(result is ResponseStatus.Success) {

            }

            _loading.value = Event(result)
        }
    }

}