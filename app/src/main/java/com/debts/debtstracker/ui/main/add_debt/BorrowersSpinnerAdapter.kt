package com.debts.debtstracker.ui.main.add_debt

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.debts.debtstracker.R
import com.debts.debtstracker.data.network.model.UserModel
import kotlinx.android.synthetic.main.item_spinner_dropdown.view.*

class BorrowersSpinnerAdapter (
    context: Context,
    borrowersList: List<UserModel>
): ArrayAdapter<UserModel>(context, R.layout.item_spinner_dropdown, borrowersList){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createView(position, convertView, parent)
    }

    private fun createView(position: Int, recycledView: View?, parent:ViewGroup?): View {
        val borrower = getItem(position)

        val view = recycledView ?: LayoutInflater.from(context).inflate(
            R.layout.item_spinner_dropdown,
            parent,
            false
        )

        view.tv_simple.text = borrower?.name

        return view
    }
}