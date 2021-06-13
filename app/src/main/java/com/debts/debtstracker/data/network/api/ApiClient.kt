package com.debts.debtstracker.data.network.api

import com.debts.debtstracker.data.network.model.AuthErrorModel
import com.debts.debtstracker.injection.moshi
import com.squareup.moshi.Moshi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.IOException

class ApiClient(
    moshi: Moshi
) {
    private val okHttpClient: OkHttpClient
    private val retrofitBuilder: Retrofit.Builder

    private val BASE_URL = "http://3.13.168.196:4000/api/"

    init {

        val level = HttpLoggingInterceptor.Level.BODY
//            if(BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
//            else HttpLoggingInterceptor.Level.NONE

        val loggingInterceptor = HttpLoggingInterceptor().apply { this.level = level }

        okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthorizationInterceptor())
            .addInterceptor(loggingInterceptor)
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
        const val HEADER_AUTHORIZATION = "authorization"
    }
}

class TokenAuthenticatorException(refreshMessage: String) : IOException(refreshMessage)

enum class ExceptionTypes {
    INVALID_REFRESH_TOKEN,
    NO_NETWORK
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