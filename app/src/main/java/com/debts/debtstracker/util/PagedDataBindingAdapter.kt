package com.debts.debtstracker.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.debts.debtstracker.R
import com.debts.debtstracker.data.NetworkState

abstract class BasePagedListAdapter<T>(
    private val itemLayoutId: Int,
    diffUtil: DiffUtil.ItemCallback<T>
) : PagedListAdapter<T, DataBindingViewHolder>(diffUtil) {

    var loadingState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder {
        val binding: ViewDataBinding = createBinding(parent, viewType)

        return DataBindingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataBindingViewHolder, position: Int) {
        if (position < itemCount) {
            when (getItemViewType(position)) {
                REQUEST_VIEW_TYPE -> {
                    getItem(position)?.let { bind(holder.binding, it, position) }
                }
                LOADING_VIEW_TYPE -> {
                    bind(holder.binding, null, position)
                }
            }
        }
    }

    protected abstract fun bind(binding: ViewDataBinding, item: T?, position: Int)

    private fun createBinding(parent: ViewGroup, viewType: Int): ViewDataBinding {
        return when (viewType) {
            REQUEST_VIEW_TYPE -> DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                itemLayoutId,
                parent,
                false
            )
            LOADING_VIEW_TYPE -> DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_network_state,
                parent,
                false
            )
            else -> throw IllegalArgumentException("unknown view type $viewType")
        }
    }

    private fun hasExtraRow() = loadingState != null && loadingState != NetworkState.LOADED

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            LOADING_VIEW_TYPE
        } else {
            REQUEST_VIEW_TYPE
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
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

class DataBindingViewHolder(val binding: ViewDataBinding) :
    RecyclerView.ViewHolder(binding.root) {
}

const val REQUEST_VIEW_TYPE = 1
const val LOADING_VIEW_TYPE = 0