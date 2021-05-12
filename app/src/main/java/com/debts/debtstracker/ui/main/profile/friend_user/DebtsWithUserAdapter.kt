package com.debts.debtstracker.ui.main.profile.friend_user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.debts.debtstracker.R
import com.debts.debtstracker.data.NetworkState
import com.debts.debtstracker.data.network.model.FriendDebtModel
import com.debts.debtstracker.util.*

class DebtsWithUserAdapter(
    private val callback:(String) -> Unit
): PagedListAdapter<FriendDebtModel, DataBindingViewHolder>(DiffUtil()) {
    var loadingState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder {
        val binding: ViewDataBinding = createBinding(parent, viewType)

        return DataBindingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataBindingViewHolder, position: Int) {
        if (position < itemCount)
            when (getItemViewType(position)) {
                REQUEST_VIEW_TYPE -> getItem(position)?.let { bind(holder.binding, it, position) }
                LOADING_VIEW_TYPE -> bind(holder.binding, null, position)
            }

    }

    private fun createBinding(parent: ViewGroup, viewType: Int): ViewDataBinding {
        val layoutId = when (viewType) {
            MONEY_LENT_VIEW_TYPE -> R.layout.item_money_lent
            MONEY_BORROWED_VIEW_TYPE -> R.layout.item_money_borrowed
            LOADING_VIEW_TYPE -> R.layout.item_network_state

            else -> R.layout.item_network_state
         }

        return  DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            layoutId,
            parent,
            false
        )
    }

    private fun hasExtraRow() = loadingState != null && loadingState != NetworkState.LOADED

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            LOADING_VIEW_TYPE
        } else {
//            if((this.currentList[position] as FriendDebtModel).borrowerId)
            REQUEST_VIEW_TYPE
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    fun bind(binding: ViewDataBinding, item: FriendDebtModel?, position: Int){

    }

    fun setNetworkStateValue(newNetworkState: NetworkState?) {
        val previousState = this.loadingState
        val hadExtraRow = hasExtraRow()
        this.loadingState = newNetworkState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }
}

const val MONEY_LENT_VIEW_TYPE = 1
const val MONEY_BORROWED_VIEW_TYPE = 2

class DiffUtil : DiffUtil.ItemCallback<FriendDebtModel>() {
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