package com.debts.debtstracker.ui.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.debts.debtstracker.R
import com.debts.debtstracker.databinding.DialogInfoBinding

class InfoDialog(
    private val dialogData: InfoDialogModel
) : DialogFragment() {
    private lateinit var binding: DialogInfoBinding

    private var positiveCallback: (() -> Unit)? = null
    private var negativeCallback: (() -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.dialog_info,
            container,
            false
        )
        binding.lifecycleOwner = viewLifecycleOwner

        dialog?.let {
            it.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupLayout()
        attachClickListeners()
    }

    private fun setupLayout(){
        binding.tvTitle.text = dialogData.title
        binding.tvMessage.text = dialogData.message
        dialogData.positiveBtnText?.let {
            binding.btPositive.text = it
        }
        dialogData.negativeBtnText?.let {
            binding.btNegative.text = it
        }

        showButtons(dialogData.buttonType ?: InfoDialogButtonType.POSITIVE_AND_NEGATIVE)
    }

    private fun attachClickListeners(){
        binding.btPositive.setOnClickListener {
            positiveCallback?.let{ it() }
            dismiss()
        }
        binding.btNegative.setOnClickListener {
            negativeCallback?.let { it() }
            dismiss()
        }
    }

    fun setPositiveAction(positiveCallback: () -> Unit): InfoDialog {
        this.positiveCallback = positiveCallback
        return this
    }

    fun setNegativeAction(negativeAction: () -> Unit): InfoDialog {
        this.negativeCallback = negativeAction
        return this
    }

    private fun showButtons(type: InfoDialogButtonType){
        when(type){
            InfoDialogButtonType.POSITIVE_AND_NEGATIVE -> {
                binding.btPositive.visibility = View.VISIBLE
                binding.btNegative.visibility = View.VISIBLE
            }
            InfoDialogButtonType.ONLY_POSITIVE -> {
                binding.btPositive.visibility = View.VISIBLE
                binding.btNegative.visibility = View.GONE
            }
            InfoDialogButtonType.NO_BUTTONS -> {
                binding.btPositive.visibility = View.GONE
                binding.btNegative.visibility = View.GONE
            }
        }
    }
}

enum class InfoDialogButtonType{
    POSITIVE_AND_NEGATIVE,
    ONLY_POSITIVE,
    NO_BUTTONS
}

data class InfoDialogModel(
    val title: String,
    val message: String,
    val positiveBtnText: String? = null,
    val negativeBtnText: String? = null,
    val buttonType: InfoDialogButtonType? = InfoDialogButtonType.POSITIVE_AND_NEGATIVE
)