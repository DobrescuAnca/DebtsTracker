package com.debts.debtstracker.ui.main.profile.friend_user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import com.debts.debtstracker.R
import com.debts.debtstracker.data.network.model.DebtWithUserStatus
import com.debts.debtstracker.databinding.FragmentFriendProfileBinding
import com.debts.debtstracker.ui.base.BaseFragment
import com.debts.debtstracker.ui.main.profile.ProfileViewModel
import com.debts.debtstracker.util.PROFILE_USER_ID
import com.squareup.picasso.Picasso
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ProfileFriendUserFragment: BaseFragment() {

    private lateinit var dataBinding: FragmentFriendProfileBinding
    private val viewModel: ProfileViewModel by sharedViewModel()

    private lateinit var userId: String
    private lateinit var adapter: DebtsWithUserAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_friend_profile,
            container,
            false
        )

        dataBinding.lifecycleOwner = viewLifecycleOwner
        return dataBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getString(PROFILE_USER_ID)?.let {
            userId = it
            viewModel.getUserProfile(userId)
        }

        setupLayout()
        initAdapter()
        attachObservers()
    }

    private fun setupLayout(){

    }

    private fun initAdapter(){
        adapter = DebtsWithUserAdapter(
            this::onDebtClicked
        )
        dataBinding.debtsContainer.adapter = adapter
    }

    private fun onDebtClicked(debtId: String){

    }

    private fun attachObservers(){
        viewModel.userProfile.observe(viewLifecycleOwner) { userProfile ->
            dataBinding.tvProfileName.text = userProfile.username
            Picasso.get().load(userProfile.profilePictureUrl).into(dataBinding.ivProfilePic)

            viewModel.updateDebtsWithUserStatus(DebtWithUserStatus.ALL)
        }

        viewModel.content.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.loading.observe(viewLifecycleOwner, loadingObserver)
    }

    override fun setLoading(loading: Boolean) {

    }
}