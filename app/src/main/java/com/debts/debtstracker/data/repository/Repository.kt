package com.debts.debtstracker.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.debts.debtstracker.data.ResponseStatus
import com.debts.debtstracker.data.network.model.*
import com.debts.debtstracker.data.pagination.BaseDataSource.Companion.NETWORK_PAGE_SIZE
import com.debts.debtstracker.data.pagination.HomeDataSource
import com.debts.debtstracker.injection.ApiServiceObject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import retrofit2.Response

class Repository(
    private val apiService: ApiServiceObject
): RepositoryInterface {

    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    private fun <T>getResponseCall(response: Response<T>): ResponseStatus<T>{
        if(response.isSuccessful) {
            response.body()?.let {
                return ResponseStatus.Success(it)
            }
            return ResponseStatus.None
        }
        else {
            return ResponseStatus.Error(
                code = response.code(),
                errorObject = response.message()
            )
        }
    }

    override suspend fun login(username: String, password: String): ResponseStatus<SingleValueModel>{
        var response: Response<SingleValueModel>

        withContext(ioDispatcher){
            response = apiService.RETROFIT_SERVICE.login( LoginModel(
                    username = username,
                    password = password
                )
            )
        }
        return getResponseCall(response)
    }

    override suspend fun addDebt(debtModel: AddDebtModel): ResponseStatus<Any> {
        var response: Response<Any>

        withContext(ioDispatcher){
            response = apiService.RETROFIT_SERVICE.addDebt(debtModel)
        }
        return getResponseCall(response)
    }

    override suspend fun getUserProfile(): ResponseStatus<ProfileModel> {
        var response: Response<ProfileModel>

        withContext(ioDispatcher){
            response = apiService.RETROFIT_SERVICE.getUserProfile()
        }
        return getResponseCall(response)
    }

    override suspend fun getTotals(): ResponseStatus<TotalsModel> {
        var response: Response<TotalsModel>

        withContext(ioDispatcher){
            response = apiService.RETROFIT_SERVICE.getTotals()
        }
        return getResponseCall(response)
    }

    override suspend fun equate(): ResponseStatus<Any> {
        var response: Response<Any>

        withContext(ioDispatcher){
            response = apiService.RETROFIT_SERVICE.equate()
        }
        return getResponseCall(response)
    }




    override fun getHomeCardsStream(filter: String): Flow<PagingData<HomeCardModel>> {
        return Pager(
            config = PagingConfig(NETWORK_PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { HomeDataSource(filter) }
        ).flow
    }
}