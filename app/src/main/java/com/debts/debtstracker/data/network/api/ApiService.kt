package com.debts.debtstracker.data.network.api

import com.debts.debtstracker.data.network.model.AddDebtModel
import com.debts.debtstracker.data.network.model.HomeCardModel
import com.debts.debtstracker.data.network.model.LoginModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    // ----- auth ----

    @POST("/api/oauth/token")
    suspend fun login(
        @Body model: LoginModel
    ): Response<String>

    // ----- home -------
    @GET("api/home/cards")
    suspend fun getHomeCards(
        @Query("filterType") filter: String,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10
    ): Response<List<HomeCardModel>>

    // ----- debts ------
    @POST("api/debts/add")
    suspend fun addDebt(@Body debtModel: AddDebtModel): Response<Any>
}