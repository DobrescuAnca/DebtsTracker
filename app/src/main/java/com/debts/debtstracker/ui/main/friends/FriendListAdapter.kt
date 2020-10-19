package com.debts.debtstracker.ui.main.friends

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import com.debts.debtstracker.R
import com.debts.debtstracker.data.network.model.UserModel
import com.debts.debtstracker.databinding.ItemFriendListBinding
import com.debts.debtstracker.util.DataBindingViewHolder
import com.squareup.picasso.Picasso

class FriendListAdapter(
    private val  callback: (String) -> Unit
): ListAdapter<UserModel, DataBindingViewHolder>(DiffUtilUser()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder {
        return DataBindingViewHolder(DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_friend_list,
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: DataBindingViewHolder, position: Int) {
        getItem(position)?.let {
            bind(holder.binding, it)
        }
    }

    private fun bind(binding: ViewDataBinding, user: UserModel) {
        when(binding) {
            is ItemFriendListBinding -> {
                binding.user = user

                Picasso.get().load(user.profilePictureUrl).into(binding.profilePicture)
                binding.root.setOnClickListener {
                    callback(user.id)
                }
            }
        }
    }
}