package com.debts.debtstracker.ui.main.profile

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.debts.debtstracker.R
import com.debts.debtstracker.data.network.model.FriendDebtModel
import com.debts.debtstracker.databinding.ItemFriendDebtListBinding
import com.debts.debtstracker.databinding.ItemNetworkStateBinding
import com.debts.debtstracker.util.BasePagedListAdapter

class FriendDebtsListAdapter(
    private val callback:(Int) -> Unit
): BasePagedListAdapter<FriendDebtModel>(R.layout.item_friend_debt_list, DiffUtilUser()) {

    override fun bind(binding: ViewDataBinding, item: FriendDebtModel?, position: Int) {
        item?.let {
            when(binding){
                is ItemFriendDebtListBinding -> {
                    binding.debt = it

                    binding.root.setOnClickListener { _ ->
                        callback(it.id)
                    }
                }
                is ItemNetworkStateBinding -> {
                    binding.networkState = loadingState
                }
            }
        }
    }
}

class DiffUtilUser : DiffUtil.ItemCallback<FriendDebtModel>() {
    override fun areItemsTheSame(
        oldItem: FriendDebtModel,
        newItem: FriendDebtModel
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: FriendDebtModel,
        newItem: FriendDebtModel
    ): Boolean {
        return oldItem.id == newItem.id
    }
}