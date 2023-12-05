package com.dominate.thirtySecondsChallenge.ui.notification.model

data class NotificationModel(

    val messageTitle: String,
    val isAddFriends: Boolean = false,
    val isAddGame: Boolean = false,
    )