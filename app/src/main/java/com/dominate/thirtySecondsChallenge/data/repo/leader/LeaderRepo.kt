package com.dominate.thirtySecondsChallenge.data.repo.leader

import com.dominate.thirtySecondsChallenge.data.model.gift.request.SendGiftsRequestModel
import com.dominate.thirtySecondsChallenge.data.model.home.ActiveGameTypesResponseModel
import com.dominate.thirtySecondsChallenge.data.model.home.BannersResponseModel
import com.dominate.thirtySecondsChallenge.data.model.leader.LeaderboardFiltersModel
import com.dominate.thirtySecondsChallenge.data.model.profile.GiftsResponseModel
import com.dominate.thirtySecondsChallenge.data.model.profile.ProfileResponseModel
import com.dominate.thirtySecondsChallenge.data.model.profile.UserInfoModel
import com.dominate.thirtySecondsChallenge.data.response.ResponseWrapper
import io.reactivex.Single
import okhttp3.RequestBody
import retrofit2.http.Part
import retrofit2.http.Path


interface LeaderRepo {

    fun getLeaderboardFilters(
    ): Single<ResponseWrapper<List<LeaderboardFiltersModel>>>

    fun getLeaderboard(
        id: Int
    ): Single<ResponseWrapper<List<UserInfoModel>>>


}