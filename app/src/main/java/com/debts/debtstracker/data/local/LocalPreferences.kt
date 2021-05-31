package com.debts.debtstracker.data.local

import com.debts.debtstracker.data.network.api.AuthorizationInterceptor

class LocalPreferences(
    private val preferencesSource: PreferencesSource
) : LocalPreferencesInterface {

    override fun saveAccessToken(token: String) {
        preferencesSource.customPrefs()[ACCESS_TOKEN] = token
        AuthorizationInterceptor.updateAccessToken(token)
    }

    override fun getAccessToken(): String? {
        val localToken: String = preferencesSource.customPrefs()[ACCESS_TOKEN] ?: ""
        if(localToken.isEmpty()) {
            AuthorizationInterceptor.updateAccessToken("")
            return null
        }
        AuthorizationInterceptor.updateAccessToken(localToken)
        return localToken
    }

    override fun clearSharedPrefs() {
        preferencesSource.customPrefs().edit().clear().apply()
        AuthorizationInterceptor.updateAccessToken("")
    }

    companion object {
        const val PREFERENCES_FILE_NAME = "preferences"
        const val ACCESS_TOKEN = "token"
    }
}