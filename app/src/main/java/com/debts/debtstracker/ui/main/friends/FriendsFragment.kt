package com.debts.debtstracker.ui.main.friends

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.debts.debtstracker.R
import com.debts.debtstracker.data.Status
import com.debts.debtstracker.databinding.FragmentFriendListBinding
import com.debts.debtstracker.ui.base.BaseFragment
import com.debts.debtstracker.util.EventObserver
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class FriendsFragment: BaseFragment() {

    private lateinit var dataBinding: FragmentFriendListBinding
    private lateinit var adapter: UserListAdapter

    private val viewModel: FriendListViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_friend_list,
            container,
            false)

        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        attachObservers()

        dataBinding.topAppBar.setOnClickListener{
            findNavController().navigate(R.id.action_friendsFragment_to_userListFragment)
        }
    }

    private fun initAdapter(){
        adapter = UserListAdapter(
            this::gotoUserProfile,
            R.layout.item_friend_list
        )
        dataBinding.listContainer.adapter = adapter
    }

    private fun gotoUserProfile(id: String){

    }

    private fun attachObservers() {
        viewModel.content.observe(
            viewLifecycleOwner, Observer {
                adapter.submitList(it)
            })

        viewModel.networkState.observe(viewLifecycleOwner, EventObserver {
            if(it.status == Status.EMPTY_LIST)
                dataBinding.tvEmptyList.visibility = View.VISIBLE
            else
                adapter.setNetworkStateValue(it)
        })
    }
}