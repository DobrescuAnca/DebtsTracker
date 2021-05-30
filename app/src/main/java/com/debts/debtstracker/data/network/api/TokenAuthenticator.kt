package com.debts.debtstracker.data.network.api

import com.debts.debtstracker.data.local.LocalPreferencesInterface
import com.debts.debtstracker.data.network.model.AuthErrorModel
import com.debts.debtstracker.data.network.model.AuthModel
import com.debts.debtstracker.injection.ApiServiceObject
import com.debts.debtstracker.injection.moshi
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import org.koin.core.KoinComponent
import org.koin.core.inject

class TokenAuthenticator: Authenticator, KoinComponent {

    private val sharedPrefs: LocalPreferencesInterface by inject()
    private val apiService: ApiServiceObject by inject()

    override fun authenticate(route: Route?, response: Response): Request {
        val errorString: String = response.body?.string() ?: ""

        if (response.code == 401) {
            val jsonAdapter = moshi.adapter(AuthErrorModel::class.java)
            val error = jsonAdapter.fromJson(errorString)?.error

            if(error == "invalid_token") {
                val updatedToken = getNewToken()

                return response.request.newBuilder()
                    .header(ApiClient.HEADER_AUTHORIZATION, "Bearer$updatedToken")
                    .build()
            }
        }
        return response.request
    }

    private fun getNewToken(): String{
        val authToken: AuthModel?
        try {
            authToken = apiService.RETROFIT_SERVICE.refreshToken(refreshToken = sharedPrefs.getRefreshToken()?.refresh_token ?: "").execute().body()

        } catch (e: Exception){
//            Timber.e("Token Authenticator ${e.message.toString()}")
            throw  TokenAuthenticatorException(ExceptionTypes.NO_NETWORK.toString())
        }

        sharedPrefs.saveRefreshToken(authToken)

        if(authToken == null)
            throw  TokenAuthenticatorException(ExceptionTypes.INVALID_REFRESH_TOKEN.toString())

        return authToken.access_token
    }
}