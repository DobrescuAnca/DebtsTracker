package com.debts.debtstracker.ui.main

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.debts.debtstracker.R
import com.debts.debtstracker.data.local.LocalPreferencesInterface
import com.debts.debtstracker.databinding.ActivityMainBinding
import com.debts.debtstracker.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject

class MainActivity : BaseActivity(){

    private lateinit var dataBinding: ActivityMainBinding
    private val sharedPrefs: LocalPreferencesInterface by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        navController = findNavController(R.id.main_nav_host_fragment)

        setMenu()

        dataBinding.floatingAdd.setOnClickListener {
            navController.navigate(R.id.action_global_addDebtFragment)
            hideNavBar(true)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        if(navController.currentDestination?.id != R.id.addDebtFragment || navController.currentDestination?.id != R.id.profileFragment)
            hideNavBar(false)
        else
            hideNavBar(true)
    }

    private fun setMenu(){
        bottomAppBar.setOnMenuItemClickListener{
            when(it.itemId){
                R.id.action_home -> {
                    if(navController.currentDestination?.id != R.id.homeFragment)
                        navController.navigate(R.id.action_global_homeFragment)

                    true
                }
                R.id.action_friends -> {
                    if(navController.currentDestination?.id != R.id.friendsFragment)
                        navController.navigate(R.id.action_global_friendsFragment)
                    true
                }
                else -> true
            }
        }
    }

    fun hideNavBar(hide: Boolean) {
        if (hide) {
            dataBinding.floatingAdd.hide()
            dataBinding.bottomAppBar.performHide()
        } else {
            dataBinding.floatingAdd.show()
            dataBinding.bottomAppBar.performShow()
        }
    }

    fun logout(){
        //delete shared prefs
        sharedPrefs.clearSharedPrefs()

//        navController.navigate(R.id.loginFragment)
//        finish()
    }
}