package com.debts.debtstracker.ui.main

import android.os.Bundle
import androidx.navigation.findNavController
import com.debts.debtstracker.R
import com.debts.debtstracker.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        navController = findNavController(R.id.main_nav_host_fragment)

        setMenu()
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

}