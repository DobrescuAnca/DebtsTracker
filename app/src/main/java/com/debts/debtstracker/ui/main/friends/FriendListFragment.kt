package com.debts.debtstracker.ui.main.friends

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.debts.debtstracker.R
import com.debts.debtstracker.databinding.FragmentFriendListBinding
import com.debts.debtstracker.ui.base.BaseFragment
import com.debts.debtstracker.ui.main.add_debt.AddDebtViewModel
import com.debts.debtstracker.util.PROFILE_USER_ID
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class FriendListFragment: BaseFragment() {

    private lateinit var dataBinding: FragmentFriendListBinding
    private lateinit var adapter: FriendListAdapter

    private val viewModel: AddDebtViewModel by sharedViewModel()

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

        viewModel.getServerFriendList()

        dataBinding.topAppBar.setOnClickListener{
            findNavController().navigate(R.id.action_friendsFragment_to_userListFragment)
        }
    }

    private fun initAdapter(){
        adapter = FriendListAdapter(
            this::gotoUserProfile
        )
        dataBinding.listContainer.adapter = adapter
    }

    private fun gotoUserProfile(id: String){
        val bundle = Bundle()
        bundle.putString(PROFILE_USER_ID, id)

        findNavController().navigate(R.id.action_global_profileFriendFragment, bundle)
    }

    private fun attachObservers() {
        viewModel.friendList.observe(
            viewLifecycleOwner, {
                adapter.submitList(it)
            })
    }

    override fun setLoading(loading: Boolean) { }
}