package com.dominate.thirtySecondsChallenge.data.daos.remote.player

import com.dominate.thirtySecondsChallenge.data.model.friends.SendRequestModel
import com.dominate.thirtySecondsChallenge.data.model.friends.UserFriendsResponseModel
import com.dominate.thirtySecondsChallenge.data.model.player.FriendshipStatusResponseModel
import com.dominate.thirtySecondsChallenge.data.model.player.ReportReasonResponseModel
import com.dominate.thirtySecondsChallenge.data.model.player.ReportResponseModel
import com.dominate.thirtySecondsChallenge.data.model.profile.ProfileResponseModel
import com.dominate.thirtySecondsChallenge.data.model.player.request.ReportPlayerAddRequestModel
import com.dominate.thirtySecondsChallenge.data.response.ResponseWrapper
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Path

interface PlayerRemoteDao {

    @GET("api/UserPlayer/GetPlayerProfile/{id}")
    fun getPlayerProfile(
        @Path(value= "id", encoded=false) id: Int
    ): Single<ResponseWrapper<ProfileResponseModel>>

    @GET("api/ReportReason/GetList")
    fun getReportReason(
    ): Single<ResponseWrapper<List<ReportReasonResponseModel>>>

    @POST("api/ReportPlayer/Add")
    fun reportPlayerAdd(
        @Body reportPlayerAddRequestModel : ReportPlayerAddRequestModel
    ): Single<ResponseWrapper<List<ReportResponseModel>>>

    @POST("api/UserFriends/SendRequest")
    fun sendRequest(
        @Body sendRequestModel : SendRequestModel
    ): Single<ResponseWrapper<UserFriendsResponseModel>>

    @GET("api/UserFriends/FriendshipStatus/{id}")
    fun friendshipStatus(
        @Path(value= "id", encoded=false) id: Int
    ): Single<ResponseWrapper<FriendshipStatusResponseModel>>

}
