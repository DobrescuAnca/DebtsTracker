package com.debts.debtstracker.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.debts.debtstracker.R
import com.debts.debtstracker.data.Status
import com.debts.debtstracker.data.network.model.HomeCardModel
import com.debts.debtstracker.databinding.FragmentHomeBinding
import com.debts.debtstracker.ui.base.BaseFragment
import com.debts.debtstracker.util.EventObserver
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class HomeFragment: BaseFragment() {

    private lateinit var dataBinding: FragmentHomeBinding
    private val viewModel: HomeViewModel by sharedViewModel()

    private lateinit var adapter: HomeCardsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_home,
            container,
            false)

        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setLoading(true)
        initAdapter()

        setupLayout()
        attachObservers()

    }

    private fun setupLayout(){
    }

    private fun initAdapter(){
        adapter = HomeCardsAdapter(
            this::onCardClicked
        )
        dataBinding.homeCardContainer.adapter = adapter
    }

    private fun onCardClicked(homeCard: HomeCardModel){

    }

    private fun attachObservers() {
        viewModel.content.observe(
            viewLifecycleOwner, {
                adapter.submitList(it)
            })

        viewModel.networkState.observe(viewLifecycleOwner, EventObserver {
            if(it.status == Status.EMPTY_LIST)
                dataBinding.tvEmptyList.visibility = View.VISIBLE
            else {
                adapter.setNetworkStateValue(it)
                dataBinding.tvEmptyList.visibility = View.GONE
            }
        })
    }

    override fun setLoading(loading: Boolean) {

    }

}