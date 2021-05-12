package com.debts.debtstracker.ui.main.friends

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.debts.debtstracker.R
import com.debts.debtstracker.data.network.model.FriendshipStatusEnum
import com.debts.debtstracker.data.network.model.UserModel
import com.debts.debtstracker.databinding.ItemNetworkStateBinding
import com.debts.debtstracker.databinding.ItemUserListBinding
import com.debts.debtstracker.util.BasePagedListAdapter
import com.squareup.picasso.Picasso

class UserListAdapter(
    private val callback:(String) -> Unit
): BasePagedListAdapter<UserModel>(R.layout.item_user_list, DiffUtilUser()) {

    override fun bind(binding: ViewDataBinding, item: UserModel?, position: Int) {
        item?.let { user ->
            when(binding){
                is ItemUserListBinding -> {
                    binding.user = user

                    Picasso.get().load(user.profilePictureUrl).into(binding.profilePicture)

                    val drawable = when(user.friendshipStatus){
                        FriendshipStatusEnum.FRIENDS -> R.drawable.ic_people
                        FriendshipStatusEnum.NOT_FRIENDS -> R.drawable.ic_add_friend
                        FriendshipStatusEnum.REQUEST_RECEIVED -> R.drawable.ic_add_friend
                        FriendshipStatusEnum.REQUEST_SENT -> R.drawable.ic_add_friend
                        FriendshipStatusEnum.OWN_PROFILE -> R.drawable.ic_people
                        else -> R.drawable.ic_add_friend
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
        return oldItem == newItem
    }
}