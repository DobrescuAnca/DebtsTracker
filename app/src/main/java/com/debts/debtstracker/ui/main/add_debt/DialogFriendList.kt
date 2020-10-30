package com.debts.debtstracker.ui.main.add_debt

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.debts.debtstracker.R
import com.debts.debtstracker.data.network.model.UserModel
import com.debts.debtstracker.databinding.DialogFriendListBinding
import com.debts.debtstracker.util.SEARCH_KEY_MIN_LENGTH
import com.debts.debtstracker.util.onChange
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class DialogFriendList(
    listItem: List<UserModel>,
    private val callback: (UserModel) -> Unit
): DialogFragment() {

    private lateinit var dataBinding: DialogFriendListBinding
    private lateinit var adapter: DialogFriendListAdapter
    private var friendList: List<UserModel> = listItem.toList()
    private var previousSearchKey = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.dialog_friend_list,
            container,
            false
        )
        dataBinding.lifecycleOwner = viewLifecycleOwner

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()

        dataBinding.etSearch.onChange { searchKey ->
            if (searchKey.isNotBlank() && searchKey.trim().length >= SEARCH_KEY_MIN_LENGTH) {
                var newFilteredList: List<UserModel>

                CoroutineScope(Dispatchers.Default).launch {
                    withContext(Dispatchers.IO) {
                        newFilteredList = filterDialogList(friendList, searchKey.trim())
                    }
                    previousSearchKey = searchKey.trim()
                    adapter.submitList(newFilteredList)
                }
            } else {
                if (previousSearchKey.length > searchKey.trim().length) {
                    previousSearchKey = ""
                    adapter.submitList(friendList)
                }
            }
        }
    }

    private fun initAdapter(){
        adapter = DialogFriendListAdapter(
            this::selectFriend
        )
        dataBinding.rvContainer.adapter = adapter
        adapter.submitList(friendList)
    }

    private fun selectFriend(friend: UserModel){
        callback(friend)
        dismiss()
    }

    private fun filterDialogList(list: List<UserModel>, search: String): List<UserModel>{
        val newList = LinkedList<UserModel>()

        list.map {
            if (it.name.contains(search, ignoreCase = true))
                newList.add(it.copy())
        }

        return newList

    }

}