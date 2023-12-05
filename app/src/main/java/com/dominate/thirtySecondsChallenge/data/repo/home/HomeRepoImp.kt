package com.dominate.thirtySecondsChallenge.data.repo.home

import com.dominate.thirtySecondsChallenge.data.daos.remote.home.HomeRemoteDao
import com.dominate.thirtySecondsChallenge.data.model.home.ActiveGameTypesResponseModel
import com.dominate.thirtySecondsChallenge.data.model.home.BannersResponseModel
import com.dominate.thirtySecondsChallenge.data.model.home.HomeResponseModel
import com.dominate.thirtySecondsChallenge.data.model.home.request.AddFirebaseTokenRequestModel
import com.dominate.thirtySecondsChallenge.data.repo.base.BaseRepo
import com.dominate.thirtySecondsChallenge.data.response.ResponseWrapper
import io.reactivex.Single
import javax.inject.Inject


class HomeRepoImp @Inject constructor(
    private val homeRemoteDao: HomeRemoteDao
) : BaseRepo(), HomeRepo {


    override fun getBanners(): Single<ResponseWrapper<List<BannersResponseModel>>> {
        return homeRemoteDao.getBanners()
    }

    override fun getActiveGameTypes(): Single<ResponseWrapper<List<ActiveGameTypesResponseModel>>> {
        return homeRemoteDao.getActiveGameTypes()
    }

    override fun getHome(): Single<ResponseWrapper<HomeResponseModel>> {
        return homeRemoteDao.getHome()
    }

    override fun addFirebaseToken(addFirebaseToken: AddFirebaseTokenRequestModel): Single<ResponseWrapper<Boolean>> {
        return homeRemoteDao.addFirebaseToken(addFirebaseToken)
    }

}