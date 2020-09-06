package com.debts.debtstracker.ui.main.friends

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.debts.debtstracker.R
import com.debts.debtstracker.data.Status
import com.debts.debtstracker.databinding.FragmentUserListBinding
import com.debts.debtstracker.ui.base.BaseFragment
import com.debts.debtstracker.util.EventObserver
import com.debts.debtstracker.util.PROFILE_USER_ID
import com.debts.debtstracker.util.hideKeyboard
import com.debts.debtstracker.util.openKeyboard
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class UserListFragment: BaseFragment() {

    private lateinit var dataBinding: FragmentUserListBinding
    private val viewModel: UserListViewModel by sharedViewModel()

    private lateinit var adapter: UserListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_user_list,
            container,
            false
        )

        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        setUpLayout()
        attachObservers()

    }

    private fun setUpLayout(){
        dataBinding.viewModel = viewModel

        dataBinding.etSearch.requestFocus()
        context?.openKeyboard()
        dataBinding.etSearch.setOnFocusChangeListener { view, hasFocus ->
            if(!hasFocus)
                context?.hideKeyboard(view)
        }

        dataBinding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initAdapter(){
        adapter = UserListAdapter(
            this::gotoUserProfile,
            R.layout.item_user_list
        )
        dataBinding.listContainer.adapter = adapter
    }

    private fun gotoUserProfile(id: String){
        val bundle = Bundle()
        bundle.putString(PROFILE_USER_ID, id)

        findNavController().navigate(R.id.action_global_profileFragment, bundle)
    }

    private fun attachObservers() {
        viewModel.content.observe(
            viewLifecycleOwner, {
                adapter.submitList(it)
            })

        viewModel.networkState.observe(viewLifecycleOwner, EventObserver {
            if(it.status == Status.EMPTY_LIST)
                dataBinding.tvEmptyList.visibility = View.VISIBLE
            else {
                adapter.setNetworkStateValue(it)
                dataBinding.tvEmptyList.visibility = View.GONE
            }
        })
    }
}