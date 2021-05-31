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
import com.debts.debtstracker.databinding.DialogProfileBinding
import com.debts.debtstracker.ui.main.MainViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ProfileDialog: DialogFragment() {

    private lateinit var binding: DialogProfileBinding
    private val viewModel: MainViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.dialog_profile,
            container,
            false
        )
        binding.lifecycleOwner = viewLifecycleOwner

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        attachObservers()

        binding.btnEquate.setOnClickListener {

        }

        binding.tvLogout.setOnClickListener {

        }
    }

    private fun attachObservers(){
        
    }

    private fun setLoading(state: Boolean){
        binding.isLoading = state
    }
}