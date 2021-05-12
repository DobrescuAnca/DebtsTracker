package com.debts.debtstracker.ui.main.add_debt

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.debts.debtstracker.data.ResponseStatus
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
        baseScope.launch {
            _loading.value = Event(ResponseStatus.Loading)

            val result = repositoryInterface.getFriendList()

            friendList.value = result
            _loading.value = Event(result)
        }
    }

    fun addDebt(debtModel: AddDebtModel){
        baseScope.launch {
            _loading.value= Event(ResponseStatus.Loading)

            val result = repositoryInterface.addDebt(debtModel)

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