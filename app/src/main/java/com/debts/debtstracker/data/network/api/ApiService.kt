package com.debts.debtstracker.data.network.api

import com.debts.debtstracker.data.network.model.AuthModel
import com.debts.debtstracker.data.network.model.RegisterModel
import com.debts.debtstracker.data.network.model.UserModel
import com.debts.debtstracker.data.pagination.PagedListServerModel
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // ----- auth ----
    @POST("/api/account/register")
    suspend fun register(@Body register: RegisterModel): Response<Any>

    @POST("/api/oauth/token")
    @FormUrlEncoded
    suspend fun login(
        @Field("grant_type") grantType: String = "password",
        @Field("scope") scope: String = "mobile",
        @Field("username") username: String,
        @Field("password") password: String,
        @Header("Authorization") authorization:String = "Basic ZGVidHN0cmFja2VyOg=="
    ): Response<AuthModel>

    @POST("/api/oauth/token")
    @FormUrlEncoded
    fun refreshToken(
        @Field("grant_type") grantType: String = "refresh_token",
        @Field("scope") scope: String = "mobile",
        @Field("refresh_token") refreshToken: String,
        @Header("Authorization") authorization:String = "Basic ZGVidHN0cmFja2VyOg=="
    ): retrofit2.Call<AuthModel>

    // -----  friends -----

    @GET("")
    suspend fun getFriendsList(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10
    ): Response<PagedListServerModel<UserModel>>

    @GET("api/users/search")
    suspend fun getUserList(
        @Query("search") search: String,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10
    ): Response<PagedListServerModel<UserModel>>
}