package com.debts.debtstracker.ui.main.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.debts.debtstracker.R
import com.debts.debtstracker.data.ResponseStatus
import com.debts.debtstracker.data.Status
import com.debts.debtstracker.data.network.model.HomeCardFilterTypeEnum
import com.debts.debtstracker.data.network.model.HomeCardModel
import com.debts.debtstracker.databinding.FragmentHomeBinding
import com.debts.debtstracker.ui.base.BaseFragment
import com.debts.debtstracker.ui.custom_views.RoundedIconTextView
import com.debts.debtstracker.ui.main.MainActivity
import com.debts.debtstracker.ui.main.profile.ProfileViewModel
import com.debts.debtstracker.util.DEBT_ID
import com.debts.debtstracker.util.EventObserver
import com.debts.debtstracker.util.PROFILE_USER_ID
import com.squareup.picasso.Picasso
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class HomeFragment: BaseFragment() {

    private lateinit var dataBinding: FragmentHomeBinding
    private val viewModel: HomeViewModel by sharedViewModel()
    private val profileViewModel: ProfileViewModel by sharedViewModel()

    private lateinit var adapter: HomeCardsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_home,
            container,
            false)

        return dataBinding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.getTotalDebts()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setLoading(true)
        initAdapter()

        setupLayout()
        attachObservers()
        profileViewModel.getLoggedUser()
    }

    private fun setupLayout(){
        dataBinding.currentUserProfilePicture.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
            (activity as MainActivity).hideNavBar(true)
        }

        dataBinding.allFilter.selectView(true)

        dataBinding.allFilter.setOnClickListener {
           selectFilter(dataBinding.allFilter, HomeCardFilterTypeEnum.ALL)
        }
        dataBinding.friendFilter.setOnClickListener {
            selectFilter(dataBinding.friendFilter, HomeCardFilterTypeEnum.FRIENDS)
        }
        dataBinding.debtsFilter.setOnClickListener {
            selectFilter(dataBinding.debtsFilter, HomeCardFilterTypeEnum.DEBTS)
        }
    }

    private fun selectFilter(filterView: RoundedIconTextView, filterType: HomeCardFilterTypeEnum){
        if(!filterView.getViewIsSelected()) {
            deselectFilters()
            viewModel.setFilter(filterType)
            filterView.selectView(true)
        }
    }

    private fun deselectFilters(){
        dataBinding.allFilter.selectView(false)
        dataBinding.debtsFilter.selectView(false)
        dataBinding.friendFilter.selectView(false)
    }

    private fun initAdapter(){
        adapter = HomeCardsAdapter(
            this::onCardClicked
        )
        dataBinding.homeCardContainer.adapter = adapter
    }

    private fun onCardClicked(homeCard: HomeCardModel){
        if(homeCard.homeCardType.toString().startsWith("FRIEND")){
            val bundle = Bundle()
            bundle.putString(PROFILE_USER_ID, homeCard.otherUserId)
            findNavController().navigate(R.id.action_global_profileFriendFragment, bundle)
        }

        if(homeCard.homeCardType.toString().startsWith("DEBT")){
            val bundle = Bundle()
            bundle.putString(DEBT_ID, homeCard.debtId)
//            findNavController().navigate(R.id.action_global_debtFragment, bundle)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun attachObservers() {
        viewModel.totalDebts.observe(viewLifecycleOwner) {
            if(it is ResponseStatus.Success) {
                val totalDebts = it.data
                dataBinding.tvTotalBorrowed.text = "${totalDebts.totalBorrowed} lei"
                dataBinding.tvTotalLend.text = "${totalDebts.totalLent} lei"
            }
        }

        profileViewModel.userProfile.observe(viewLifecycleOwner) {
            if(it is ResponseStatus.Success)
                Picasso.get()
                    .load(it.data.profilePictureUrl)
                    .error(R.drawable.ic_people_menu)
                    .into(dataBinding.currentUserProfilePicture)
        }

        viewModel.content.observe(
            viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.networkState.observe(viewLifecycleOwner, EventObserver {
            if(it.status == Status.EMPTY_LIST)
                dataBinding.tvEmptyList.visibility = View.VISIBLE
            else {
                adapter.setNetworkStateValue(it)
                dataBinding.tvEmptyList.visibility = View.GONE
            }
        })
    }

    override fun setLoading(loading: Boolean) {

    }

}