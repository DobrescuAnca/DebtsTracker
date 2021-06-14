package com.debts.debtstracker.ui.main.add_debt

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.debts.debtstracker.data.ResponseStatus
import com.debts.debtstracker.data.network.model.AddDebtModel
import com.debts.debtstracker.data.repository.RepositoryInterface
import com.debts.debtstracker.ui.base.BaseViewModel
import com.debts.debtstracker.util.Event
import kotlinx.coroutines.launch
import org.joda.time.DateTime

class AddDebtViewModel(private val repository: RepositoryInterface): BaseViewModel() {

    var selectedDate: DateTime = DateTime.now()

    private var _addDebtStatus: MutableLiveData<Event<ResponseStatus<*>>> = MutableLiveData(Event(ResponseStatus.None))
    val addDebtStatus: LiveData<Event<ResponseStatus<*>>> = _addDebtStatus

    fun updateDate(year: Int, month: Int, day: Int): String{
        selectedDate = DateTime(year, month, day, 0, 0)

        return selectedDate.toString("dd.MM.yyyy")
    }

    fun addDebt(debtModel: AddDebtModel){
        baseScope.launch {
            _loading.value = Event(ResponseStatus.Loading)

            val result = repository.addDebt(debtModel)
            _addDebtStatus.value = Event(result)
            _loading.value = Event(result)
        }
    }


}