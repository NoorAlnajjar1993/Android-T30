package com.dominate.thirtySecondsChallenge.data.daos.remote.profile

import com.dominate.thirtySecondsChallenge.data.model.profile.ActiveInterestsResponseModel
import com.dominate.thirtySecondsChallenge.data.model.profile.ActiveLanguagesResponseModel
import com.dominate.thirtySecondsChallenge.data.model.profile.BackgroundImagesProfileResponseModel
import com.dominate.thirtySecondsChallenge.data.model.profile.BadgesResponseModel
import com.dominate.thirtySecondsChallenge.data.model.profile.FaqCategoryResponseModel
import com.dominate.thirtySecondsChallenge.data.model.profile.GetAllFaqsResponseModel
import com.dominate.thirtySecondsChallenge.data.model.profile.ProfileResponseModel
import com.dominate.thirtySecondsChallenge.data.model.profile.UserInterestResponseModel
import com.dominate.thirtySecondsChallenge.data.model.profile.request.EditProfileRequestModel
import com.dominate.thirtySecondsChallenge.data.model.profile.request.EditProfileUserInfoRequestModel
import com.dominate.thirtySecondsChallenge.data.model.profile.request.EditUserInteresteRequest
import com.dominate.thirtySecondsChallenge.data.model.user.UserDetailsResponseModel
import com.dominate.thirtySecondsChallenge.data.model.user.request.RegisterRequestModel
import com.dominate.thirtySecondsChallenge.data.response.ResponseWrapper
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ProfileRemoteDao {

    @GET("api/UserPlayer/GetProfile")
    fun getProfile(
    ): Single<ResponseWrapper<ProfileResponseModel>>

    @GET("api/BackgroundImages/GetAll")
    fun getAllBackgroundImages(
    ): Single<ResponseWrapper<List<BackgroundImagesProfileResponseModel>>>

    @GET("api/Category/GetInterests")
    fun getActiveInterests(
    ): Single<ResponseWrapper<List<ActiveInterestsResponseModel>>>

    @GET("api/Faq/Category")
    fun getFaqCategory(
    ): Single<ResponseWrapper<List<FaqCategoryResponseModel>>>

    @GET("api/Faq/GetAllFaqs/{id}")
    fun getAllFaqs(
        @Path(value = "id", encoded = false) id: Int
    ): Single<ResponseWrapper<List<GetAllFaqsResponseModel>>>

    @GET("api/Lookups/GetActiveLanguages")
    fun getActiveLanguages(
    ): Single<ResponseWrapper<List<ActiveLanguagesResponseModel>>>

    @POST("api/UserPlayer/EditProfile/Background/{id}")
    fun editProfileBackground(
        @Path(value = "id", encoded = false) id: Int
    ): Single<ResponseWrapper<ProfileResponseModel>>

    @POST("api/UserPlayer/EditProfile/UserInfo")
    fun editProfileUserInfo(
        @Body editProfileUserInfoRequestModel: EditProfileUserInfoRequestModel
    ): Single<ResponseWrapper<ProfileResponseModel>>

    @POST("api/UserPlayer/EditProfile")
    fun editProfile(
        @Body editProfileRequestModel: EditProfileRequestModel
    ): Single<ResponseWrapper<ProfileResponseModel>>

    @Multipart
    @POST("api/UserPlayer/EditProfile/Picture")
    fun editProfilePicture(
        @Part profileImage: MultipartBody.Part?,
    ): Single<ResponseWrapper<ProfileResponseModel>>

    @POST("api/UserInterest/Edit")
    fun editUserIntereste(
        @Body   editUserInteresteRequest : EditUserInteresteRequest
    ): Single<ResponseWrapper<List<ActiveInterestsResponseModel>>>


}
