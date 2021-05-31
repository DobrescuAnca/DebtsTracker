package com.debts.debtstracker.ui.base

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.navigation.NavController
import com.debts.debtstracker.R
import com.debts.debtstracker.ui.dialogs.InfoDialog
import com.debts.debtstracker.ui.dialogs.InfoDialogButtonType
import com.debts.debtstracker.ui.dialogs.InfoDialogModel

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

    fun showErrorDialog(errorMessage: Int){
        val data = InfoDialogModel(
            title = resources.getString(R.string.error),
            message = getString(errorMessage),
            positiveBtnText = getString(R.string.ok),
            buttonType = InfoDialogButtonType.ONLY_POSITIVE
        )

        this.showCustomDialog(InfoDialog(data))
    }

    companion object {
        const val DIALOG_TAG = "dialog tag"
    }
}