package com.debts.debtstracker.ui.custom_views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.debts.debtstracker.R
import com.debts.debtstracker.databinding.ViewSuccessMessageBinding

class SuccessFixedMessageView: LinearLayout {

    private lateinit var dataBinding: ViewSuccessMessageBinding

    private fun init(context: Context, attrs: AttributeSet?=null ){
        dataBinding = ViewSuccessMessageBinding.inflate(LayoutInflater.from(context), this, true)

        context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.SuccessMessageView,
                0, 0)
                .apply {
                    try {
                        dataBinding.tvTitle.text = getString(R.styleable.SuccessMessageView_successMessageTitle)
                    } finally {
                        recycle()
                    }
                }
    }

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }
}