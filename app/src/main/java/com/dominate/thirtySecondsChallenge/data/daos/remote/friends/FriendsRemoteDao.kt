package com.dominate.thirtySecondsChallenge.data.daos.remote.friends

import com.dominate.thirtySecondsChallenge.data.model.applicationsetting.SocialLinkResponseModel
import com.dominate.thirtySecondsChallenge.data.model.friends.AcceptRequestModel
import com.dominate.thirtySecondsChallenge.data.model.friends.DeleteFriendRequestModel
import com.dominate.thirtySecondsChallenge.data.model.friends.PaginationResponseModel
import com.dominate.thirtySecondsChallenge.data.model.friends.ReferralResponseModel
import com.dominate.thirtySecondsChallenge.data.model.friends.RejectRequestModel
import com.dominate.thirtySecondsChallenge.data.model.friends.SendRequestModel
import com.dominate.thirtySecondsChallenge.data.model.friends.UnfriendRequestModel
import com.dominate.thirtySecondsChallenge.data.model.friends.UserBlockResponseModel
import com.dominate.thirtySecondsChallenge.data.model.friends.UserFriendsResponseModel
import com.dominate.thirtySecondsChallenge.data.model.profile.GiftsResponseModel
import com.dominate.thirtySecondsChallenge.data.model.profile.ProfileResponseModel
import com.dominate.thirtySecondsChallenge.data.response.ResponseWrapper
import io.reactivex.Single
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface FriendsRemoteDao {


    @GET("api/UserFriends/GetAll")
    fun getAllUserFriends(
    ): Single<ResponseWrapper<List<UserFriendsResponseModel>>>

    @GET("api/UserFriends/GetAllRequest")
    fun getAllRequest(
    ): Single<ResponseWrapper<List<UserFriendsResponseModel>>>

    @POST("api/UserFriends/Accept")
    fun acceptRequest(
        @Body acceptRequestModel : AcceptRequestModel
    ): Single<ResponseWrapper<List<UserFriendsResponseModel>>>

    @POST("api/UserFriends/Reject")
    fun rejectRequest(
        @Body rejectRequestModel : RejectRequestModel
    ): Single<ResponseWrapper<List<UserFriendsResponseModel>>>

    @POST("api/UserFriends/Unfriend")
    fun unfriendRequest(
        @Body unfriendRequestModel : UnfriendRequestModel
    ): Single<ResponseWrapper<List<UserFriendsResponseModel>>>

    @POST("api/UserFriends/DeleteFriendRequest")
    fun deleteFriendRequest(
        @Body deleteFriendRequestModel : DeleteFriendRequestModel
    ): Single<ResponseWrapper<List<UserFriendsResponseModel>>>

    @POST("api/UserBlockList/Add/{id}")
    fun userBlock(
        @Path(value = "id", encoded = false) id: Int
    ): Single<ResponseWrapper<List<UserBlockResponseModel>>>

    @GET("api/UserPlayer/Pagination/{page}/{query}")
    fun getPagination(
        @Path(value= "page", encoded=false) page: Int,
        @Path(value= "query", encoded=false) query: String
    ): Single<ResponseWrapper<PaginationResponseModel>>

    @GET("api/UserPlayer/Referral")
    fun referral(
    ): Single<ResponseWrapper<ReferralResponseModel>>
}
