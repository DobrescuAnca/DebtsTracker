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
            val bundle = Bundle()
            bundle.putInt(UNAUTHORIZED_KEY, intent.getIntExtra(UNAUTHORIZED_KEY, 0))
            navController.setGraph(navController.graph, bundle)
        }
    }
}

const val UNAUTHORIZED_KEY = "unauthorized"