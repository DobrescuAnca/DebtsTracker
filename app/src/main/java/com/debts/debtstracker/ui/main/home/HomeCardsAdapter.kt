package com.debts.debtstracker.ui.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.debts.debtstracker.R
import com.debts.debtstracker.data.network.model.HomeCardModel
import com.debts.debtstracker.databinding.ItemHomeListBinding
import com.debts.debtstracker.util.DataBindingViewHolder

class HomeCardsAdapter(
    private val callback: () -> Unit
): ListAdapter<HomeCardModel, DataBindingViewHolder>(DiffUtilHome()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder {
        return DataBindingViewHolder(DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_home_list,
            parent,
            false
        ))
    }


    override fun onBindViewHolder(holder: DataBindingViewHolder, position: Int) {
        getItem(position)?.let { bind(holder.binding, it) }
    }

    private fun bind(binding: ViewDataBinding, item: HomeCardModel) {
        when(binding) {
            is ItemHomeListBinding -> {
                binding.model = item
            }
        }
    }

}


private class DiffUtilHome : DiffUtil.ItemCallback<HomeCardModel>() {
    override fun areItemsTheSame(
        oldItem: HomeCardModel,
        newItem: HomeCardModel
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: HomeCardModel,
        newItem: HomeCardModel
    ): Boolean {
        return oldItem.id == newItem.id
                &&  oldItem.creationDate == newItem.creationDate
                &&  oldItem.userId == newItem.userId
                &&  oldItem.homeCardType == newItem.homeCardType
                &&  oldItem.text == newItem.text
                &&  oldItem.otherUserId == newItem.otherUserId
                &&  oldItem.sum == newItem.sum
    }
}