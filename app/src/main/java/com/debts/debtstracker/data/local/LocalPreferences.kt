package com.debts.debtstracker.data.local

import com.debts.debtstracker.data.network.api.updateAuthorizationInterceptor
import com.debts.debtstracker.data.network.model.AuthModel
import com.debts.debtstracker.injection.moshi

class LocalPreferences(
    private val preferencesSource: PreferencesSource
) : LocalPreferencesInterface {

    private val tokenJsonAdapter =
        moshi.adapter(AuthModel::class.java)

    override fun saveRefreshToken(token: AuthModel) {
        preferencesSource.customPrefs()[REFRESH_TOKEN] = tokenJsonAdapter.toJson(token)
        updateAuthorizationInterceptor(token.access_token)
    }

    override fun getRefreshToken(): AuthModel? {
        val localTokenJson: String = preferencesSource.customPrefs()[REFRESH_TOKEN] ?: ""
        if(localTokenJson.isEmpty())
            return null
        return tokenJsonAdapter.fromJson(localTokenJson)
    }

    override fun clearSharedPrefs() {
        preferencesSource.customPrefs().edit().clear().apply()
        updateAuthorizationInterceptor("")
    }

    companion object {
        const val PREFERENCES_FILE_NAME = "preferences"
        const val REFRESH_TOKEN = "token"
    }
}