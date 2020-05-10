package com.debts.debtstracker.data.network.api

import com.squareup.moshi.Moshi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class ApiClient(moshi: Moshi) {
    private val okHttpClient: OkHttpClient
    private val retrofitBuilder: Retrofit.Builder

    private val BASE_URL = "http://3.13.168.196:4000/"

    init {

        val level = HttpLoggingInterceptor.Level.BODY
//            if(BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
//            else HttpLoggingInterceptor.Level.NONE

        val loggingInterceptor = HttpLoggingInterceptor().apply { this.level = level }

        okHttpClient = OkHttpClient.Builder()
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
}

class ErrorInterceptor: Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val response = chain.proceed(original)

        when(response.code) {
            //TODO add response codes here
            //401 "invalid_token" pe errror din modelul cu error si error_description
        }

        return response
    }
}

class NoNetworkConnectionException : Exception()