package com.debts.debtstracker.ui.main.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.debts.debtstracker.R
import com.debts.debtstracker.data.ResponseStatus
import com.debts.debtstracker.data.local.LocalPreferencesInterface
import com.debts.debtstracker.data.network.model.UserModel
import com.debts.debtstracker.databinding.FragmentProfileBinding
import com.debts.debtstracker.ui.base.BaseFragment
import com.debts.debtstracker.ui.main.MainActivity
import com.debts.debtstracker.util.EventObserver
import com.squareup.picasso.Picasso
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ProfileFragment: BaseFragment() {

    private lateinit var dataBinding: FragmentProfileBinding
    private val viewModel: ProfileViewModel by sharedViewModel()
    private val sharedPrefs: LocalPreferencesInterface by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        dataBinding.lifecycleOwner = viewLifecycleOwner

        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getLoggedUser()

        attachClickListeners()
        attachObservers()

    }

    private fun attachClickListeners(){
        dataBinding.ivProfilePicture.setOnClickListener {

        }

        dataBinding.tvLogout.setOnClickListener {
            viewModel.logout()
        }
    }

    private fun setupLayout(user: UserModel){
        dataBinding.user = user
        Picasso.get().load(user.profilePictureUrl).into(dataBinding.ivProfilePicture)
    }


    private fun attachObservers(){
        viewModel.userProfile.observe(viewLifecycleOwner, {
            setupLayout(it)
        })

        viewModel.logout.observe(viewLifecycleOwner, EventObserver{
            when(it){
                is ResponseStatus.Success -> logout()

            }
        })
    }

    private fun logout(){
        sharedPrefs.clearSharedPrefs()

        findNavController().navigate(R.id.action_profileFragment_to_onboardingActivity)
        (activity as MainActivity).finish()
    }

    override fun setLoading(loading: Boolean) {

    }
}