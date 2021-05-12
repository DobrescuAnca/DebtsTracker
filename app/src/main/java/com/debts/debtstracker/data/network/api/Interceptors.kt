package com.debts.debtstracker.data.network.api

import com.debts.debtstracker.injection.authorizationInterceptor
import okhttp3.Interceptor
import okhttp3.Response

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

fun updateAuthorizationInterceptor(accessToken: String) {
    authorizationInterceptor.accessToken = accessToken
}