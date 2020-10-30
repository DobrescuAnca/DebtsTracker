package com.debts.debtstracker.ui.main.add_debt

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.debts.debtstracker.data.network.model.BorrowerDebtModel
import com.debts.debtstracker.data.network.model.UserModel
import com.debts.debtstracker.databinding.ItemBorrowerListBinding
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_add_debt.view.*

class BorrowerCustomView: FrameLayout{

    private lateinit var dataBinding: ItemBorrowerListBinding
    private var index: Int = 0
    private var selectedUser: UserModel? = null

    private fun init(context: Context){
        dataBinding = ItemBorrowerListBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun setupView(viewIndex: Int, callback: (String, Int) -> Unit){
        this.index = viewIndex

        dataBinding.userName.setOnClickListener {
            callback(SELECT_FRIEND, index)
        }
    }

    fun setSelectedUser(user: UserModel){
        selectedUser = user
        dataBinding.userName.text = user.name

        Picasso.get().load(user.profilePictureUrl).into(profilePicture)
    }

    fun updateIndex(newIndex: Int){
        index = newIndex
    }

    fun getSelectedUser(): UserModel?{
        return selectedUser
    }

    fun getSelectedBorrower(): BorrowerDebtModel {
        var sum: String = dataBinding.etSum.text.toString()
        if(sum.isEmpty())
            sum = "0.0"

        return BorrowerDebtModel(
            selectedUser?.id ?: "0",
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

    companion object {
        const val SELECT_FRIEND = "select friend"
        const val DELETE_VIEW = "delete view"
    }
}