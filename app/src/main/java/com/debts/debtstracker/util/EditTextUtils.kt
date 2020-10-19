package com.debts.debtstracker.util

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.debts.debtstracker.DebtsTrackerApplication

fun Context.hideKeyboard(view: View){
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}


fun Context.openKeyboard(){
    val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
}

fun getString(resource: Int): String {
    return DebtsTrackerApplication.applicationContext().getString(resource)
}



