package com.debts.debtstracker.data.network.api

import com.debts.debtstracker.data.network.model.AuthModel
import com.debts.debtstracker.data.network.model.RegisterModel
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



    // -----   -----

}