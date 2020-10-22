package com.debts.debtstracker.data.network.api

import com.debts.debtstracker.data.NetworkState
import com.debts.debtstracker.data.network.model.*
import com.debts.debtstracker.data.pagination.PagedListServerModel
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // ----- auth ----
    @POST("/api/account/register")
    suspend fun register(@Body register: RegisterModel): Response<NetworkState>

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

    @GET("api/account/profile")
    suspend fun getCurrentUserProfile(): Response<CurrentUserProfile>

    @GET("api/account/logout")
    suspend fun logout(): Response<NetworkState>

    // ----- home -------
    @GET("api/home/totals")
    suspend fun getUserTotalDebts(): Response<HomeTotalDebtsModel>

    @GET("api/home/cards")
    suspend fun getHomeCards(
        @Query("filterType") filter: String,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10
    ): Response<PagedListServerModel<HomeCardModel>>

    // ----- debts ------

    @POST("api/debts/add")
    suspend fun addDebt(@Body debtModel: AddDebtModel): Response<NetworkState>


    // -----  friends -----

    @GET("api/users/friends")
    suspend fun getFriendsList(): Response<PagedListServerModel<UserModel>>

    @GET("api/users/search")
    suspend fun getUserList(
        @Query("search") search: String,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10
    ): Response<PagedListServerModel<UserModel>>

    @GET("api/account/profile")
    suspend fun getUserProfile(
        @Query("userId") userId: String
    ): Response<UserModel>

    @POST("api/users/profile-action")
    suspend fun sendProfileAction(
        @Query("profileAction") action: ProfileActionEnum,
        @Query("userId") userId: String
    ): Response<UserModel>

    @GET("api/account/profile")
    suspend fun getLoggedUserProfile(): Response<UserModel>

    @POST("api/account/update-password")
    suspend fun updatePassword(passModel: UpdatePasswordModel): Response<NetworkState>

    @POST("api/account/update-profile")
    suspend fun updateProfile(profileModel: UpdateProfileModel): Response<UserModel>
}