package com.debts.debtstracker.ui.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.debts.debtstracker.R
import com.debts.debtstracker.databinding.ItemNetworkStateBinding

class LoadStateViewHolder(
    val binding: ItemNetworkStateBinding,
): RecyclerView.ViewHolder(binding.root) {

    fun bind(loadState: LoadState){
        if(loadState is LoadState.Error)
            binding.errorMsg.text = loadState.error.localizedMessage

        binding.progressBar.isVisible = loadState is LoadState.Loading
        binding.errorMsg.isVisible = loadState is LoadState.Error
    }

    companion object{
        fun create(parent: ViewGroup): LoadStateViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_network_state, parent, false)
            val binding = ItemNetworkStateBinding.bind(view)
            return LoadStateViewHolder(binding = binding)
        }
    }
}