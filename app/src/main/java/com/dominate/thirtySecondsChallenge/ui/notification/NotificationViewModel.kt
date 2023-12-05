package com.dominate.thirtySecondsChallenge.ui.notification

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.dominate.thirtySecondsChallenge.base.viewmodel.BaseViewModel
import com.dominate.thirtySecondsChallenge.data.model.notification.UserNotificationResponseModel
import com.dominate.thirtySecondsChallenge.data.repo.notification.NotificationRepo
import com.dominate.thirtySecondsChallenge.data.response.Result
import com.dominate.thirtySecondsChallenge.utils.plus
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class NotificationViewModel @ViewModelInject constructor(
    private val notificationRepo: NotificationRepo,
) : BaseViewModel() {

    val isEmptyNotification : MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(false) }

    // get User Notification
    val notificationResult: MutableLiveData<Result<List<UserNotificationResponseModel>>> =
        MutableLiveData()

    fun getAllUserNotification(): MutableLiveData<Result<List<UserNotificationResponseModel>>> {
        notificationResult.value = Result.Loading
        compositeDisposable + notificationRepo.getAllUserNotification()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.succeeded) {

                    notificationResult.value = Result.Success(it.data, it.message)
                } else {
                    notificationResult.value =
                        Result.CustomError(
                            errorCode = it.error?.code,
                            message = it.error?.message
                        )
                }
            }, {
                notificationResult.value = Result.Error(it)
            })
        return notificationResult
    }

    // read Notification
    val readResult: MutableLiveData<Result<List<UserNotificationResponseModel>>> =
        MutableLiveData()

    fun readNotification(): MutableLiveData<Result<List<UserNotificationResponseModel>>> {
        readResult.value = Result.Loading
        compositeDisposable + notificationRepo.readNotification()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.succeeded) {

                    readResult.value = Result.Success(it.data, it.message)
                } else {
                    readResult.value =
                        Result.CustomError(
                            errorCode = it.error?.code,
                            message = it.error?.message
                        )
                }
            }, {
                readResult.value = Result.Error(it)
            })
        return readResult
    }

}