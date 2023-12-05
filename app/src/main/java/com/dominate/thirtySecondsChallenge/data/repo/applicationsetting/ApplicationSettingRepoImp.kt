package com.dominate.thirtySecondsChallenge.data.repo.applicationsetting

import com.dominate.thirtySecondsChallenge.data.daos.remote.applicationsetting.ApplicationSettingRemoteDao
import com.dominate.thirtySecondsChallenge.data.model.applicationsetting.SocialLinkResponseModel
import com.dominate.thirtySecondsChallenge.data.repo.base.BaseRepo
import com.dominate.thirtySecondsChallenge.data.response.ResponseWrapper
import io.reactivex.Single
import javax.inject.Inject


class ApplicationSettingRepoImp @Inject constructor(
    private val applicationSettingRemoteDao: ApplicationSettingRemoteDao
) : BaseRepo(), ApplicationSettingRepo {

    override fun getSocialLinks(): Single<ResponseWrapper<List<SocialLinkResponseModel>>> {
        return applicationSettingRemoteDao.getSocialLinks()
    }


}