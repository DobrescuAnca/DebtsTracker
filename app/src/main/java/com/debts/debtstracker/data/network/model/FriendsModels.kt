package com.debts.debtstracker.data.network.model

data class UserModel(
    val id: String,
    val name: String,
    val username: String,
    val profilePictureUrl: String,
    val totalToReceive: Double?,
    val totalToPay: Double?,
    val friendshipStatus: FriendshipStatus
)

enum class FriendshipStatus{
    FRIENDS, NOT_FRIENDS, REQUEST_SENT, REQUEST_RECEIVED
}

