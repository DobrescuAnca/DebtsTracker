package com.debts.debtstracker.ui.main.friends

import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.debts.debtstracker.data.pagination.UserListDataSource
import com.debts.debtstracker.data.pagination.getPagedListRequests

class UserListViewModel: ViewModel() {

    private val searchText = MutableLiveData<String>()
    private val repoResult = Transformations.map(searchText){
        getPagedListRequests(UserListDataSource(it, viewModelScope))
    }

    val content = Transformations.switchMap(repoResult) { it.pagedList }
    val networkState = Transformations.switchMap(repoResult) { it.networkState }

    var onSearchTextChanged: TextWatcher = object  : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun afterTextChanged(s: Editable?) {
            s?.let{
                if(it.length >= 3 && searchText.value != it.toString())
                    searchText.value = it.toString()
            }
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }
}