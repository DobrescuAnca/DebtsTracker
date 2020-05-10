package com.debts.debtstracker.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.debts.debtstracker.R
import com.debts.debtstracker.data.local.LocalPreferencesInterface
import com.debts.debtstracker.databinding.FragmentSplashBinding
import com.debts.debtstracker.ui.base.BaseFragment
import org.koin.android.ext.android.inject

class SplashFragment: BaseFragment() {

    private lateinit var dataBinding: FragmentSplashBinding
    private val sharedPrefs: LocalPreferencesInterface by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_splash,
            container,
            false
        )

        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val refreshToken = sharedPrefs.getRefreshToken()
        if (refreshToken == null)
//            findNavController().navigate(R.id.)
//        else
            findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
    }

}