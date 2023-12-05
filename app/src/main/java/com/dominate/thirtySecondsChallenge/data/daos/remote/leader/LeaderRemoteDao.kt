package com.dominate.thirtySecondsChallenge.data.daos.remote.leader

import com.dominate.thirtySecondsChallenge.data.model.gift.request.SendGiftsRequestModel
import com.dominate.thirtySecondsChallenge.data.model.leader.LeaderboardFiltersModel
import com.dominate.thirtySecondsChallenge.data.model.profile.GiftsResponseModel
import com.dominate.thirtySecondsChallenge.data.model.profile.ProfileResponseModel
import com.dominate.thirtySecondsChallenge.data.model.profile.UserInfoModel
import com.dominate.thirtySecondsChallenge.data.response.ResponseWrapper
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Path

interface LeaderRemoteDao {

    @GET("api/Leaderboard/Get/Filters")
    fun getLeaderboardFilters(
    ): Single<ResponseWrapper<List<LeaderboardFiltersModel>>>

    @GET("api/Leaderboard/Get/{id}")
    fun getLeaderboard(
        @Path(value = "id", encoded = false) id: Int
    ): Single<ResponseWrapper<List<UserInfoModel>>>

}
