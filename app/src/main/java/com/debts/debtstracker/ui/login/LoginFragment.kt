package com.debts.debtstracker.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.debts.debtstracker.R
import com.debts.debtstracker.data.local.LocalPreferencesInterface
import com.debts.debtstracker.databinding.FragmentLoginBinding
import com.debts.debtstracker.ui.base.BaseFragment
import com.debts.debtstracker.util.EventObserver
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class LoginFragment: BaseFragment() {

    private lateinit var dataBinding: FragmentLoginBinding
    private val sharedPrefs: LocalPreferencesInterface by inject()
    private val viewModel: OnboardingViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_login,
            container,
            false)

        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupLayout()
        attachObservers()

    }

    private fun setupLayout(){
        dataBinding.btnLogin.setOnClickListener {
            viewModel.login(
                dataBinding.etEmail.text.toString(),
                dataBinding.etPass.text.toString()
            )
        }
    }

    private fun attachObservers(){
        viewModel.loginStatus.observe(viewLifecycleOwner, EventObserver {
            sharedPrefs.saveAccessToken(it)

            findNavController().navigate(R.id.action_loginFragment_to_mainActivity)
            (activity as OnboardingActivity).finish()
        })
    }

    override fun setLoading(loading: Boolean){
        dataBinding.isLoading = loading
    }

}