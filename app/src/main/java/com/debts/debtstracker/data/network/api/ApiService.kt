package com.debts.debtstracker.data.network.api

import com.debts.debtstracker.data.network.model.AddDebtModel
import com.debts.debtstracker.data.network.model.AuthModel
import com.debts.debtstracker.data.network.model.HomeCardModel
import com.debts.debtstracker.util.BASIC_AUTHORIZATION
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // ----- auth ----

    @POST("/api/oauth/token")
    @FormUrlEncoded
    suspend fun login(
        @Field("grant_type") grantType: String = "password",
        @Field("scope") scope: String = "mobile",
        @Field("username") username: String,
        @Field("password") password: String,
        @Header("Authorization") authorization:String = BASIC_AUTHORIZATION
    ): Response<AuthModel>

    @POST("/api/oauth/token")
    @FormUrlEncoded
    fun refreshToken(
        @Field("grant_type") grantType: String = "refresh_token",
        @Field("scope") scope: String = "mobile",
        @Field("refresh_token") refreshToken: String,
        @Header("Authorization") authorization:String = BASIC_AUTHORIZATION
    ): retrofit2.Call<AuthModel>


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