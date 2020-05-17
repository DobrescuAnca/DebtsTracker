package com.debts.debtstracker.util

import android.app.Activity
import android.content.Context
import android.graphics.Typeface
import android.text.InputType
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText


fun switchPasswordSecureInput(editText: EditText) {
    val referenceTypeface: Typeface? = editText.typeface

    if (editText.inputType == InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD) editText.inputType =
        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD else editText.inputType =
        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
    editText.typeface = referenceTypeface
    editText.setSelection(editText.text.length)
}

fun hideKeyboard(activity: Activity){
    val imm: InputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    var view: View? = activity.currentFocus

    if(view == null)
        view = View(activity)

    if (imm.isActive)
        imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Context.hideKeyboard(view: View){
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}


fun Context.openKeyboard(){
    val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
}

