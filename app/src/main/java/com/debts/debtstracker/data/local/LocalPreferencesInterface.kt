package com.debts.debtstracker.data.local

import com.debts.debtstracker.data.network.model.AuthModel

interface LocalPreferencesInterface {

    fun saveRefreshToken(token: AuthModel)

    fun getRefreshToken(): AuthModel?

    fun clearSharedPrefs()
}