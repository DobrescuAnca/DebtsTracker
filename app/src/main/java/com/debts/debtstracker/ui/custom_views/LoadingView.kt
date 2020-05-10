package com.debts.debtstracker.ui.custom_views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.debts.debtstracker.databinding.ViewLoadingBinding

class LoadingView: LinearLayout {

    private lateinit var binding: ViewLoadingBinding

    private fun init(context: Context){
        val inflater = LayoutInflater.from(context)
        binding = ViewLoadingBinding.inflate(inflater, this, true)
    }

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }
}