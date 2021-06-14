package com.debts.debtstracker.ui.main.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.debts.debtstracker.R
import com.debts.debtstracker.data.local.LocalPreferencesInterface
import com.debts.debtstracker.data.network.model.HomeCardModel
import com.debts.debtstracker.databinding.FragmentHomeBinding
import com.debts.debtstracker.ui.base.BaseActivity
import com.debts.debtstracker.ui.base.BaseFragment
import com.debts.debtstracker.ui.base.LoadStateAdapter
import com.debts.debtstracker.ui.main.MainActivity
import com.debts.debtstracker.ui.main.MainViewModel
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class HomeFragment: BaseFragment() {

    private lateinit var dataBinding: FragmentHomeBinding
    private val viewModel: MainViewModel by sharedViewModel()
    private val sharedPrefs: LocalPreferencesInterface by inject()

    private lateinit var adapter: HomeCardsAdapter
    private var filterJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_home,
            container,
            false)

        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        verifyToken()
        initAdapter()

        viewModel.getProfile()
        setupFilterListeners()

        setupLayout()
        attachObservers()
    }

    private fun setupLayout() {
        // Scroll to top when the list is refreshed from network.
        lifecycleScope.launch {
            adapter.loadStateFlow
                // Only emit when REFRESH LoadState for RemoteMediator changes.
                .distinctUntilChangedBy { it.refresh }
                // Only react to cases where Remote REFRESH completes i.e., NotLoading.
                .filter { it.refresh is LoadState.NotLoading }
                .collect { dataBinding.homeCardContainer.scrollToPosition(0) }
        }

        dataBinding.searchView.currentUserProfilePicture.setOnClickListener {
            (activity as BaseActivity).showCustomDialog(
                ProfileDialog(),
            )
        }
    }

    private fun setupFilterListeners(){
        dataBinding.allFilter.selectView(true)
        filter("All")

        dataBinding.borrowedFilter.setOnClickListener {
            deselectFilters()
            dataBinding.borrowedFilter.selectView(true)
            filter("Borrowed")
        }
        dataBinding.allFilter.setOnClickListener {
            deselectFilters()
            dataBinding.allFilter.selectView(true)
            filter("All")
        }
        dataBinding.lentFilter.setOnClickListener {
            deselectFilters()
            dataBinding.lentFilter.selectView(true)
            filter("Lent")
        }
        dataBinding.moreFilter.setOnClickListener {
            deselectFilters()
            dataBinding.lentFilter.selectView(true)
            //todo open bottom sheet with more filter options
        }
    }

    private fun deselectFilters(){
        dataBinding.allFilter.selectView(false)
        dataBinding.lentFilter.selectView(false)
        dataBinding.borrowedFilter.selectView(false)
    }

    private fun initAdapter(){
        adapter = HomeCardsAdapter(
            this::onCardClicked
        )
        dataBinding.homeCardContainer.adapter = adapter.withLoadStateFooter(LoadStateAdapter())

        adapter.addLoadStateListener { loadState ->
            // show empty list
            val isListEmpty = loadState.refresh is LoadState.NotLoading && adapter.itemCount == 0
            showEmptyList(isListEmpty)
        }
    }

    private fun onCardClicked(homeCard: HomeCardModel){

    }

    private fun showEmptyList(show: Boolean) {
        dataBinding.tvEmptyList.isVisible = show
        dataBinding.homeCardContainer.isVisible = !show
    }

    private fun filter(filter: String) {
        // Make sure we cancel the previous job before creating a new one
        filterJob?.cancel()
        filterJob = lifecycleScope.launch {
            viewModel.filterHomeCards(filter).collectLatest {
                adapter.submitData(it)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun attachObservers() {
        viewModel.loading.observe(viewLifecycleOwner, loadingObserver)

        viewModel.profileData.observe(viewLifecycleOwner){
            it.profilePictureUrl?.let { url ->
                Picasso.get()
                    .load(url)
                    .centerCrop()
                    .into(dataBinding.searchView.currentUserProfilePicture)
            }
        }
    }

    override fun setLoading(loading: Boolean) {

    }

    private fun verifyToken(){
        setLoading(true)
        val token = sharedPrefs.getAccessToken()
        if(token.isNullOrEmpty()){
            findNavController().navigate(R.id.action_global_onboardingActivity)
            (activity as MainActivity).finish()
        }
    }

}