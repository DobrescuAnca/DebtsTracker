package com.debts.debtstracker.ui.main.add_debt

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.debts.debtstracker.data.ErrorCode
import com.debts.debtstracker.data.ResponseStatus
import com.debts.debtstracker.data.network.api.NoNetworkConnectionException
import com.debts.debtstracker.data.network.model.AddDebtModel
import com.debts.debtstracker.data.network.model.UserModel
import com.debts.debtstracker.data.pagination.PagedListServerModel
import com.debts.debtstracker.data.repository.RepositoryInterface
import com.debts.debtstracker.ui.base.BaseViewModel
import com.debts.debtstracker.util.Event
import kotlinx.coroutines.launch
import java.util.*

class AddDebtViewModel(private val repositoryInterface: RepositoryInterface): BaseViewModel() {

    val friendList = MutableLiveData<ResponseStatus<PagedListServerModel<UserModel>>>()

    private var updatedFriendList = LinkedList<UserModel>()

    private var _addDebtResponse: MutableLiveData<Event<ResponseStatus<*>>> = MutableLiveData(Event(ResponseStatus.None))
    val addDebtResponse: LiveData<Event<ResponseStatus<*>>> = _addDebtResponse

    fun getServerFriendList(){
        viewModelScope.launch {
            _loading.value = Event(ResponseStatus.Loading)

            val result = try {
                repositoryInterface.getFriendList()
            } catch (e: NoNetworkConnectionException){
                ResponseStatus.Error(code = ErrorCode.NO_DATA_CONNECTION.code)
            }

            friendList.value = result
            _loading.value = Event(result)
        }
    }

    fun addDebt(debtModel: AddDebtModel){
        viewModelScope.launch {
            _loading.value= Event(ResponseStatus.Loading)

            val result = try {
                repositoryInterface.addDebt(debtModel)
            } catch (e:NoNetworkConnectionException) {
                ResponseStatus.Error(code = ErrorCode.NO_DATA_CONNECTION.code)
            }

            _loading.value = Event(result)
            _addDebtResponse.value = Event(result)
        }
    }

    fun getUpdatedFriendList(): List<UserModel>{
        return updatedFriendList
    }

    fun setUpdatedFriendList(list: List<UserModel>){
        updatedFriendList = LinkedList(list)
    }

    fun updateFriendList(userToRemove: UserModel?, userToAdd: UserModel? = null){
        userToAdd?.let {
            if (it !in updatedFriendList)
                updatedFriendList.add(it)
        }

         updatedFriendList.remove(userToRemove)
    }
}