package com.debts.debtstracker.ui.base

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {
    lateinit var navController: NavController

}