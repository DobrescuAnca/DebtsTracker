package com.debts.debtstracker.ui.main.friends

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.debts.debtstracker.R
import com.debts.debtstracker.databinding.FragmentFriendsBinding
import com.debts.debtstracker.ui.base.BaseFragment

class FriendsFragment: BaseFragment() {

    private lateinit var dataBinding: FragmentFriendsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_friends,
            container,
            false)

        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        setupLayout()
//        attachObservers()
    }


}