package com.debts.debtstracker.data.network.model

data class UserModel(
    val id: String,
    var name: String,
    val username: String,
    val email: String ?,
    val profilePictureUrl: String,
    val totalToReceive: Double? = 0.0,
    val totalToPay: Double? = 0.0,
    val friendshipStatus: FriendshipStatusEnum?,

    var debtSum: Float ? = 0F
)

data class UpdateProfileModel(
    val email: String,
    var name: String,
    val username: String
)

data class UpdatePasswordModel(
    val currentPassword: String,
    val newPassword: String
)

enum class FriendshipStatusEnum{
    FRIENDS, NOT_FRIENDS, REQUEST_SENT, REQUEST_RECEIVED, OWN_PROFILE
}

enum class ProfileActionEnum{
    ADD_FRIEND, REMOVE_FRIEND, CANCEL_REQUEST, ACCEPT_REQUEST, DECLINE_REQUEST
}

val EmptyUserModel = UserModel("", "", "", "", "", 0.0, 0.0, FriendshipStatusEnum.NOT_FRIENDS)


