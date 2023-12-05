package com.dominate.thirtySecondsChallenge.data.repo.player

import com.dominate.thirtySecondsChallenge.data.model.friends.SendRequestModel
import com.dominate.thirtySecondsChallenge.data.model.friends.UserFriendsResponseModel
import com.dominate.thirtySecondsChallenge.data.model.player.FriendshipStatusResponseModel
import com.dominate.thirtySecondsChallenge.data.model.player.ReportReasonResponseModel
import com.dominate.thirtySecondsChallenge.data.model.player.ReportResponseModel
import com.dominate.thirtySecondsChallenge.data.model.player.request.ReportPlayerAddRequestModel
import com.dominate.thirtySecondsChallenge.data.model.profile.ProfileResponseModel
import com.dominate.thirtySecondsChallenge.data.model.user.UserDetailsResponseModel
import com.dominate.thirtySecondsChallenge.data.response.ResponseWrapper
import io.reactivex.Single
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.Part
import retrofit2.http.Path


interface PlayerRepo {

    fun getPlayerProfile(
        id: Int
    ): Single<ResponseWrapper<ProfileResponseModel>>

    fun getReportReason(
    ): Single<ResponseWrapper<List<ReportReasonResponseModel>>>

    fun reportPlayerAdd(
        reportPlayerAddRequestModel: ReportPlayerAddRequestModel
    ): Single<ResponseWrapper<List<ReportResponseModel>>>

    fun sendRequest(
        sendRequestModel: SendRequestModel
    ): Single<ResponseWrapper<UserFriendsResponseModel>>

    fun friendshipStatus(
        id: Int
    ): Single<ResponseWrapper<FriendshipStatusResponseModel>>

}