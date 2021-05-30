package com.debts.debtstracker.ui.main

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.debts.debtstracker.R
import com.debts.debtstracker.databinding.ActivityMainBinding
import com.debts.debtstracker.ui.base.BaseActivity

class MainActivity : BaseActivity(){

    private lateinit var dataBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        navController = findNavController(R.id.main_nav_host_fragment)

        dataBinding.floatingAdd.setOnClickListener {
            navController.navigate(R.id.action_global_addDebtFragment)
            hideNavBar(true)
        }
    }

    fun hideNavBar(hide: Boolean) {
        if (hide)
            dataBinding.floatingAdd.hide()
        else
            dataBinding.floatingAdd.show()

    }
}