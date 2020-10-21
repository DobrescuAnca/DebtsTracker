package com.debts.debtstracker.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatEditText
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.debts.debtstracker.R
import com.debts.debtstracker.data.ResponseStatus
import com.debts.debtstracker.data.local.LocalPreferencesInterface
import com.debts.debtstracker.data.network.model.AuthModel
import com.debts.debtstracker.data.network.model.RegisterModel
import com.debts.debtstracker.databinding.FragmentSignupBinding
import com.debts.debtstracker.ui.base.BaseFragment
import com.debts.debtstracker.util.EventObserver
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SignupFragment: BaseFragment() {

    private val viewModel:OnboardingViewModel by sharedViewModel()
    private val sharedPrefs: LocalPreferencesInterface by inject()
    private lateinit var dataBinding: FragmentSignupBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_signup,
            container,
            false
        )

        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupLayout()
        attachObservers()
    }

    private fun setupLayout(){
        dataBinding.btnSignup.setOnClickListener {
            if(isRegisterDataValid())
                viewModel.register( RegisterModel(
                    dataBinding.etEmail.text.toString(),
                    dataBinding.etName.text.toString(),
                    dataBinding.etPass.text.toString()
                ))
        }
    }

    private fun isRegisterDataValid(): Boolean{
        var validData = true

        if (!dataBinding.etPass.matchData(dataBinding.etPassConf)){
            //show error message
            validData = false
        }

       return validData
    }

    private fun AppCompatEditText.matchData(data: AppCompatEditText): Boolean{
        return this.text.toString() == data.text.toString()
    }

    private fun attachObservers(){
        viewModel.registerStatus.observe(viewLifecycleOwner, EventObserver { result ->
            if (result is ResponseStatus.Success)
                viewModel.login(
                    dataBinding.etEmail.text.toString(),
                    dataBinding.etPass.text.toString()
                )
        })
        
        viewModel.loginStatus.observe(viewLifecycleOwner, EventObserver { result ->
            if (result is ResponseStatus.Success) {
                sharedPrefs.saveRefreshToken(result.data as AuthModel)
                findNavController().navigate(R.id.action_signupFragment_to_mainActivity)
                (activity as OnboardingActivity).finish()
            }
        })

        viewModel.loading.observe(viewLifecycleOwner, loadingObserver)
    }

    override fun setLoading(loading: Boolean){
        dataBinding.isLoading = loading
    }

}