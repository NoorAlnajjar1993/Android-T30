package com.dominate.thirtySecondsChallenge.data.repo.profile

import com.dominate.thirtySecondsChallenge.data.daos.remote.profile.ProfileRemoteDao
import com.dominate.thirtySecondsChallenge.data.model.profile.ActiveInterestsResponseModel
import com.dominate.thirtySecondsChallenge.data.model.profile.ActiveLanguagesResponseModel
import com.dominate.thirtySecondsChallenge.data.model.profile.BackgroundImagesProfileResponseModel
import com.dominate.thirtySecondsChallenge.data.model.profile.FaqCategoryResponseModel
import com.dominate.thirtySecondsChallenge.data.model.profile.GetAllFaqsResponseModel
import com.dominate.thirtySecondsChallenge.data.model.profile.ProfileResponseModel
import com.dominate.thirtySecondsChallenge.data.model.profile.request.EditProfileRequestModel
import com.dominate.thirtySecondsChallenge.data.model.profile.request.EditProfileUserInfoRequestModel
import com.dominate.thirtySecondsChallenge.data.model.profile.request.EditUserInteresteRequest
import com.dominate.thirtySecondsChallenge.data.repo.base.BaseRepo
import com.dominate.thirtySecondsChallenge.data.response.ResponseWrapper
import io.reactivex.Single
import okhttp3.MultipartBody
import javax.inject.Inject


class ProfileRepoImp @Inject constructor(
    private val profileRemoteDao: ProfileRemoteDao
) : BaseRepo(), ProfileRepo {

    override fun getProfile(): Single<ResponseWrapper<ProfileResponseModel>> {
        return profileRemoteDao.getProfile()
    }

    override fun getAllBackgroundImages(): Single<ResponseWrapper<List<BackgroundImagesProfileResponseModel>>> {
        return profileRemoteDao.getAllBackgroundImages()
    }

    override fun getActiveInterests(): Single<ResponseWrapper<List<ActiveInterestsResponseModel>>> {
        return profileRemoteDao.getActiveInterests()
    }

    override fun getFaqCategory(): Single<ResponseWrapper<List<FaqCategoryResponseModel>>> {
        return profileRemoteDao.getFaqCategory()
    }

    override fun getAllFaqs(id: Int): Single<ResponseWrapper<List<GetAllFaqsResponseModel>>> {
        return profileRemoteDao.getAllFaqs(id)
    }

    override fun getActiveLanguages(): Single<ResponseWrapper<List<ActiveLanguagesResponseModel>>> {
        return profileRemoteDao.getActiveLanguages()
    }

    override fun editProfileBackground(id: Int): Single<ResponseWrapper<ProfileResponseModel>> {
        return profileRemoteDao.editProfileBackground(id)
    }

    override fun editProfileUserInfo(editProfileUserInfoRequestModel: EditProfileUserInfoRequestModel): Single<ResponseWrapper<ProfileResponseModel>> {
        return profileRemoteDao.editProfileUserInfo(
            editProfileUserInfoRequestModel
        )
    }

    override fun editProfile(editProfileRequestModel: EditProfileRequestModel): Single<ResponseWrapper<ProfileResponseModel>> {
        return profileRemoteDao.editProfile(
            editProfileRequestModel
        )
    }

    override fun editProfilePicture(profileImage: MultipartBody.Part?): Single<ResponseWrapper<ProfileResponseModel>> {
        return profileRemoteDao.editProfilePicture(
            profileImage
        )
    }

    override fun editUserIntereste(
        editUserInteresteRequest: EditUserInteresteRequest
    ): Single<ResponseWrapper<List<ActiveInterestsResponseModel>>> {
        return profileRemoteDao.editUserIntereste(editUserInteresteRequest)
    }

}