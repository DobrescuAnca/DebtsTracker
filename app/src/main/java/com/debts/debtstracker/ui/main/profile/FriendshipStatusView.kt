package com.debts.debtstracker.ui.main.profile

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.debts.debtstracker.data.network.model.FriendshipStatus
import com.debts.debtstracker.databinding.ViewFriendshipStatusBinding

class FriendshipStatusView: ConstraintLayout {

    private lateinit var dataBinding: ViewFriendshipStatusBinding

    private fun init(context: Context){
        val inflater = LayoutInflater.from(context)
        dataBinding = ViewFriendshipStatusBinding.inflate(inflater, this, true)
        dataBinding.friendshipStatus = FriendshipStatus.NOT_FRIENDS
    }

    fun setFriendshipStatus(newStatus: FriendshipStatus){
        dataBinding.friendshipStatus = newStatus
    }

    constructor(context: Context) : super(context){
        init(context)
    }

    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet) {
        init(context)
    }
}