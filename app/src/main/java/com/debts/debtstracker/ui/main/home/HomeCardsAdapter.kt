package com.debts.debtstracker.ui.main.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.debts.debtstracker.R
import com.debts.debtstracker.data.network.model.DebtStatus
import com.debts.debtstracker.data.network.model.HomeCardModel
import com.debts.debtstracker.databinding.ItemHomeListBinding
import com.debts.debtstracker.util.getCreationDateForCards
import com.debts.debtstracker.util.getString

class HomeCardsAdapter(
    private val callback: (HomeCardModel) -> Unit
): PagingDataAdapter<HomeCardModel, HomeCardsAdapter.ViewHolder>(HOME_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.item_home_list,
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val repoItem = getItem(position)
        if (repoItem != null) {
            holder.item = repoItem
            holder.bind(repoItem)
        }
    }

    inner class ViewHolder(val binding: ItemHomeListBinding): RecyclerView.ViewHolder(binding.root) {
        var item: HomeCardModel? = null

        init {
            binding.root.setOnClickListener {
                item?.let{callback(it) }
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(model: HomeCardModel) {
            val context = binding.root.context

            binding.creationDate.text = getCreationDateForCards(model.creationDate)
            binding.tvSum.text = model.sum.toString().plus(getString(R.string.lei))
            binding.tvDescription.text = model.description

            val statusColor = when(model.status){
                DebtStatus.PAID -> R.color.colorPrimary
                DebtStatus.IN_PROGRESS -> R.color.warning_color
                DebtStatus.DELETED -> R.color.error_color
            }
            binding.ivStatus.imageTintList  = ContextCompat.getColorStateList(context, statusColor)

            if(model.isUserLender)
                binding.ivArrow.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_right_arrow))
            else
                binding.ivArrow.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_arrow_left))
        }
    }

    companion object {
        private val HOME_COMPARATOR = object : DiffUtil.ItemCallback<HomeCardModel>() {
            override fun areItemsTheSame(oldItem: HomeCardModel, newItem: HomeCardModel): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: HomeCardModel, newItem: HomeCardModel): Boolean =
                oldItem == newItem
        }
    }
}