package com.debts.debtstracker.data.pagination

import com.debts.debtstracker.data.network.model.UserModel
import com.debts.debtstracker.injection.ApiServiceObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.inject
import retrofit2.Response


class FriendListDataSource(scope: CoroutineScope): BaseDataSource<UserModel>(scope){

    private val api: ApiServiceObject by inject()

    override suspend fun requestData(page: Int): Response<PagedListServerModel<UserModel>>? {
        var response: Response<PagedListServerModel<UserModel>>? = null

        withContext(Dispatchers.IO) {

            response = api.RETROFIT_SERVICE.getFriendsList(
                page = page,
                size = NETWORK_PAGE_SIZE
            )
        }
        return response
    }
}

class UserListDataSource(
    private val search: String,
    scope: CoroutineScope
): BaseDataSource<UserModel>(scope){

    private val api: ApiServiceObject by inject()

    override suspend fun requestData(page: Int): Response<PagedListServerModel<UserModel>>? {
        var response: Response<PagedListServerModel<UserModel>>? = null

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
