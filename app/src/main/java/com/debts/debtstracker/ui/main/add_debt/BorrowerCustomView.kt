package com.debts.debtstracker.ui.main.add_debt

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import com.debts.debtstracker.DebtsTrackerApplication
import com.debts.debtstracker.R
import com.debts.debtstracker.data.network.model.BorrowerDebtModel
import com.debts.debtstracker.databinding.ItemBorrowerListBinding

class BorrowerCustomView: FrameLayout {

    private lateinit var dataBinding: ItemBorrowerListBinding
    private var index: Int = 0

    private fun init(context: Context){
        dataBinding = ItemBorrowerListBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun setupView(borrowerNumber: Int, friendList: List<String>){
        index = borrowerNumber
        dataBinding.tvIndex.text = (borrowerNumber + 1).toString().plus(".")

        ArrayAdapter(
            context ?: DebtsTrackerApplication.applicationContext(),
            R.layout.item_spinner_dropdown,
            R.id.tv_simple,
            friendList
        ).also { adapter ->
            dataBinding.spinnerBorrowers.adapter = adapter
        }
    }

    fun getSelectedBorrower(): BorrowerDebtModel? {
        var sum: String = dataBinding.etSum.text.toString()
        if(sum.isEmpty())
            sum = "0.0"

        return BorrowerDebtModel(
            dataBinding.spinnerBorrowers.selectedItemId.toInt().toString(),
            sum.toFloat()
        )
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