package com.debts.debtstracker.data.network.model

data class AuthErrorModel(
    val error: String?
)

data class LoginModel(
    val username: String,
    val password: String
)

data class ProfileModel(
    val name: String,
    val profilePictureUrl: String
)

data class SingleValueModel(
    val value: String
)


