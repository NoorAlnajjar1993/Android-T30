package com.dominate.thirtySecondsChallenge.data.repo.friends

import com.dominate.thirtySecondsChallenge.data.model.friends.AcceptRequestModel
import com.dominate.thirtySecondsChallenge.data.model.friends.DeleteFriendRequestModel
import com.dominate.thirtySecondsChallenge.data.model.friends.PaginationResponseModel
import com.dominate.thirtySecondsChallenge.data.model.friends.ReferralResponseModel
import com.dominate.thirtySecondsChallenge.data.model.friends.RejectRequestModel
import com.dominate.thirtySecondsChallenge.data.model.friends.SendRequestModel
import com.dominate.thirtySecondsChallenge.data.model.friends.UnfriendRequestModel
import com.dominate.thirtySecondsChallenge.data.model.friends.UserBlockResponseModel
import com.dominate.thirtySecondsChallenge.data.model.friends.UserFriendsResponseModel
import com.dominate.thirtySecondsChallenge.data.model.player.ReportReasonResponseModel
import com.dominate.thirtySecondsChallenge.data.model.player.ReportResponseModel
import com.dominate.thirtySecondsChallenge.data.model.profile.ProfileResponseModel
import com.dominate.thirtySecondsChallenge.data.model.user.UserDetailsResponseModel
import com.dominate.thirtySecondsChallenge.data.response.ResponseWrapper
import io.reactivex.Single
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path


interface FriendsRepo {

    fun getAllUserFriends(
    ): Single<ResponseWrapper<List<UserFriendsResponseModel>>>

    fun getAllRequest(
    ): Single<ResponseWrapper<List<UserFriendsResponseModel>>>

    fun acceptRequest(
        acceptRequestModel: AcceptRequestModel
    ): Single<ResponseWrapper<List<UserFriendsResponseModel>>>

    fun rejectRequest(
        rejectRequestModel: RejectRequestModel
    ): Single<ResponseWrapper<List<UserFriendsResponseModel>>>

    fun unfriendRequest(
        unfriendRequestModel: UnfriendRequestModel
    ): Single<ResponseWrapper<List<UserFriendsResponseModel>>>

    fun deleteFriendRequest(
        deleteFriendRequestModel: DeleteFriendRequestModel
    ): Single<ResponseWrapper<List<UserFriendsResponseModel>>>

    fun userBlock(
        id: Int
    ): Single<ResponseWrapper<List<UserBlockResponseModel>>>

    fun getPagination(
        page: Int,
        query: String
    ): Single<ResponseWrapper<PaginationResponseModel>>

    fun referral(
    ): Single<ResponseWrapper<ReferralResponseModel>>
}