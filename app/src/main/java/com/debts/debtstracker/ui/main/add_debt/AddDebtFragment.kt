package com.debts.debtstracker.ui.main.add_debt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.children
import androidx.core.view.size
import androidx.databinding.DataBindingUtil
import com.debts.debtstracker.DebtsTrackerApplication
import com.debts.debtstracker.R
import com.debts.debtstracker.data.ResponseStatus
import com.debts.debtstracker.data.network.model.UserModel
import com.debts.debtstracker.databinding.FragmentAddDebtBinding
import com.debts.debtstracker.ui.base.BaseFragment
import com.debts.debtstracker.util.EventObserver
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.*

class AddDebtFragment: BaseFragment(){

    private val viewModel: AddDebtViewModel by sharedViewModel()
    private lateinit var dataBinding: FragmentAddDebtBinding

    private var updatedFriendList: List<UserModel> = emptyList()
    private lateinit var lender: UserModel
    private val borrowerList = LinkedList<UserModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_add_debt,
            container,
            false
        )

        dataBinding.lifecycleOwner = viewLifecycleOwner
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getServerFriendList()
        dataBinding.llBorrowersContainer.removeAllViews()

        setClickListeners()
        attachObservers()
    }

    private fun setClickListeners(){

        dataBinding.ivAddBorrower.setOnClickListener {
            addBorrower()
        }

        dataBinding.btnAddDebt.setOnClickListener {
            if(isDataValid()){
                viewModel.addDebt(
                    lender.id,
                    borrowerList,
                    dataBinding.etDescription.text.toString()
                )
            }
        }
    }

    private fun isDataValid(): Boolean{
        lender = updatedFriendList[dataBinding.spinnerLender.selectedItemId.toInt()]
        borrowerList.clear()

        if (lender.name == getString(R.string.select_user)) {
            Toast.makeText(context, R.string.lender_not_selected, Toast.LENGTH_LONG).show()
            return false
        }

        for(child in dataBinding.llBorrowersContainer.children) {
            //borrowerModel contains the id of the user selected from the list and the associated sum
            val borrowerModel = (child as BorrowerCustomView).getSelectedBorrower()

            //borrower is the user at the position specified in the borrowerModel
            val borrower: UserModel = updatedFriendList[borrowerModel?.borrowerId?.toInt() ?: 0]
            borrower.debtSum = borrowerModel?.sum

            if(borrower in borrowerList || lender == borrower) {
                Toast.makeText(context, R.string.user_must_be_selected_once, Toast.LENGTH_LONG).show()
                return false
            }
            if(borrower.id != "")
                borrowerList.add(borrower)
        }

        if(borrowerList.isNullOrEmpty()) {
            Toast.makeText(context, R.string.at_least_one_borrower_selected, Toast.LENGTH_LONG).show()
            return false
        }

        return true
    }


    private fun attachObservers(){
        viewModel.loading.observe(viewLifecycleOwner, loadingObserver)
        viewModel.friendList.observe( viewLifecycleOwner, {
            addBorrower()

            ArrayAdapter(
                context ?: DebtsTrackerApplication.applicationContext(),
                R.layout.item_spinner_dropdown,
                R.id.tv_simple,
                getFriendsNameList(viewModel.getUpdatedFriendList().orEmpty())
            ).also { adapter ->
                dataBinding.spinnerLender.adapter = adapter
            }

        })

        viewModel.addDebtResponse.observe(viewLifecycleOwner, EventObserver { event ->
            when(event){
                is ResponseStatus.Success -> {
                    //todo afiseaza succes
                    activity?.onBackPressed()
                }
                is ResponseStatus.Error -> { }
//                    handleError(result.peekContent() as ResponseStatus.Error)

            }

        })
    }

    private fun getFriendsNameList(list: List<UserModel>): List<String> {
        updatedFriendList = list
        val nameList = LinkedList<String>()

        list.map {
            nameList.add(it.name)
        }
        return  nameList
    }

    private fun addBorrower(){
        if(dataBinding.llBorrowersContainer.childCount < 5){
            val borrowerView = BorrowerCustomView(context ?: DebtsTrackerApplication.applicationContext())

            borrowerView.setupView(dataBinding.llBorrowersContainer.size, getFriendsNameList(viewModel.getUpdatedFriendList().orEmpty()))
            dataBinding.llBorrowersContainer.addView(borrowerView)
        }
    }

    override fun setLoading(loading: Boolean) {
        dataBinding.isLoading = loading
    }
}