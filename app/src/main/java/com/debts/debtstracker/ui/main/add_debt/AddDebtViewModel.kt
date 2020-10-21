package com.debts.debtstracker.ui.main.add_debt

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.debts.debtstracker.R
import com.debts.debtstracker.data.ErrorCode
import com.debts.debtstracker.data.ResponseStatus
import com.debts.debtstracker.data.network.api.NoNetworkConnectionException
import com.debts.debtstracker.data.network.model.AddDebtModel
import com.debts.debtstracker.data.network.model.BorrowerDebtModel
import com.debts.debtstracker.data.network.model.EmptyUserModel
import com.debts.debtstracker.data.network.model.UserModel
import com.debts.debtstracker.data.repository.RepositoryInterface
import com.debts.debtstracker.ui.base.BaseViewModel
import com.debts.debtstracker.util.Event
import com.debts.debtstracker.util.LOGGED_USER_EXPLANATION
import com.debts.debtstracker.util.getString
import kotlinx.coroutines.launch
import java.util.*

class AddDebtViewModel(private val repositoryInterface: RepositoryInterface): BaseViewModel() {

    val friendList = repositoryInterface.friendList
    private val loggedUserProfile = repositoryInterface.userProfile

    private var _addDebtResponse: MutableLiveData<Event<ResponseStatus<*>>> = MutableLiveData(Event(ResponseStatus.None))
    val addDebtResponse: LiveData<Event<ResponseStatus<*>>> = _addDebtResponse

    fun getServerFriendList(){
        viewModelScope.launch {
            _loading.value = Event(ResponseStatus.Loading)

            try {
                repositoryInterface.getLoggedUserProfile()
                repositoryInterface.getFriendList()
            } catch (e: NoNetworkConnectionException){
                _loading.value = Event(ResponseStatus.Error(code = ErrorCode.NO_DATA_CONNECTION.code))
            }

            _loading.value = Event(ResponseStatus.Success(""))
        }
    }

    fun addDebt(lenderId: String, borrowerList: List<UserModel>, description: String){
        val borrowerDebtList = LinkedList<BorrowerDebtModel>()

        borrowerList.map {
            if(it.debtSum != null && it.debtSum != 0f)
                borrowerDebtList.add( BorrowerDebtModel( it.id, it.debtSum!!))
        }

        addDebt(
            AddDebtModel(
                borrowerDebtList,
                description,
                lenderId
            )
        )
    }

    private fun addDebt(debtModel: AddDebtModel){
        viewModelScope.launch {
            _loading.value= Event(ResponseStatus.Loading)

            var result: ResponseStatus<*> = ResponseStatus.None
            result = try {
                repositoryInterface.addDebt(debtModel)
            } catch (e:NoNetworkConnectionException) {
                ResponseStatus.Error(code = ErrorCode.NO_DATA_CONNECTION.code)
            }

            _loading.value = Event(result)
            _addDebtResponse.value = Event(result)
        }
    }

    fun getUpdatedFriendList(): MutableList<UserModel>?{
        val updatedFriendList = friendList.value?.toMutableList()
        val noSelection = EmptyUserModel.copy(name = getString(R.string.select_user))
        val loggedUser = loggedUserProfile.value?.copy()

        updatedFriendList?.let { list ->
            loggedUser?.name = loggedUser?.name + LOGGED_USER_EXPLANATION
            list.add(0, loggedUser!!)
            list.add(0, noSelection)
        }

        return updatedFriendList
    }
}