package com.debts.debtstracker.ui.base

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.navigation.NavController

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {
    lateinit var navController: NavController

    fun showCustomDialog(currentDialog: DialogFragment) {
        if (isFinishing) {
            return
        }

        val previousDialog =
            supportFragmentManager.findFragmentByTag(DIALOG_TAG) as DialogFragment?

        previousDialog?.dismiss()

        currentDialog.show(supportFragmentManager, DIALOG_TAG)
    }

    companion object {
        const val DIALOG_TAG = "dialog tag"
    }
}