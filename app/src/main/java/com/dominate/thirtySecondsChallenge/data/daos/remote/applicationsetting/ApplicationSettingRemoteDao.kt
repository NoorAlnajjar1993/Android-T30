package com.dominate.thirtySecondsChallenge.data.daos.remote.applicationsetting

import com.dominate.thirtySecondsChallenge.data.model.applicationsetting.SocialLinkResponseModel
import com.dominate.thirtySecondsChallenge.data.response.ResponseWrapper
import io.reactivex.Single
import retrofit2.http.GET

interface ApplicationSettingRemoteDao {

    @GET("api/ApplicationSetting/get/social_links")
    fun getSocialLinks(
    ): Single<ResponseWrapper<List<SocialLinkResponseModel>>>

}
