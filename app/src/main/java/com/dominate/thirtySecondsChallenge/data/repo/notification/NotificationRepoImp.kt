package com.dominate.thirtySecondsChallenge.data.repo.notification

import com.dominate.thirtySecondsChallenge.data.daos.remote.notification.NotificationRemoteDao
import com.dominate.thirtySecondsChallenge.data.model.notification.UserNotificationResponseModel
import com.dominate.thirtySecondsChallenge.data.repo.base.BaseRepo
import com.dominate.thirtySecondsChallenge.data.response.ResponseWrapper
import io.reactivex.Single
import javax.inject.Inject


class NotificationRepoImp @Inject constructor(
    private val notificationRemoteDao: NotificationRemoteDao
) : BaseRepo(), NotificationRepo {

    override fun getAllUserNotification(): Single<ResponseWrapper<List<UserNotificationResponseModel>>> {
        return notificationRemoteDao.getAllUserNotification()
    }

    override fun readNotification(): Single<ResponseWrapper<List<UserNotificationResponseModel>>> {
        return notificationRemoteDao.readNotification()
    }


}