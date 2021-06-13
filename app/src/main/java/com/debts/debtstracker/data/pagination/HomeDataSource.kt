package com.debts.debtstracker.data.pagination

import com.debts.debtstracker.data.network.model.HomeCardModel
import com.debts.debtstracker.data.network.model.ListModel
import com.debts.debtstracker.injection.ApiServiceObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.KoinComponent
import org.koin.core.inject
import retrofit2.Response

class HomeDataSource (
    private val filter: String
): BaseDataSource<HomeCardModel>(), KoinComponent {

    private val api: ApiServiceObject by inject()

    override suspend fun requestData(page: Int): Response<ListModel<HomeCardModel>>? {
        var response: Response<ListModel<HomeCardModel>>?

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