package com.debts.debtstracker.ui.main.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.debts.debtstracker.data.ResponseStatus
import com.debts.debtstracker.data.network.model.*
import com.debts.debtstracker.data.pagination.DebtsWithUserDataSource
import com.debts.debtstracker.data.pagination.getPagedListRequests
import com.debts.debtstracker.data.repository.RepositoryInterface
import com.debts.debtstracker.ui.base.BaseViewModel
import com.debts.debtstracker.util.Event
import kotlinx.coroutines.launch

class ProfileViewModel(private val repositoryInterface: RepositoryInterface): BaseViewModel() {

    val userProfile = MutableLiveData<UserModel>()

    private val debtWithUserStatus = MutableLiveData(DebtWithUserStatus.ALL)

    private val repoResult = Transformations.map(debtWithUserStatus){
        getPagedListRequests(DebtsWithUserDataSource(it, userProfile.value?.id ?: "0", baseScope))
    }

    val content = Transformations.switchMap(repoResult) { it.pagedList }
    val networkState = Transformations.switchMap(repoResult) { it.networkState }


    private var _logout: MutableLiveData<Event<ResponseStatus<*>>> = MutableLiveData(Event(ResponseStatus.None))
    val logout: LiveData<Event<ResponseStatus<*>>> = _logout

    private var _updateResponse: MutableLiveData<Event<ResponseStatus<*>>> = MutableLiveData(Event(ResponseStatus.None))
    val updateResponse: LiveData<Event<ResponseStatus<*>>> = _updateResponse


    fun getUserProfile(userId: String){
        baseScope.launch {
            _loading.value = Event(ResponseStatus.Loading)

            val result = repositoryInterface.getUserProfile(userId)

            if(result is ResponseStatus.Success)
                userProfile.value = result.data
            _loading.value = Event(result)
        }
    }

    fun getLoggedUser(){
        baseScope.launch {
            _loading.value = Event(ResponseStatus.Loading)

            val result = repositoryInterface.getLoggedUserProfile()

            if(result is ResponseStatus.Success)
                userProfile.value = result.data
            _loading.value = Event(result)
        }
    }

    fun updateLoggedUserProfile(profileModel: UpdateProfileModel){
        baseScope.launch {
            _loading.value = Event(ResponseStatus.Loading)

            val result: ResponseStatus<*> = repositoryInterface.updateProfile(profileModel)

            _loading.value = Event(result)
            _updateResponse.value = Event(result)
        }
    }

    fun updateLoggedUserPassword(passwordModel: UpdatePasswordModel){
        baseScope.launch {
            _loading.value = Event(ResponseStatus.Loading)

            val result: ResponseStatus<*> = repositoryInterface.updatePassword(passwordModel)

            _loading.value = Event(result)
            _updateResponse.value = Event(result)
        }
    }

    fun sendProfileAction(action: ProfileActionEnum, userId: String){
        baseScope.launch {
            _loading.value= Event(ResponseStatus.Loading)

            val result: ResponseStatus<*> = repositoryInterface.sendProfileAction(action, userId)

            _loading.value = Event(result)
        }
    }

    fun updateDebtsWithUserStatus(status: DebtWithUserStatus){
        debtWithUserStatus.value = status
    }

    fun logout(){
        baseScope.launch {
            _loading.value = Event(ResponseStatus.Loading)

            val result: ResponseStatus<*> = repositoryInterface.logout()

            _loading.value = Event(result)
            _logout.value = Event(result)
        }
    }
}