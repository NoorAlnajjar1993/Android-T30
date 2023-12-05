package com.dominate.thirtySecondsChallenge.data.repo.leader

import com.dominate.thirtySecondsChallenge.data.daos.remote.leader.LeaderRemoteDao
import com.dominate.thirtySecondsChallenge.data.model.leader.LeaderboardFiltersModel
import com.dominate.thirtySecondsChallenge.data.model.profile.ProfileResponseModel
import com.dominate.thirtySecondsChallenge.data.model.profile.UserInfoModel
import com.dominate.thirtySecondsChallenge.data.repo.base.BaseRepo
import com.dominate.thirtySecondsChallenge.data.response.ResponseWrapper
import io.reactivex.Single
import javax.inject.Inject


class LeaderRepoImp @Inject constructor(
    private val leaderRemoteDao: LeaderRemoteDao

) : BaseRepo(), LeaderRepo {
    override fun getLeaderboardFilters(): Single<ResponseWrapper<List<LeaderboardFiltersModel>>> {
        return leaderRemoteDao.getLeaderboardFilters()
    }

    override fun getLeaderboard(id: Int): Single<ResponseWrapper<List<UserInfoModel>>> {
      return leaderRemoteDao.getLeaderboard(id)
    }


}