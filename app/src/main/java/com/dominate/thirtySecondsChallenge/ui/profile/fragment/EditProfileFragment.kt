package com.dominate.thirtySecondsChallenge.ui.profile.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import butterknife.ButterKnife
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.adapters.BaseBindingRecyclerViewAdapterAny
import com.dominate.thirtySecondsChallenge.base.fragment.BaseBindingFragment
import com.dominate.thirtySecondsChallenge.data.model.profile.BackgroundImagesProfileResponseModel
import com.dominate.thirtySecondsChallenge.data.model.profile.BadgesResponseModel
import com.dominate.thirtySecondsChallenge.data.model.profile.GiftProfileResponseModel
import com.dominate.thirtySecondsChallenge.data.model.profile.InterestsResponseModel
import com.dominate.thirtySecondsChallenge.data.model.profile.ProfileResponseModel
import com.dominate.thirtySecondsChallenge.data.model.profile.RewardsResponseModel
import com.dominate.thirtySecondsChallenge.data.response.Result
import com.dominate.thirtySecondsChallenge.databinding.FragmentEditProfileBinding
import com.dominate.thirtySecondsChallenge.ui.onboarding.OnBoardingStepThreeFragment
import com.dominate.thirtySecondsChallenge.ui.profile.ProfileFragmentDirections
import com.dominate.thirtySecondsChallenge.ui.profile.ProfileViewModel
import com.dominate.thirtySecondsChallenge.ui.profile.adapter.AchievementAdapter
import com.dominate.thirtySecondsChallenge.ui.profile.adapter.BadgesAdapter
import com.dominate.thirtySecondsChallenge.ui.profile.adapter.InterestsAdapter
import com.dominate.thirtySecondsChallenge.ui.profile.adapter.LevelRewardsAdapter
import com.dominate.thirtySecondsChallenge.ui.profile.dialog.EditBackgroundProfileFragment
import com.dominate.thirtySecondsChallenge.utils.CustomProgressBar
import com.dominate.thirtySecondsChallenge.utils.HandleRequestFailedUtil
import com.dominate.thirtySecondsChallenge.utils.Utils
import com.dominate.thirtySecondsChallenge.utils.extensions.onClick
import com.dominate.thirtySecondsChallenge.utils.extensions.shortToast
import com.dominate.thirtySecondsChallenge.utils.extensions.showSnackbar
import com.dominate.thirtySecondsChallenge.utils.pickImages
import com.github.dhaval2404.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import javax.inject.Inject

