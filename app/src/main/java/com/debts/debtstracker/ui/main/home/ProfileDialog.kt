package com.debts.debtstracker.ui.main.home

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.debts.debtstracker.R
import com.debts.debtstracker.data.ResponseStatus
import com.debts.debtstracker.databinding.DialogProfileBinding
import com.debts.debtstracker.ui.main.MainViewModel
import com.debts.debtstracker.util.EventObserver
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ProfileDialog: DialogFragment() {

    private lateinit var dataBinding: DialogProfileBinding
    private val viewModel: MainViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.dialog_profile,
            container,
            false
        )
        dataBinding.lifecycleOwner = viewLifecycleOwner

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        attachObservers()
        viewModel.getProfile()
        viewModel.getTotals()

        dataBinding.btnEquate.setOnClickListener {
            viewModel.equate()
        }

        dataBinding.tvLogout.setOnClickListener {

        }
    }

    private fun attachObservers(){
        viewModel.loading.observe(viewLifecycleOwner, EventObserver{
            when (it) {
                is ResponseStatus.Loading -> setLoading(true)
                else -> setLoading(false)
            }
        })

        viewModel.totals.observe(viewLifecycleOwner){
            dataBinding.tvBorrowedSum.text = it.totalBorrowed.toString()
            dataBinding.tvLentSum.text = it.totalLent.toString()
        }

        viewModel.profileData.observe(viewLifecycleOwner){
            dataBinding.tvUserName.text = it.name
        }
    }

    private fun setLoading(state: Boolean){
        dataBinding.isLoading = state
    }
}