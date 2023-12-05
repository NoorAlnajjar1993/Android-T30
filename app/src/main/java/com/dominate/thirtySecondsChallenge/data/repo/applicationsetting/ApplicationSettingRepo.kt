package com.dominate.thirtySecondsChallenge.data.repo.applicationsetting

import com.dominate.thirtySecondsChallenge.data.model.applicationsetting.SocialLinkResponseModel
import com.dominate.thirtySecondsChallenge.data.response.ResponseWrapper
import io.reactivex.Single


interface ApplicationSettingRepo {

    fun getSocialLinks(
    ): Single<ResponseWrapper<List<SocialLinkResponseModel>>>

}