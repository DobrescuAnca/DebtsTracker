package com.debts.debtstracker.ui.main.add_debt

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.debts.debtstracker.R
import com.debts.debtstracker.data.ResponseStatus
import com.debts.debtstracker.data.network.model.AddDebtModel
import com.debts.debtstracker.databinding.FragmentAddDebtBinding
import com.debts.debtstracker.ui.base.BaseFragment
import com.debts.debtstracker.util.EventObserver
import com.debts.debtstracker.util.hideKeyboard
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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

        dataBinding.etDate.setOnClickListener {
            val picker = DatePickerDialog(requireContext(), { _, year, monthOfYear, dayOfMonth ->
                dataBinding.etDate.text =  viewModel.updateDate(year, monthOfYear+1, dayOfMonth)
            },
                viewModel.selectedDate.year,
                viewModel.selectedDate.monthOfYear,
                viewModel.selectedDate.dayOfMonth
            )
            picker.datePicker.maxDate = DateTime.now().millis
            picker.show()
        }
    }

    private fun setupListener(){
        dataBinding.btnAdd.setOnClickListener {
            requireContext().hideKeyboard(dataBinding.btnAdd)

            if(dataBinding.etDescription.text.isNullOrEmpty()
                || dataBinding.etSum.text.isNullOrEmpty())
                    dataBinding.tvError.isVisible = true
            else {
                dataBinding.tvError.visibility = View.INVISIBLE
                viewModel.addDebt( AddDebtModel(
                    dataBinding.etSum.text.toString().toDouble(),
                    dataBinding.etDescription.text.toString(),
                    viewModel.selectedDate.millis,
                    !dataBinding.debtType.isChecked
                ))
            }
        }
    }

    private fun showDebtAddedSuccessfully(){
        if(dataBinding.debtAddedSuccessfully != true) {
            dataBinding.debtAddedSuccessfully = true
            lifecycleScope.launch() {
                delay(3000)
                dataBinding.debtAddedSuccessfully = false
            }
        }
    }


    @SuppressLint("SetTextI18n")
    private fun attachObservers() {
        viewModel.addDebtStatus.observe(viewLifecycleOwner, EventObserver{
            if(it is ResponseStatus.Success)
                showDebtAddedSuccessfully()
        })
    }


    override fun setLoading(loading: Boolean) {

    }
}