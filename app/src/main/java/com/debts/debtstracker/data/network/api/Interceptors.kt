package com.debts.debtstracker.data.network.api

import okhttp3.Interceptor
import okhttp3.Response

class AuthorizationInterceptor: Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val builder = original.newBuilder()

        if(accessToken != "")
            builder.addHeader(ApiClient.HEADER_AUTHORIZATION, "$accessToken")

        val request = builder.url(original.url).build()
        return chain.proceed(request)
    }

    companion object{
        private var accessToken: String? = null

        fun updateAccessToken(newAccessToken: String){
            accessToken = newAccessToken
        }
    }
}