package com.dominate.thirtySecondsChallenge.data.repo.notification

import com.dominate.thirtySecondsChallenge.data.model.notification.UserNotificationResponseModel
import com.dominate.thirtySecondsChallenge.data.response.ResponseWrapper
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.POST


interface NotificationRepo {

    fun getAllUserNotification(
    ): Single<ResponseWrapper<List<UserNotificationResponseModel>>>

    fun readNotification(
    ): Single<ResponseWrapper<List<UserNotificationResponseModel>>>

}