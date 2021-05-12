package com.debts.debtstracker.data.pagination

import com.debts.debtstracker.data.network.model.DebtWithUserStatus
import com.debts.debtstracker.data.network.model.FriendDebtModel
import com.debts.debtstracker.data.network.model.UserModel
import com.debts.debtstracker.injection.ApiServiceObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.inject
import retrofit2.Response

class UserListDataSource(
    private val search: String,
    scope: CoroutineScope
): BaseDataSource<UserModel>(scope){

    private val api: ApiServiceObject by inject()

    override suspend fun requestData(page: Int): Response<PagedListServerModel<UserModel>>? {
        var response: Response<PagedListServerModel<UserModel>>?

        withContext(Dispatchers.IO) {
            response = api.RETROFIT_SERVICE.getUserList(
                search = search,
                page = page,
                size = NETWORK_PAGE_SIZE
            )
        }
        return response
    }
}

class DebtsWithUserDataSource(
    private val filter: DebtWithUserStatus,
    private val userId: String,
    scope: CoroutineScope
): BaseDataSource<FriendDebtModel>(scope){

    private val api: ApiServiceObject by inject()

    override suspend fun requestData(page: Int): Response<PagedListServerModel<FriendDebtModel>>? {
        var response: Response<PagedListServerModel<FriendDebtModel>>? = null

        withContext(Dispatchers.IO) {
            try{
            response = api.RETROFIT_SERVICE.getDebtsWithUser(
                filter = filter,
                page = page,
                size = NETWORK_PAGE_SIZE,
                userId = userId
            )}catch (e: Exception){
                val x = 1
            }
        }
        return response
    }
}
