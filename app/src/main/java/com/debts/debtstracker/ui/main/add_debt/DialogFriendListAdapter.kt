package com.debts.debtstracker.ui.main.add_debt

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import com.debts.debtstracker.R
import com.debts.debtstracker.data.network.model.UserModel
import com.debts.debtstracker.databinding.ItemFriendListSimpleBinding
import com.debts.debtstracker.ui.main.friends.DiffUtilUser
import com.debts.debtstracker.util.DataBindingViewHolder
import com.squareup.picasso.Picasso

class DialogFriendListAdapter(
    private val callback: (UserModel) -> Unit
):  ListAdapter<UserModel, DataBindingViewHolder>(DiffUtilUser())  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder {
        return DataBindingViewHolder(
            DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_friend_list_simple,
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: DataBindingViewHolder, position: Int) {
        getItem(position)?.let {
            val isLastElement = position == itemCount - 1
            bind(holder.binding, it, isLastElement)
        }
    }

    private fun bind(binding: ViewDataBinding, user: UserModel, isLastElement: Boolean){
        if(binding is ItemFriendListSimpleBinding) {
            binding.tvName.text = user.name
            Picasso.get().load(user.profilePictureUrl).into(binding.profilePicture)
            binding.root.setOnClickListener { callback(user) }

            if(isLastElement)
                binding.separationLine.visibility = View.GONE
            else
                binding.separationLine.visibility = View.VISIBLE
        }
    }
}