package com.debts.debtstracker.util

import android.graphics.Typeface
import android.text.InputType
import android.widget.EditText

fun switchPasswordSecureInput(editText: EditText) {
    val referenceTypeface: Typeface? = editText.typeface

    if (editText.inputType == InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD) editText.inputType =
        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD else editText.inputType =
        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
    editText.typeface = referenceTypeface
    editText.setSelection(editText.text.length)
}

