package com.debts.debtstracker.ui.main

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import com.debts.debtstracker.R
import com.debts.debtstracker.databinding.ActivityMainBinding
import com.debts.debtstracker.ui.base.BaseActivity
import com.debts.debtstracker.util.hideKeyboard

class MainActivity: BaseActivity(){

    private lateinit var dataBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        navController = findNavController(R.id.main_nav_host_fragment)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            onDestinationChanged(destination)
        }

        dataBinding.floatingAdd.setOnClickListener {
            navController.navigate(R.id.action_global_addDebtFragment)
        }
    }

    private fun onDestinationChanged(destination: NavDestination){
        hideKeyboard(dataBinding.root)

        when(destination.id){
            R.id.homeFragment -> dataBinding.floatingAdd.show()
            else -> dataBinding.floatingAdd.hide()

        }
    }
}