package com.dominate.thirtySecondsChallenge.data.daos.remote.notification

import com.dominate.thirtySecondsChallenge.data.model.notification.UserNotificationResponseModel
import com.dominate.thirtySecondsChallenge.data.model.player.ReportReasonResponseModel
import com.dominate.thirtySecondsChallenge.data.model.profile.ProfileResponseModel
import com.dominate.thirtySecondsChallenge.data.model.user.UserDetailsResponseModel
import com.dominate.thirtySecondsChallenge.data.response.ResponseWrapper
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface NotificationRemoteDao {


    @GET("api/UserNotification/GetAll")
    fun getAllUserNotification(
    ): Single<ResponseWrapper<List<UserNotificationResponseModel>>>

    @POST("api/UserNotification/Read")
    fun readNotification(
    ): Single<ResponseWrapper<List<UserNotificationResponseModel>>>

}
