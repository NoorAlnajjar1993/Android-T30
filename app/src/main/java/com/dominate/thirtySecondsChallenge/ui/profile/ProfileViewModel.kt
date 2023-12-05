package com.dominate.thirtySecondsChallenge.ui.profile

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dominate.thirtySecondsChallenge.base.viewmodel.BaseViewModel
import com.dominate.thirtySecondsChallenge.data.model.applicationsetting.SocialLinkResponseModel
import com.dominate.thirtySecondsChallenge.data.model.friends.UserFriendsResponseModel
import com.dominate.thirtySecondsChallenge.data.model.profile.ActiveInterestsResponseModel
import com.dominate.thirtySecondsChallenge.data.model.profile.ActiveLanguagesResponseModel
import com.dominate.thirtySecondsChallenge.data.model.profile.BackgroundImagesProfileResponseModel
import com.dominate.thirtySecondsChallenge.data.model.profile.BadgesResponseModel
import com.dominate.thirtySecondsChallenge.data.model.profile.FaqCategoryResponseModel
import com.dominate.thirtySecondsChallenge.data.model.profile.GetAllFaqsResponseModel
import com.dominate.thirtySecondsChallenge.data.model.profile.GiftProfileResponseModel
import com.dominate.thirtySecondsChallenge.data.model.profile.InterestsResponseModel
import com.dominate.thirtySecondsChallenge.data.model.profile.ProfileResponseModel
import com.dominate.thirtySecondsChallenge.data.model.profile.RewardsResponseModel
import com.dominate.thirtySecondsChallenge.data.model.profile.request.DtoEditProfileModel
import com.dominate.thirtySecondsChallenge.data.model.profile.request.DtoEditProfileUserModel
import com.dominate.thirtySecondsChallenge.data.model.profile.request.EditProfileRequestModel
import com.dominate.thirtySecondsChallenge.data.model.profile.request.EditProfileUserInfoRequestModel
import com.dominate.thirtySecondsChallenge.data.model.profile.request.EditUserInteresteRequest
import com.dominate.thirtySecondsChallenge.data.pref.user.UserPref
import com.dominate.thirtySecondsChallenge.data.repo.applicationsetting.ApplicationSettingRepo
import com.dominate.thirtySecondsChallenge.data.repo.auth.UserRepo
import com.dominate.thirtySecondsChallenge.data.repo.friends.FriendsRepo
import com.dominate.thirtySecondsChallenge.data.repo.profile.ProfileRepo
import com.dominate.thirtySecondsChallenge.data.response.Result
import com.dominate.thirtySecondsChallenge.utils.plus
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class ProfileViewModel @ViewModelInject constructor(
    private val userRepo: UserRepo,
    private val profileRepo: ProfileRepo,
    private val applicationSettingRepo: ApplicationSettingRepo,
    private val friendsRepo: FriendsRepo,
    private val userPref: UserPref,
) : BaseViewModel() {

    val socialMediaId: MutableLiveData<String> by lazy { MutableLiveData<String>("null") }

    val personName: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val firstName: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val lastName: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val personId: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val userLevelTypeTitle: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val phone: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val email: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val xp: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val coins: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val imagePerson: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val imagePersonBackground: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    val isConfirmDelete: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(false) }
    val titleConfirmDelete: MutableLiveData<String> by lazy { MutableLiveData<String>("هل تريد تعطيل حسابك؟") }
    val confirmDeleteAccount: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    var imagePath: String = ""

    val isSound: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val isMusic: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    // get profile info
    var giftModel: List<GiftProfileResponseModel> = arrayListOf()

    val profileResult: MutableLiveData<Result<ProfileResponseModel>> =
        MutableLiveData()

    val giftList = mutableListOf<GiftProfileResponseModel>()
    val badgesList = mutableListOf<BadgesResponseModel>()
    val rewardsList = mutableListOf<RewardsResponseModel>()
    val interestsList = mutableListOf<InterestsResponseModel>()

    fun profileApi(): MutableLiveData<Result<ProfileResponseModel>> {
        profileResult.value = Result.Loading
        compositeDisposable + profileRepo.getProfile()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.succeeded) {

                    profileResult.value = Result.Success(it.data, it.message)
                    it.data?.userInfo?.let {
                        personName.value = "${it.firstName ?: ""} ${it.lastName ?: ""}"
                        firstName.value = if (it.firstName.isNullOrEmpty()) {
                            ""
                        } else {
                            it.firstName.takeIf { it.isNotEmpty() } ?: ""
                        }
                        lastName.value = if (it.lastName.isNullOrEmpty()) {
                            ""
                        } else {
                            it.lastName.takeIf { it.isNotEmpty() } ?: ""
                        }
                        personId.value = it.id.toString()
                        userLevelTypeTitle.value = it.userLevelTypeTitle
                        phone.value = if (it.phone.isNullOrEmpty()) {
                            ""
                        } else {
                            it.phone.takeIf { it.isNotEmpty() } ?: ""
                        }
                        email.value = if (it.email.isNullOrEmpty()) {
                            ""
                        } else {
                            it.email.takeIf { it.isNotEmpty() } ?: ""
                        }
                        xp.value = it.xp.toString()
                        coins.value = it.coins.toString()
                        imagePerson.value = it.profileImageUrl
                        imagePersonBackground.value = it.backgroundImageUrl
                    }
                    giftModel = it.data?.gifts ?: arrayListOf()

                } else {
                    profileResult.value =
                        Result.CustomError(
                            errorCode = it.error?.code,
                            message = it.error?.message
                        )
                }
            }, {
                profileResult.value = Result.Error(it)
            })
        return profileResult
    }

    // select firend list
    val isEmptyListFriends: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(false) }

    // get all background
    val backgroundImagesResult: MutableLiveData<Result<List<BackgroundImagesProfileResponseModel>>> =
        MutableLiveData()

    val _getAllBackgroundImagesListLiveData =
        MutableLiveData<List<BackgroundImagesProfileResponseModel>>()
    val getAllBackgroundImagesLiveData: LiveData<List<BackgroundImagesProfileResponseModel>> get() = _getAllBackgroundImagesListLiveData

    fun getAllBackgroundImages(): MutableLiveData<Result<List<BackgroundImagesProfileResponseModel>>> {
        backgroundImagesResult.value = Result.Loading
        compositeDisposable + profileRepo.getAllBackgroundImages()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.succeeded) {

                    backgroundImagesResult.value = Result.Success(it.data, it.message)

                } else {
                    backgroundImagesResult.value =
                        Result.CustomError(
                            errorCode = it.error?.code,
                            message = it.error?.message
                        )
                }
            }, {
                backgroundImagesResult.value = Result.Error(it)
            })
        return backgroundImagesResult
    }


    // faq
    val fAQResult: MutableLiveData<Result<List<FaqCategoryResponseModel>>> =
        MutableLiveData()

    fun getFaqCategory(): MutableLiveData<Result<List<FaqCategoryResponseModel>>> {
        fAQResult.value = Result.Loading
        compositeDisposable + profileRepo.getFaqCategory()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.succeeded) {

                    fAQResult.value = Result.Success(it.data, it.message)

                } else {
                    fAQResult.value =
                        Result.CustomError(
                            errorCode = it.error?.code,
                            message = it.error?.message
                        )
                }
            }, {
                fAQResult.value = Result.Error(it)
            })
        return fAQResult
    }

    val allFaqsResult: MutableLiveData<Result<List<GetAllFaqsResponseModel>>> =
        MutableLiveData()

    val allFaqsModel: MutableLiveData<List<GetAllFaqsResponseModel>> =
        MutableLiveData()

    fun getAllFaqs(id: Int): MutableLiveData<Result<List<GetAllFaqsResponseModel>>> {
        allFaqsResult.value = Result.Loading
        compositeDisposable + profileRepo.getAllFaqs(
            id
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.succeeded) {
                    allFaqsResult.value = Result.Success(it.data, it.message)
                    allFaqsModel.value = it.data
                } else {
                    allFaqsResult.value =
                        Result.CustomError(
                            errorCode = it.error?.code,
                            message = it.error?.message
                        )
                }
            }, {
                allFaqsResult.value = Result.Error(it)
            })
        return allFaqsResult
    }

    // get active language
    val languagesEntry: MutableLiveData<List<String>> = MutableLiveData(arrayListOf())
    val selectedLanguagesPosition: MutableLiveData<Int> by lazy { MutableLiveData<Int>(0) }

    val activeLanguagesResult: MutableLiveData<Result<List<ActiveLanguagesResponseModel>>> =
        MutableLiveData()

    fun getActiveLanguages(): MutableLiveData<Result<List<ActiveLanguagesResponseModel>>> {
        activeLanguagesResult.value = Result.Loading
        compositeDisposable + profileRepo.getActiveLanguages()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.succeeded) {
                    activeLanguagesResult.value = Result.Success(it.data, it.message)
                    languagesEntry.value = it.data?.map { it.title }
                } else {
                    activeLanguagesResult.value =
                        Result.CustomError(
                            errorCode = it.error?.code,
                            message = it.error?.message
                        )
                }
            }, {
                activeLanguagesResult.value = Result.Error(it)
            })
        return activeLanguagesResult
    }

    // edit Profile Background
    val selectedProfileBackgroundId: MutableLiveData<Int> by lazy { MutableLiveData<Int>(-1) }

    val editProfileBackgroundResult: MutableLiveData<Result<ProfileResponseModel>> =
        MutableLiveData()

    fun editProfileBackground(id: Int): MutableLiveData<Result<ProfileResponseModel>> {
        editProfileBackgroundResult.value = Result.Loading
        compositeDisposable + profileRepo.editProfileBackground(
            id = id
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.succeeded) {
                    editProfileBackgroundResult.value = Result.Success(it.data, it.message)
                    imagePersonBackground.value = it.data?.userInfo?.backgroundImageUrl

                } else {
                    editProfileBackgroundResult.value =
                        Result.CustomError(
                            errorCode = it.error?.code,
                            message = it.error?.message
                        )
                }
            }, {
                editProfileBackgroundResult.value = Result.Error(it)
            })
        return editProfileBackgroundResult
    }

    // edit Profile UserInfo
    val editProfileUserInfoResult: MutableLiveData<Result<ProfileResponseModel>> =
        MutableLiveData()

    fun editProfileUserInfo(): MutableLiveData<Result<ProfileResponseModel>> {
        editProfileUserInfoResult.value = Result.Loading
        compositeDisposable + profileRepo.editProfileUserInfo(
            EditProfileUserInfoRequestModel(
                dto = DtoEditProfileUserModel(
                    firstName = firstName.value.toString(),
                    lastName = lastName.value.toString(),
                    email = email.value.toString(),
                    phone = phone.value.toString()
                )
            )
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.succeeded) {
                    editProfileUserInfoResult.value = Result.Success(it.data, it.message)
                    it.data?.userInfo?.let {
                        personName.value = "${it.firstName ?: ""} ${it.lastName ?: ""}"
                        firstName.value = if (it.firstName.isNullOrEmpty()) {
                            ""
                        } else {
                            it.firstName.takeIf { it.isNotEmpty() } ?: ""
                        }
                        lastName.value = if (it.lastName.isNullOrEmpty()) {
                            ""
                        } else {
                            it.lastName.takeIf { it.isNotEmpty() } ?: ""
                        }
                        phone.value = if (it.phone.isNullOrEmpty()) {
                            ""
                        } else {
                            it.phone.takeIf { it.isNotEmpty() } ?: ""
                        }
                        email.value = if (it.email.isNullOrEmpty()) {
                            ""
                        } else {
                            it.email.takeIf { it.isNotEmpty() } ?: ""
                        }
                    }

                } else {
                    editProfileUserInfoResult.value =
                        Result.CustomError(
                            errorCode = it.error?.code,
                            message = it.error?.message
                        )
                }
            }, {
                editProfileUserInfoResult.value = Result.Error(it)
            })
        return editProfileUserInfoResult
    }

    // edit Profile
    val editProfileResult: MutableLiveData<Result<ProfileResponseModel>> =
        MutableLiveData()

    fun editProfile(
        userRewards: ArrayList<Int>,
        userGifts: ArrayList<Int>,
        userBadges: ArrayList<Int>
    ): MutableLiveData<Result<ProfileResponseModel>> {
        editProfileResult.value = Result.Loading
        compositeDisposable + profileRepo.editProfile(
            EditProfileRequestModel(
                dto = DtoEditProfileModel(
                    userRewards = userRewards,
                    userBadges = userBadges,
                    userGifts = userGifts
                )
            )
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.succeeded) {
                    editProfileResult.value = Result.Success(it.data, it.message)
                    it.data?.userInfo?.let {
                        personName.value = "${it.firstName ?: ""} ${it.lastName ?: ""}"
                        firstName.value = if (it.firstName.isNullOrEmpty()) {
                            ""
                        } else {
                            it.firstName.takeIf { it.isNotEmpty() } ?: ""
                        }
                        lastName.value = if (it.lastName.isNullOrEmpty()) {
                            ""
                        } else {
                            it.lastName.takeIf { it.isNotEmpty() } ?: ""
                        }
                        phone.value = if (it.phone.isNullOrEmpty()) {
                            ""
                        } else {
                            it.phone.takeIf { it.isNotEmpty() } ?: ""
                        }
                        email.value = if (it.email.isNullOrEmpty()) {
                            ""
                        } else {
                            it.email.takeIf { it.isNotEmpty() } ?: ""
                        }
                    }

                } else {
                    editProfileResult.value =
                        Result.CustomError(
                            errorCode = it.error?.code,
                            message = it.error?.message
                        )
                }
            }, {
                editProfileResult.value = Result.Error(it)
            })
        return editProfileResult
    }

    // edit Profile
    val editProfilePictureResult: MutableLiveData<Result<ProfileResponseModel>> =
        MutableLiveData()

    fun editProfilePicture(): MutableLiveData<Result<ProfileResponseModel>> {
        editProfilePictureResult.value = Result.Loading
        compositeDisposable + profileRepo.editProfilePicture(
            profileImage = if (!imagePath.isNullOrEmpty()) {
                uploadImage()
            } else {
                null
            }
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.succeeded) {
                    editProfilePictureResult.value = Result.Success(it.data, it.message)
                    it.data?.userInfo?.let {
                        personName.value = "${it.firstName ?: ""} ${it.lastName ?: ""}"
                        firstName.value = if (it.firstName.isNullOrEmpty()) {
                            ""
                        } else {
                            it.firstName.takeIf { it.isNotEmpty() } ?: ""
                        }
                        lastName.value = if (it.lastName.isNullOrEmpty()) {
                            ""
                        } else {
                            it.lastName.takeIf { it.isNotEmpty() } ?: ""
                        }
                        phone.value = if (it.phone.isNullOrEmpty()) {
                            ""
                        } else {
                            it.phone.takeIf { it.isNotEmpty() } ?: ""
                        }
                        email.value = if (it.email.isNullOrEmpty()) {
                            ""
                        } else {
                            it.email.takeIf { it.isNotEmpty() } ?: ""
                        }
                        imagePerson.value = it.profileImageUrl
                    }

                } else {
                    editProfilePictureResult.value =
                        Result.CustomError(
                            errorCode = it.error?.code,
                            message = it.error?.message
                        )
                }
            }, {
                editProfilePictureResult.value = Result.Error(it)
            })
        return editProfilePictureResult
    }

    fun uploadImage(): MultipartBody.Part {
        val file = File(imagePath)
        val requestFile = RequestBody.create("application/octet-stream".toMediaTypeOrNull(), file)
        val filePart = MultipartBody.Part.createFormData(
            "dto.ProfileImage",
            "${file.name}.png",
            requestFile
        )
        return filePart
    }

    // edit User Intereste
    val editUserInteresteResult: MutableLiveData<Result<List<ActiveInterestsResponseModel>>> =
        MutableLiveData()

    fun editUserIntereste(listOfInterested: ArrayList<Int>): MutableLiveData<Result<List<ActiveInterestsResponseModel>>> {
        editUserInteresteResult.value = Result.Loading
        compositeDisposable + profileRepo.editUserIntereste(
            EditUserInteresteRequest(
                ids = listOfInterested ?: arrayListOf()
            )
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.succeeded) {
                    editUserInteresteResult.value = Result.Success(it.data, it.message)
                } else {
                    editUserInteresteResult.value =
                        Result.CustomError(
                            errorCode = it.error?.code,
                            message = it.error?.message
                        )
                }
            }, {
                editUserInteresteResult.value = Result.Error(it)
            })
        return editUserInteresteResult
    }

    // edit User Intereste
    val socialLinksResult: MutableLiveData<Result<List<SocialLinkResponseModel>>> =
        MutableLiveData()

    val facebookUrl: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val youtubeUrl: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val instagramUrl: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    fun getSocialLinks(): MutableLiveData<Result<List<SocialLinkResponseModel>>> {
        socialLinksResult.value = Result.Loading
        compositeDisposable + applicationSettingRepo.getSocialLinks()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.succeeded) {
                    socialLinksResult.value = Result.Success(it.data, it.message)

                    it.data?.let {
                        facebookUrl.value = it[0].value
                        youtubeUrl.value = it[1].value
                        instagramUrl.value = it[2].value
                    }

                } else {
                    socialLinksResult.value =
                        Result.CustomError(
                            errorCode = it.error?.code,
                            message = it.error?.message
                        )
                }
            }, {
                socialLinksResult.value = Result.Error(it)
            })
        return socialLinksResult
    }

    // get All User Friends
    val getAllUserFriendsResult: MutableLiveData<Result<List<UserFriendsResponseModel>>> =
        MutableLiveData()

    fun getAllUserFriends(): MutableLiveData<Result<List<UserFriendsResponseModel>>> {
        getAllUserFriendsResult.value = Result.Loading
        compositeDisposable + friendsRepo.getAllUserFriends()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.succeeded) {
                    getAllUserFriendsResult.value = Result.Success(it.data,it.message)
                } else {
                    getAllUserFriendsResult.value =
                        Result.CustomError(
                            errorCode = it.error?.code,
                            message = it.error?.message
                        )
                }
            }, {
                getAllUserFriendsResult.value = Result.Error(it)
            })
        return getAllUserFriendsResult
    }

    fun clearData() {
        giftList.clear()
        badgesList.clear()
        rewardsList.clear()
        interestsList.clear()
    }


}