package com.debts.debtstracker.data.local

interface LocalPreferencesInterface {

    fun saveAccessToken(token: String)

    fun getAccessToken(): String?

    fun clearSharedPrefs()
}