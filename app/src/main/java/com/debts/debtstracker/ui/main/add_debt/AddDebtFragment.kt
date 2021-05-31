package com.debts.debtstracker.ui.main.add_debt

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.debts.debtstracker.R
import com.debts.debtstracker.databinding.FragmentAddDebtBinding
import com.debts.debtstracker.ui.base.BaseFragment
import org.joda.time.DateTime
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class AddDebtFragment: BaseFragment() {

    private lateinit var dataBinding: FragmentAddDebtBinding
    private val viewModel: AddDebtViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_add_debt,
            container,
            false)

        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDatePicker()
        setupListener()
        attachObservers()
    }

    private fun setupDatePicker(){
        dataBinding.etDate.text = DateTime.now().toString("dd.MM.yyyy")

        val picker = DatePickerDialog(requireContext(), { _, year, monthOfYear, dayOfMonth ->
                dataBinding.etDate.text =  viewModel.updateDate(year, monthOfYear+1, dayOfMonth)
            },
            viewModel.selectedDate.year,
            viewModel.selectedDate.monthOfYear,
            viewModel.selectedDate.dayOfMonth
        )
        picker.show()
    }

    private fun setupListener(){
        dataBinding.btnAdd.setOnClickListener {
            if(dataBinding.etDescription.text.isNullOrEmpty()
                || dataBinding.etSum.text.isNullOrEmpty())
                    dataBinding.tvError.isVisible = true
            else {
                dataBinding.tvError.visibility = View.INVISIBLE
                viewModel.addDebt()
            }
        }
    }


    @SuppressLint("SetTextI18n")
    private fun attachObservers() {
    }


    override fun setLoading(loading: Boolean) {

    }
}