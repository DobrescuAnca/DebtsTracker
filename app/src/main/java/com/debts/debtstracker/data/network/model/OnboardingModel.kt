package com.debts.debtstracker.data.network.model

data class RegisterModel(
    val email: String,
    val name: String,
    val password: String
)

data class StatusModel(
    val status: String
)

data class MessageModel(
    val messageModel: String
)

data class AuthErrorModel(
    val error: String?
)

data class AuthModel(
    val access_token: String,
    val refresh_token: String
)


