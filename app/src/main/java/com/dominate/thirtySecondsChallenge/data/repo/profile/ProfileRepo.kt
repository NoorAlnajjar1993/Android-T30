package com.dominate.thirtySecondsChallenge.data.repo.profile

import com.dominate.thirtySecondsChallenge.data.model.profile.ActiveInterestsResponseModel
import com.dominate.thirtySecondsChallenge.data.model.profile.ActiveLanguagesResponseModel
import com.dominate.thirtySecondsChallenge.data.model.profile.BackgroundImagesProfileResponseModel
import com.dominate.thirtySecondsChallenge.data.model.profile.FaqCategoryResponseModel
import com.dominate.thirtySecondsChallenge.data.model.profile.GetAllFaqsResponseModel
import com.dominate.thirtySecondsChallenge.data.model.profile.ProfileResponseModel
import com.dominate.thirtySecondsChallenge.data.model.profile.request.EditProfileRequestModel
import com.dominate.thirtySecondsChallenge.data.model.profile.request.EditProfileUserInfoRequestModel
import com.dominate.thirtySecondsChallenge.data.model.profile.request.EditUserInteresteRequest
import com.dominate.thirtySecondsChallenge.data.model.user.UserDetailsResponseModel
import com.dominate.thirtySecondsChallenge.data.response.ResponseWrapper
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path


interface ProfileRepo {

    fun getProfile(
    ): Single<ResponseWrapper<ProfileResponseModel>>

    fun getAllBackgroundImages(
    ): Single<ResponseWrapper<List<BackgroundImagesProfileResponseModel>>>

    fun getActiveInterests(
    ): Single<ResponseWrapper<List<ActiveInterestsResponseModel>>>

    fun getFaqCategory(
    ): Single<ResponseWrapper<List<FaqCategoryResponseModel>>>

    fun getAllFaqs(
        id: Int
    ): Single<ResponseWrapper<List<GetAllFaqsResponseModel>>>

    fun getActiveLanguages(
    ): Single<ResponseWrapper<List<ActiveLanguagesResponseModel>>>

    fun editProfileBackground(
        id: Int
    ): Single<ResponseWrapper<ProfileResponseModel>>

    fun editProfileUserInfo(
        editProfileUserInfoRequestModel: EditProfileUserInfoRequestModel
    ): Single<ResponseWrapper<ProfileResponseModel>>

    fun editProfile(
        @Body editProfileRequestModel: EditProfileRequestModel
    ): Single<ResponseWrapper<ProfileResponseModel>>

    fun editProfilePicture(
       profileImage: MultipartBody.Part?,
    ): Single<ResponseWrapper<ProfileResponseModel>>

    fun editUserIntereste(
        editUserInteresteRequest : EditUserInteresteRequest
    ): Single<ResponseWrapper<List<ActiveInterestsResponseModel>>>

}