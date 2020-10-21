package com.debts.debtstracker.data.pagination

import com.debts.debtstracker.data.network.model.HomeCardModel
import com.debts.debtstracker.injection.ApiServiceObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.inject
import retrofit2.Response

class HomeDataSource (
    private val filter: String,
    scope: CoroutineScope
): BaseDataSource<HomeCardModel>(scope){

    private val api: ApiServiceObject by inject()

    override suspend fun requestData(page: Int): Response<PagedListServerModel<HomeCardModel>>? {
        var response: Response<PagedListServerModel<HomeCardModel>>?

        withContext(Dispatchers.IO) {
            response = api.RETROFIT_SERVICE.getHomeCards(
                filter = filter,
                page = page,
                size = NETWORK_PAGE_SIZE
            )
        }
        return response
    }
}