package com.debts.debtstracker.data.network.model

data class UserModel(
    val id: String,
    val name: String,
    val username: String,
    val profilePictureUrl: String,
    val totalToReceive: Double?,
    val totalToPay: Double?,
    val friendshipStatus: FriendshipStatusEnum
)

enum class FriendshipStatusEnum{
    FRIENDS, NOT_FRIENDS, REQUEST_SENT, REQUEST_RECEIVED
}

enum class ProfileActionEnum{
    ADD_FRIEND, REMOVE_FRIEND, CANCEL_REQUEST, ACCEPT_REQUEST, DECLINE_REQUEST
}