@AndroidEntryPoint
class EditProfileFragment : BaseBindingFragment<FragmentEditProfileBinding>(),
    BaseBindingRecyclerViewAdapterAny.OnItemClickListener {

    override fun getLayoutId(): Int = R.layout.fragment_edit_profile

    private val viewModel by activityViewModels<ProfileViewModel>()

    lateinit var achievementAdapter: AchievementAdapter

    lateinit var levelRewardsAdapter: LevelRewardsAdapter

    lateinit var giftAdapter: BadgesAdapter

    lateinit var bitmapId: Bitmap

    @Inject
    lateinit var interestsAdapter: InterestsAdapter

    companion object {
        const val FLAG_EDIT_ACHIEVEMENT = 2222
        const val FLAG_EDIT_LEVELREWARDS = 3333
        const val FLAG_EDIT_GIFT = 4444
        const val IMAGEPICTUREID = 5555
    }

    var userRewards: ArrayList<Int> = ArrayList()
    var userBadges: ArrayList<Int> = ArrayList()
    var userGifts: ArrayList<Int> = ArrayList()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)

        setUpData()
        setUpViewsListeners()
        setRecycleView()
    }


    private fun setUpData() {
        binding?.viewModel = viewModel

        viewModel.getAllBackgroundImages().observe(viewLifecycleOwner, getAllBackgroundImages)

    }

    private fun setUpViewsListeners() {

        iv_back.onClick {
            findNavController().popBackStack()
        }

        tv_save.onClick {

            // badges
            userBadges.clear()
            achievementAdapter.items.forEach {
                if (it.isSelected) {
                    userBadges.addAll(listOf(it.badgeId))
                }
            }

            // Gifts
            userGifts.clear()
            giftAdapter.items.forEach {
                if (it.isSelected) {
                    userGifts.addAll(listOf(it.giftId))
                }
            }

            // Rewards
            userRewards.clear()
            levelRewardsAdapter.items.forEach {
                if (it.isSelected) {
                    userRewards.addAll(listOf(it.rewardId))
                }
            }
            viewModel.editProfile(userRewards, userGifts, userBadges)
                .observe(viewLifecycleOwner, editProfile)
        }

        iv_edit_bg.onClick {
            val editBackgroundProfileFragment = EditBackgroundProfileFragment()
            editBackgroundProfileFragment.show(parentFragmentManager, " Edit Background")

        }

        tv_AddItem.onClick {
            findNavController().navigate(
                ProfileFragmentDirections.actionGlobalOnBoardingStepThreeFragment(
                    OnBoardingStepThreeFragment.ISPROFILE
                )
            )
        }

        iv_ImageProfile.onClick {
            pickImages(
                requestCode = IMAGEPICTUREID
            )
        }

    }

    private fun setRecycleView() {

        val layoutManager = GridLayoutManager(requireContext(), 4)

        achievementAdapter = AchievementAdapter(requireContext(), 2, FLAG_EDIT_ACHIEVEMENT)
        rv_EditAchievements.adapter = achievementAdapter
        rv_EditAchievements.layoutManager = layoutManager
        achievementAdapter.itemClickListener = this

        if (viewModel.badgesList.isNullOrEmpty()) {
            val badgesList = mutableListOf<BadgesResponseModel>()
            repeat(4) {
                badgesList.add(
                    BadgesResponseModel(
                        id = -1,
                        userPlayerId = -1,
                        isVisible = false,
                        badgeId = -1,
                        image = null,
                        imageUrl = null,
                        language = "ar"
                    )
                )
            }
            achievementAdapter.submitItems(badgesList)
        } else {
            viewModel.badgesList.forEach {
                it.isSelected = it.isVisible
            }
            achievementAdapter.submitItems(viewModel.badgesList)
        }

        requireContext().let {
            levelRewardsAdapter = LevelRewardsAdapter(it, 2, FLAG_EDIT_LEVELREWARDS)
            rv_EditLevelRewards.adapter = levelRewardsAdapter
            levelRewardsAdapter.itemClickListener = this
            if (viewModel.rewardsList.isNullOrEmpty()) {
                val rewardsList = mutableListOf<RewardsResponseModel>()
                repeat(4) {
                    rewardsList.add(
                        RewardsResponseModel(
                            id = -1,
                            userPlayerId = -1,
                            rewardId = -1,
                            title = "",
                            description = "",
                            image = null,
                            imageUrl = null,
                            isVisible = false,
                            level = -1,
                            language = ""
                        )
                    )
                }
                levelRewardsAdapter.submitItems(rewardsList)
            } else {
                viewModel.rewardsList.forEach {
                    it.isSelected = it.isVisible
                }
                levelRewardsAdapter.submitItems(viewModel.rewardsList)
            }
        }

        requireContext().let {
            giftAdapter = BadgesAdapter(requireContext(), 2, FLAG_EDIT_GIFT)
            rv_EditGifts.adapter = giftAdapter
            giftAdapter.itemClickListener = this
            if (viewModel.giftList.isEmpty()) {
                val giftList = mutableListOf<GiftProfileResponseModel>()
                repeat(4) {
                    giftList.add(
                        GiftProfileResponseModel(
                            id = -1,
                            userPlayerId = -1,
                            isVisible = false,
                            giftId = -1,
                            image = null,
                            imageUrl = null,
                            title = "",
                            description = "",
                            isFree = false,
                            price = 1.1f,
                            count = 0,
                            language = "ar"
                        )
                    )
                }
                giftAdapter.submitItems(giftList)
            } else {
                viewModel.giftList.forEach {
                    it.isSelected = it.isVisible
                }
                giftAdapter.submitItems(viewModel.giftList)
            }
        }

        interestsAdapter = InterestsAdapter()
        rv_EditInterests.adapter = interestsAdapter

        if (viewModel.interestsList.isNullOrEmpty()) {
            val interestsList = mutableListOf<InterestsResponseModel>()
            repeat(4) {
                interestsList.add(
                    InterestsResponseModel(
                        id = -1,
                        title = "",
                        description = "",
                        image = null,
                        imageUrl = null,
                        isVisible = false,
                        language = ""
                    )
                )
            }
            interestsAdapter.submitItems(interestsList)
        } else {
            interestsAdapter.submitItems(viewModel.interestsList)

        }

    }

    @SuppressLint("ResourceAsColor")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == IMAGEPICTUREID) {
//                Glide.with(requireContext()).load(data?.data.toString()).into(ivPerson)
                try {
                    val contentURI = data!!.data
                    viewModel.imagePath = Utils.getRealPathFromURI(contentURI!!, requireContext())
                    viewModel.editProfilePicture().observe(viewLifecycleOwner, editProfilePicture)
                } catch (e: Exception) {
                }
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                shortToast(ImagePicker.getError(data))
            } else {
            }
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private val getAllBackgroundImages =
        Observer<Result<List<BackgroundImagesProfileResponseModel>>> {
            when (it) {
                is Result.Success -> {
                    CustomProgressBar.hide(requireContext())
                    viewModel._getAllBackgroundImagesListLiveData.value = it.data
                }

                is Result.Error -> {
                    CustomProgressBar.hide(requireContext())
                    handleError(it.throwable)
                }

                is Result.CustomError -> {
                    CustomProgressBar.hide(requireContext())
                    HandleRequestFailedUtil.showDialogMessage(
                        it.message, requireContext(), childFragmentManager
                    )
                }

                is Result.Loading -> {
                    CustomProgressBar.show(requireContext())
                }
            }
        }

    private val editProfilePicture =
        Observer<Result<ProfileResponseModel>> {
            when (it) {
                is Result.Success -> {
                    CustomProgressBar.hide(requireContext())
                    shortToast(it.message)
                }

                is Result.Error -> {
                    CustomProgressBar.hide(requireContext())
                    handleError(it.throwable)
                }

                is Result.CustomError -> {
                    CustomProgressBar.hide(requireContext())
                    HandleRequestFailedUtil.showDialogMessage(
                        it.message, requireContext(), childFragmentManager
                    )
                }

                is Result.Loading -> {
                    CustomProgressBar.show(requireContext())
                }
            }
        }

    private val editProfile =
        Observer<Result<ProfileResponseModel>> {
            when (it) {
                is Result.Success -> {
                    CustomProgressBar.hide(requireContext())
                    requireView().showSnackbar(it.message.toString(), R.drawable.snackbar)
                    findNavController().popBackStack()
                }

                is Result.Error -> {
                    CustomProgressBar.hide(requireContext())
                    handleError(it.throwable)
                }

                is Result.CustomError -> {
                    CustomProgressBar.hide(requireContext())
                    HandleRequestFailedUtil.showDialogMessage(
                        it.message, requireContext(), childFragmentManager
                    )
                }

                is Result.Loading -> {
                    CustomProgressBar.show(requireContext())
                }
            }
        }

    override fun onItemClick(view: View?, position: Int, item: Any, flag: Int) {


        when (flag) {

            FLAG_EDIT_ACHIEVEMENT -> {
                item as BadgesResponseModel

                if (item.id != -1) {

                    item.isSelected = item.isSelected != true
                    achievementAdapter.notifyItemChanged(position)

                }
            }

            FLAG_EDIT_LEVELREWARDS -> {
                item as RewardsResponseModel

                if (item.id != -1) {

                    item.isSelected = item.isSelected != true
                    levelRewardsAdapter.notifyItemChanged(position)

                }
            }

            FLAG_EDIT_GIFT -> {
                item as GiftProfileResponseModel

                if (item.id != -1) {

                    item.isSelected = item.isSelected != true
                    giftAdapter.notifyItemChanged(position)

                }
            }

        }
    }

}