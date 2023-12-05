package com.dominate.thirtySecondsChallenge.data.repo.friends

import com.dominate.thirtySecondsChallenge.data.daos.remote.friends.FriendsRemoteDao
import com.dominate.thirtySecondsChallenge.data.model.friends.AcceptRequestModel
import com.dominate.thirtySecondsChallenge.data.model.friends.DeleteFriendRequestModel
import com.dominate.thirtySecondsChallenge.data.model.friends.PaginationResponseModel
import com.dominate.thirtySecondsChallenge.data.model.friends.ReferralResponseModel
import com.dominate.thirtySecondsChallenge.data.model.friends.RejectRequestModel
import com.dominate.thirtySecondsChallenge.data.model.friends.SendRequestModel
import com.dominate.thirtySecondsChallenge.data.model.friends.UnfriendRequestModel
import com.dominate.thirtySecondsChallenge.data.model.friends.UserBlockResponseModel
import com.dominate.thirtySecondsChallenge.data.model.friends.UserFriendsResponseModel
import com.dominate.thirtySecondsChallenge.data.repo.base.BaseRepo
import com.dominate.thirtySecondsChallenge.data.response.ResponseWrapper
import io.reactivex.Single
import javax.inject.Inject


class FriendsRepoImp @Inject constructor(
    private val friendsRemoteDao: FriendsRemoteDao
) : BaseRepo(), FriendsRepo {

    override fun getAllUserFriends(): Single<ResponseWrapper<List<UserFriendsResponseModel>>> {
        return friendsRemoteDao.getAllUserFriends()
    }

    override fun getAllRequest(): Single<ResponseWrapper<List<UserFriendsResponseModel>>> {
        return friendsRemoteDao.getAllRequest()
    }

    override fun acceptRequest(acceptRequestModel: AcceptRequestModel): Single<ResponseWrapper<List<UserFriendsResponseModel>>> {
        return friendsRemoteDao.acceptRequest(acceptRequestModel)
    }

    override fun rejectRequest(rejectRequestModel: RejectRequestModel): Single<ResponseWrapper<List<UserFriendsResponseModel>>> {
        return friendsRemoteDao.rejectRequest(rejectRequestModel)
    }

    override fun unfriendRequest(unfriendRequestModel: UnfriendRequestModel): Single<ResponseWrapper<List<UserFriendsResponseModel>>> {
        return friendsRemoteDao.unfriendRequest(unfriendRequestModel)
    }

    override fun deleteFriendRequest(deleteFriendRequestModel: DeleteFriendRequestModel): Single<ResponseWrapper<List<UserFriendsResponseModel>>> {
        return friendsRemoteDao.deleteFriendRequest(deleteFriendRequestModel)
    }

    override fun userBlock(id: Int): Single<ResponseWrapper<List<UserBlockResponseModel>>> {
        return friendsRemoteDao.userBlock(id)
    }

    override fun getPagination(
        page: Int,
        query: String
    ): Single<ResponseWrapper<PaginationResponseModel>> {
        return friendsRemoteDao.getPagination(page,query)
    }

    override fun referral(): Single<ResponseWrapper<ReferralResponseModel>> {
        return friendsRemoteDao.referral()
    }


}