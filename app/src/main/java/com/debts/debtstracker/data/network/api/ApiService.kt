package com.debts.debtstracker.data.network.api

import com.debts.debtstracker.data.network.model.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    // ----- user ----
    @POST("user/authenticate")
    suspend fun login(@Body model: LoginModel): Response<SingleValueModel>

    @POST("user/logout")
    suspend fun logout(): Response<Any>

    @GET("user/profile")
    suspend fun getUserProfile(): Response<ProfileModel>

    // ----- home -------
    @GET("home/debts")
    suspend fun getHomeCards(
        @Query("filterType") filter: String,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10
    ): Response<ListModel<HomeCardModel>>

    @GET("home/totals")
    suspend fun getTotals(): Response<TotalsModel>

    // ----- debts ------
    @POST("debts/add")
    suspend fun addDebt(@Body debtModel: AddDebtModel): Response<Any>

    @POST("debts/equate")
    suspend fun equate(): Response<Any>
}