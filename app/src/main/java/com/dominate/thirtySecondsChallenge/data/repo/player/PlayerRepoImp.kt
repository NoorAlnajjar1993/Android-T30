package com.dominate.thirtySecondsChallenge.data.repo.player

import com.dominate.thirtySecondsChallenge.data.daos.remote.player.PlayerRemoteDao
import com.dominate.thirtySecondsChallenge.data.model.friends.SendRequestModel
import com.dominate.thirtySecondsChallenge.data.model.friends.UserFriendsResponseModel
import com.dominate.thirtySecondsChallenge.data.model.player.FriendshipStatusResponseModel
import com.dominate.thirtySecondsChallenge.data.model.player.ReportReasonResponseModel
import com.dominate.thirtySecondsChallenge.data.model.player.ReportResponseModel
import com.dominate.thirtySecondsChallenge.data.model.player.request.ReportPlayerAddRequestModel
import com.dominate.thirtySecondsChallenge.data.model.profile.ProfileResponseModel
import com.dominate.thirtySecondsChallenge.data.repo.base.BaseRepo
import com.dominate.thirtySecondsChallenge.data.response.ResponseWrapper
import io.reactivex.Single
import javax.inject.Inject


class PlayerRepoImp @Inject constructor(
    private val playerRemoteDao: PlayerRemoteDao
) : BaseRepo(), PlayerRepo {

    override fun getPlayerProfile(id: Int): Single<ResponseWrapper<ProfileResponseModel>> {
        return playerRemoteDao.getPlayerProfile(id)
    }

    override fun getReportReason(): Single<ResponseWrapper<List<ReportReasonResponseModel>>> {
        return playerRemoteDao.getReportReason()
    }

    override fun reportPlayerAdd(reportPlayerAddRequestModel: ReportPlayerAddRequestModel): Single<ResponseWrapper<List<ReportResponseModel>>> {
        return playerRemoteDao.reportPlayerAdd(reportPlayerAddRequestModel )
    }

    override fun sendRequest(sendRequestModel: SendRequestModel):Single<ResponseWrapper<UserFriendsResponseModel>> {
        return playerRemoteDao.sendRequest(sendRequestModel)
    }

    override fun friendshipStatus(id: Int): Single<ResponseWrapper<FriendshipStatusResponseModel>> {
        return playerRemoteDao.friendshipStatus(id)
    }

}