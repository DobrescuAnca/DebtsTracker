package com.debts.debtstracker.ui.login

import android.os.Bundle
import androidx.navigation.findNavController
import com.debts.debtstracker.R
import com.debts.debtstracker.ui.base.BaseActivity

class OnboardingActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_onboarding)

        navController = findNavController(R.id.onboarding_nav_host_fragment)

        intent.extras?.let {
            navController.graph = navController.graph
        }
    }
}
