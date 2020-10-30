package com.debts.debtstracker.ui.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.debts.debtstracker.R
import com.debts.debtstracker.databinding.DialogInfoBinding

class InfoDialog : DialogFragment() {
    private lateinit var binding: DialogInfoBinding

    var type: DialogType? = null
        private set

    private var infoDialogPositiveCallback: (() -> Unit)? = null
    private var infoDialogNegativeCallback: (() -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val args = arguments ?: return null

        type = DialogType.getDialogType(
            args.getInt(
                DIALOG_TYPE,
                DialogType.INFORMATION.type
            )
        )

        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_info, container, false)
        dialog?.let { it ->
            it.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            isCancelable = false
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = arguments

        var title = args?.getString(TITLE, "")
        val message = args?.getString(MESSAGE, "")

        title = getTitleAndCustomizeDialogView(title ?: "")

        binding.tvTitle.text = title
        binding.tvMessage.text = message

        if (!TextUtils.isEmpty(args?.getString(POSITIVE_BT_TEXT)))
            binding.tvOk.text = args?.getString(POSITIVE_BT_TEXT)

        if (!TextUtils.isEmpty(args?.getString(NEGATIVE_BT_TEXT)))
            binding.tvCancel.text = args?.getString(NEGATIVE_BT_TEXT)

        binding.tvOk.setOnClickListener {
            dismiss()
            infoDialogPositiveCallback?.invoke()
        }

        binding.tvCancel.setOnClickListener {
            dismiss()
            infoDialogNegativeCallback?.invoke()
        }
    }

    fun setPositiveAction(infoDialogPositiveCallback: (() -> Unit)?): InfoDialog {
        this.infoDialogPositiveCallback = infoDialogPositiveCallback
        return this
    }

    fun setNegativeAction(negativeAction: (() -> Unit)?): InfoDialog {
        this.infoDialogNegativeCallback = negativeAction
        return this
    }

    private fun getTitleAndCustomizeDialogView(title: String): String {
        if (TextUtils.isEmpty(title))
            return when (type) {
                DialogType.INFORMATION -> resources.getString(R.string.info)
                DialogType.CONFIRMATION -> {
                    binding.tvCancel.visibility = View.GONE
                    resources.getString(R.string.confirm)
                }
                else -> resources.getString(R.string.info)
            }

        return title
    }

    companion object {
        const val DIALOG_TYPE = "dialog type"
        const val TITLE = "title"
        const val MESSAGE = "message"
        const val POSITIVE_BT_TEXT = "positive button text"
        const val NEGATIVE_BT_TEXT = "negative button text"

        fun createInstance(
            dialogType: DialogType,
            title: String = "",
            message: String = ""
        ): InfoDialog {
            val args = Bundle()

            args.putInt(DIALOG_TYPE, dialogType.type)
            args.putString(TITLE, title)
            args.putString(MESSAGE, message)

            val dialog = InfoDialog()
            dialog.setStyle(STYLE_NO_TITLE, 0)
            dialog.arguments = args
            dialog.isCancelable = false
            dialog.type = dialogType
            return dialog
        }
    }
}

enum class DialogType(val type: Int) {
    INFORMATION(1),
    CONFIRMATION(2);

    companion object {
        @JvmStatic
        fun getDialogType(typeToFind: Int): DialogType =
            values().find { value -> value.type == typeToFind } ?: INFORMATION
    }
}