package com.debts.debtstracker.ui.custom_views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.debts.debtstracker.R
import com.debts.debtstracker.databinding.ViewIconRoundedBinding

class RoundedIconTextView: LinearLayout {

    private lateinit var dataBinding: ViewIconRoundedBinding
    private var viewIsSelected = false

    private fun init(context: Context, attrs: AttributeSet ?= null){
        val inflater = LayoutInflater.from(context)
        dataBinding = ViewIconRoundedBinding.inflate(inflater, this, true)

        val viewTitle: String?

        attrs?.let {
            context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.RoundedIconTextView,
                0, 0
            ).apply {

                try {
                    viewTitle = getString(R.styleable.RoundedIconTextView_viewTitle)
                    dataBinding.tvTitle.text = viewTitle

                    dataBinding.tvIcon.setImageDrawable(getDrawable(R.styleable.RoundedIconTextView_iconResource))
                } finally {
                    recycle()
                }
            }
        }
    }

    fun selectView(select: Boolean){
        if(select)
            dataBinding.viewContainer.backgroundTintList = context.resources.getColorStateList(R.color.colorAccent, context.theme)
        else
            dataBinding.viewContainer.backgroundTintList = context.resources.getColorStateList(R.color.colorLightGrey, context.theme)

        viewIsSelected = select
    }

    fun getViewIsSelected(): Boolean { return viewIsSelected}

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