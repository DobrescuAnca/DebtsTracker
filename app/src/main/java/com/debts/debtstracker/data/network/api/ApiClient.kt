package com.debts.debtstracker.data.network.api

import com.debts.debtstracker.data.network.model.AuthErrorModel
import com.debts.debtstracker.injection.authorizationInterceptor
import com.debts.debtstracker.injection.moshi
import com.squareup.moshi.Moshi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class ApiClient(
    moshi: Moshi,
    authorizationInterceptor: AuthorizationInterceptor
) {
    private val okHttpClient: OkHttpClient
    private val retrofitBuilder: Retrofit.Builder

    private val BASE_URL = "http://3.13.168.196:4000/"

    init {

        val level = HttpLoggingInterceptor.Level.BODY
//            if(BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
//            else HttpLoggingInterceptor.Level.NONE

        val loggingInterceptor = HttpLoggingInterceptor().apply { this.level = level }

        okHttpClient = OkHttpClient.Builder()
            .authenticator(TokenAuthenticator())
            .addInterceptor(authorizationInterceptor)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(ErrorInterceptor())
            .build()

        retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
    }

    fun <T> createApi(
        apiClass: Class<T>,
        interceptors: List<Interceptor> = emptyList()
    ): T {
        if (interceptors.isNotEmpty()) {
            val newClient = okHttpClient.newBuilder()
            interceptors.forEach {
                newClient.addInterceptor(it)
            }
            retrofitBuilder.client(newClient.build())
        }

        return retrofitBuilder.baseUrl(BASE_URL).build().create(apiClass)
    }

    companion object{
        const val HEADER_AUTHORIZATION = "Authorization"
    }
}

class AuthorizationInterceptor(var accessToken: String?) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val builder = original.newBuilder()

        if(accessToken != "")
            builder.addHeader(ApiClient.HEADER_AUTHORIZATION, "Bearer${accessToken}")

        val request = builder.url(original.url).build()
        return chain.proceed(request)
    }
}

class ErrorInterceptor: Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val response = chain.proceed(original)

        //pt original am method si url, pe care le pot pastra
        when(response.code) {
            //TODO add response codes here
            401 -> {
//                val errorString: String = response.body?.string() ?: ""
//                val jsonAdapter = moshi.adapter(AuthErrorModel::class.java)
//                val error = jsonAdapter.fromJson(errorString)?.error

            }
        }

        return response
    }
}

class NoNetworkConnectionException : Exception()

fun updateAuthorizationInterceptor(accessToken: String) {
    authorizationInterceptor.accessToken = accessToken
}

fun parseError(response: retrofit2.Response<*>): AuthErrorModel? {
    // response.errorBody().string - must be saved in a different variable, because a second call will return error
    val errorString: String = response.errorBody()?.string() ?: ""

    return if (errorString.isEmpty()) {
        AuthErrorModel(response.code().toString())
    } else {
        val jsonAdapter = moshi.adapter(AuthErrorModel::class.java)
        val error = jsonAdapter.fromJson(errorString)
        error
    }
}