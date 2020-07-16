package com.debts.debtstracker.ui.main.friends

import android.content.Context
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.debts.debtstracker.R
import com.debts.debtstracker.data.network.model.FriendshipStatus
import com.debts.debtstracker.data.network.model.UserModel
import com.debts.debtstracker.databinding.ItemFriendListBinding
import com.debts.debtstracker.databinding.ItemNetworkStateBinding
import com.debts.debtstracker.databinding.ItemUserListBinding
import com.debts.debtstracker.util.BasePagedListAdapter
import com.squareup.picasso.Picasso

class UserListAdapter(
    private val callback:(String) -> Unit,
    layout: Int,
    private val context: Context
): BasePagedListAdapter<UserModel>(layout, DiffUtilUser()) {

    override fun bind(binding: ViewDataBinding, item: UserModel?, position: Int) {
        item?.let { user ->
            when(binding){
                is ItemFriendListBinding -> {
                    binding.user = user

                    Picasso.with(context).load(user.profilePictureUrl).into(binding.profilePicture)
                    binding.root.setOnClickListener {
                        callback(user.id)
                    }
                }
                is ItemUserListBinding -> {
                    binding.user = user

                    Picasso.with(context).load(user.profilePictureUrl).into(binding.profilePicture)

                    val drawable = when(user.friendshipStatus){
                        FriendshipStatus.FRIENDS -> R.drawable.ic_people
                        FriendshipStatus.NOT_FRIENDS -> R.drawable.ic_add_friend
                        FriendshipStatus.REQUEST_RECEIVED -> R.drawable.ic_add_friend
                        FriendshipStatus.REQUEST_SENT -> R.drawable.ic_add_friend
                    }

                    binding.ivRelationshipStatus.setBackgroundResource(drawable)

                    binding.root.setOnClickListener {
                        callback(user.id)
                    }
                }
                is ItemNetworkStateBinding -> {
                    binding.networkState = loadingState
                }
            }
        }
    }
}

class DiffUtilUser : DiffUtil.ItemCallback<UserModel>() {
    override fun areItemsTheSame(
        oldItem: UserModel,
        newItem: UserModel
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: UserModel,
        newItem: UserModel
    ): Boolean {
        return oldItem.id == newItem.id
                && oldItem.name == newItem.name
                && oldItem.username == newItem.username
                && oldItem.profilePictureUrl == newItem.profilePictureUrl
                && oldItem.totalToPay == newItem.totalToPay
                && oldItem.totalToReceive == newItem.totalToReceive
    }
}