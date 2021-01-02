package com.debts.debtstracker.ui.main.add_debt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import com.debts.debtstracker.DebtsTrackerApplication
import com.debts.debtstracker.R
import com.debts.debtstracker.data.ResponseStatus
import com.debts.debtstracker.data.network.model.AddDebtModel
import com.debts.debtstracker.data.network.model.BorrowerDebtModel
import com.debts.debtstracker.data.network.model.UserModel
import com.debts.debtstracker.databinding.FragmentAddDebtBinding
import com.debts.debtstracker.ui.base.BaseActivity
import com.debts.debtstracker.ui.base.BaseFragment
import com.debts.debtstracker.ui.dialogs.DialogType
import com.debts.debtstracker.ui.dialogs.InfoDialog
import com.debts.debtstracker.ui.main.add_debt.BorrowerCustomView.Companion.DELETE_VIEW
import com.debts.debtstracker.ui.main.add_debt.BorrowerCustomView.Companion.SELECT_FRIEND
import com.debts.debtstracker.ui.main.profile.ProfileViewModel
import com.debts.debtstracker.util.EventObserver
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_add_debt.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.*

class AddDebtFragment: BaseFragment(){

    private val viewModel: AddDebtViewModel by sharedViewModel()
    private val profileViewModel: ProfileViewModel by sharedViewModel()
    private lateinit var dataBinding: FragmentAddDebtBinding

    private var activeBorrowerViewIndex: Int? = null
    private var currentSelectedLender: UserModel? = null
    private lateinit var borrowerList: List<BorrowerDebtModel>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
        addBorrower()

        setClickListeners()
        attachObservers()
    }

    private fun setClickListeners(){
        dataBinding.ivExit.setOnClickListener { activity?.onBackPressed() }

        dataBinding.ivSave.setOnClickListener {
            addDebt()
        }

        dataBinding.userName.setOnClickListener {
            openFriendListDialog(LENDER_INDEX)
        }

    }

    private fun addDebt(){
        if(isDataValid()) {
            viewModel.addDebt( AddDebtModel(
                borrowerList,
                dataBinding.etDescription.text.toString(),
                currentSelectedLender!!.id
            ))
        }
    }
    private fun isDataValid(): Boolean{
        if(currentSelectedLender == null) {
            Toast.makeText(context, R.string.lender_not_selected, Toast.LENGTH_LONG).show()
            return false
        }
        if(dataBinding.etDescription.text.isNullOrBlank()) {
            Toast.makeText(context, R.string.description_needed, Toast.LENGTH_LONG).show()
            return false
        }
        val borrowerList = LinkedList<BorrowerDebtModel>()

        for(child in llBorrowersContainer.children){
            val borrower = (child as BorrowerCustomView).getSelectedBorrower()
            if(borrower.borrowerId != "0" && borrower.sum != 0.0F)
                borrowerList.add(borrower)
        }

        if(borrowerList.isEmpty()){
            Toast.makeText(context, R.string.at_least_one_borrower_selected, Toast.LENGTH_LONG).show()
            return false
        }

        this.borrowerList = borrowerList
        return true
    }


    private fun openFriendListDialog(borrowerIndex: Int){
        activeBorrowerViewIndex = borrowerIndex

        (activity as BaseActivity).showCustomDialog(
            DialogFriendList(
                viewModel.getUpdatedFriendList(),
                this::onFriendSelected
            )
        )
    }

    private fun onFriendSelected(selectedFriend: UserModel){
        activeBorrowerViewIndex?.let {index ->
            if(index == LENDER_INDEX) {
                viewModel.updateFriendList(selectedFriend, currentSelectedLender)

                currentSelectedLender = selectedFriend
                dataBinding.userName.text = selectedFriend.name
                Picasso.get().load(selectedFriend.profilePictureUrl).into(dataBinding.profilePicture)

            } else {
                val view: BorrowerCustomView = dataBinding.llBorrowersContainer.getChildAt(index) as BorrowerCustomView
                val lastSelectedFriend = view.getSelectedUser()
                view.setSelectedUser(selectedFriend)

                viewModel.updateFriendList(selectedFriend, lastSelectedFriend)
                if(view.isFirstSelection) {
                    view.isFirstSelection = false
                    addBorrower()
                }
            }
        }
        activeBorrowerViewIndex = null
    }

    private fun attachObservers(){
        viewModel.loading.observe(viewLifecycleOwner, loadingObserver)
        profileViewModel.loading.observe(viewLifecycleOwner, loadingObserver)

        viewModel.friendList.observe( viewLifecycleOwner, {
            if(it is ResponseStatus.Success) {
                viewModel.setUpdatedFriendList(it.data.content)
                profileViewModel.getLoggedUser()
            }
        })

        profileViewModel.userProfile.observe(viewLifecycleOwner, {
            if(it is ResponseStatus.Success)
                viewModel.updateFriendList(null, it.data)
        })

        viewModel.addDebtResponse.observe(viewLifecycleOwner, EventObserver { event ->
            when(event){
                is ResponseStatus.Success ->
                    (activity as BaseActivity).showCustomDialog(
                        InfoDialog.createInstance(
                            DialogType.CONFIRMATION,
                            title = getString(R.string.success),
                            message = getString(R.string.debt_added_successfully)
                        ).setPositiveAction {
                            activity?.onBackPressed()
                        }
                    )
                is ResponseStatus.Error -> { }
//                    handleError(result.peekContent() as ResponseStatus.Error)

                else -> {}
            }

        })
    }

    private fun addBorrower(){
        val borrowerViewCount = dataBinding.llBorrowersContainer.childCount
        if(borrowerViewCount < 5){
            val borrowerView = BorrowerCustomView(context ?: DebtsTrackerApplication.applicationContext())
            borrowerView.setupView(borrowerViewCount){ actionType, childIndex ->
                when(actionType) {
                    SELECT_FRIEND -> openFriendListDialog(childIndex)
                    DELETE_VIEW -> deleteView(childIndex)
                }
            }

            dataBinding.llBorrowersContainer.addView(borrowerView)
        }
    }

    private fun deleteView(index: Int){
        val view = dataBinding.llBorrowersContainer.getChildAt(index) as BorrowerCustomView
        viewModel.updateFriendList(userToRemove = null, userToAdd =  view.getSelectedUser())

        //for deleting view, not needed yet
//        for(i in index+1 until dataBinding.llBorrowersContainer.childCount)
//            (dataBinding.llBorrowersContainer.getChildAt(i) as BorrowerCustomView).updateIndex(i-1)
//
//        dataBinding.llBorrowersContainer.removeView(view)
    }

    override fun setLoading(loading: Boolean) {
        dataBinding.isLoading = loading
    }

    companion object{
        const val LENDER_INDEX = -1
    }
}