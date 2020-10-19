package com.debts.debtstracker.ui.main.home

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.debts.debtstracker.R
import com.debts.debtstracker.data.network.model.HomeCardModel
import com.debts.debtstracker.databinding.ItemHomeListBinding
import com.debts.debtstracker.databinding.ItemNetworkStateBinding
import com.debts.debtstracker.util.*
import com.squareup.picasso.Picasso

class HomeCardsAdapter(
    private val callback: (HomeCardModel) -> Unit
): BasePagedListAdapter<HomeCardModel>(R.layout.item_home_list , DiffUtilHome()) {


    override fun bind(binding: ViewDataBinding, item: HomeCardModel?, position: Int) {
        item?.let { homeCard ->
            when(binding) {
                is ItemHomeListBinding -> {
                    binding.model = homeCard
                    Picasso.get().load(homeCard.otherUserProfilePictureUrl).into(binding.notificationPicture)

                    binding.creationDate.text = when(getCreationDateType(homeCard.creationDate)){
                        MINUTES_PAST_CREATION ->
                            getMinutesDate(System.currentTimeMillis()- homeCard.creationDate)
                                .toString()
                                .plus(getString(R.string.minutes_ago))
                        HOURS_PAST_CREATION ->
                            getHourDate(System.currentTimeMillis()- homeCard.creationDate)
                                .toString()
                                .plus(getString(R.string.hours_ago))
                        DAYS_PAST_CREATION -> getStringDate(homeCard.creationDate)
                        else -> ""
                    }

                    binding.tvSum.text = homeCard.sum.toString().plus(getString(R.string.lei))
                    binding.isDebt = homeCard.debtId != null

                    binding.root.setOnClickListener { callback(homeCard) }
                }

                is ItemNetworkStateBinding -> {
                    binding.networkState = loadingState
                }
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
                &&  oldItem.otherUserId == newItem.otherUserId
                &&  oldItem.otherUserProfilePictureUrl == newItem.otherUserProfilePictureUrl
                &&  oldItem.text == newItem.text
                &&  oldItem.otherUserId == newItem.otherUserId
                &&  oldItem.sum == newItem.sum
    }
}