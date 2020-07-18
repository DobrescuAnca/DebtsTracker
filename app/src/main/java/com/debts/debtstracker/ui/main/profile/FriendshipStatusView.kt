package com.debts.debtstracker.ui.main.profile

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.debts.debtstracker.data.network.model.FriendshipStatusEnum
import com.debts.debtstracker.data.network.model.ProfileActionEnum
import com.debts.debtstracker.databinding.ViewFriendshipStatusBinding

class FriendshipStatusView: ConstraintLayout {

    private lateinit var dataBinding: ViewFriendshipStatusBinding

    private fun init(context: Context){
        val inflater = LayoutInflater.from(context)
        dataBinding = ViewFriendshipStatusBinding.inflate(inflater, this, true)
        dataBinding.friendshipStatus = FriendshipStatusEnum.NOT_FRIENDS
    }

    fun setFriendshipStatus(newStatus: FriendshipStatusEnum, callback: (ProfileActionEnum) -> Unit){
        dataBinding.friendshipStatus = newStatus

        setupClickListeners(callback)
    }

    private fun setupClickListeners(callback: (ProfileActionEnum) -> Unit){
        dataBinding.btnAddFriend.setOnClickListener{
            callback(ProfileActionEnum.ADD_FRIEND)
        }
        dataBinding.btAccept.setOnClickListener{
            callback(ProfileActionEnum.ACCEPT_REQUEST)
        }
        dataBinding.btDecline.setOnClickListener{
            callback(ProfileActionEnum.DECLINE_REQUEST)
        }
        dataBinding.btnCancel.setOnClickListener{
            callback(ProfileActionEnum.CANCEL_REQUEST)
        }
        dataBinding.btnFriends.setOnClickListener{
            callback(ProfileActionEnum.REMOVE_FRIEND)
        }
    }

    constructor(context: Context) : super(context){
        init(context)
    }

    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet) {
        init(context)
    }

}