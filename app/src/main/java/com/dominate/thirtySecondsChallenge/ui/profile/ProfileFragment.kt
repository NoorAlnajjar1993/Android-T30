package com.dominate.thirtySecondsChallenge.ui.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.ButterKnife
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.adapters.BaseBindingRecyclerViewAdapterAny
import com.dominate.thirtySecondsChallenge.base.fragment.BaseBindingFragment
import com.dominate.thirtySecondsChallenge.data.model.profile.BadgesResponseModel
import com.dominate.thirtySecondsChallenge.data.model.profile.GiftProfileResponseModel
import com.dominate.thirtySecondsChallenge.data.model.profile.InterestsResponseModel
import com.dominate.thirtySecondsChallenge.data.model.profile.ProfileResponseModel
import com.dominate.thirtySecondsChallenge.data.model.profile.RewardsResponseModel
import com.dominate.thirtySecondsChallenge.data.pref.user.UserPref
import com.dominate.thirtySecondsChallenge.data.response.Result
import com.dominate.thirtySecondsChallenge.databinding.FragmentProfileBinding
import com.dominate.thirtySecondsChallenge.ui.onboarding.OnBoardingStepThreeFragment
import com.dominate.thirtySecondsChallenge.ui.profile.adapter.AchievementAdapter
import com.dominate.thirtySecondsChallenge.ui.profile.adapter.BadgesAdapter
import com.dominate.thirtySecondsChallenge.ui.profile.adapter.InterestsAdapter
import com.dominate.thirtySecondsChallenge.ui.profile.adapter.LevelRewardsAdapter
import com.dominate.thirtySecondsChallenge.ui.profile.dialog.EditAccountProfileFragment
import com.dominate.thirtySecondsChallenge.ui.profile.dialog.SettingProfileFragment
import com.dominate.thirtySecondsChallenge.ui.sound.AudioPlayer
import com.dominate.thirtySecondsChallenge.ui.sound.MusicPlayer
import com.dominate.thirtySecondsChallenge.utils.CustomProgressBar
import com.dominate.thirtySecondsChallenge.utils.HandleRequestFailedUtil
import com.dominate.thirtySecondsChallenge.utils.extensions.onClick
import com.dominate.thirtySecondsChallenge.utils.dialog.showDialogInfoDefault
import com.dominate.thirtySecondsChallenge.utils.dialog.showDialogInfoGift
import com.dominate.thirtySecondsChallenge.utils.extensions.copyText
import com.dominate.thirtySecondsChallenge.utils.extensions.vibrate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_profile.*
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : BaseBindingFragment<FragmentProfileBinding>(),
    BaseBindingRecyclerViewAdapterAny.OnItemClickListener {

    override fun getLayoutId(): Int = R.layout.fragment_profile

    private val viewModel by activityViewModels<ProfileViewModel>()

    lateinit var achievementAdapter: AchievementAdapter

    lateinit var levelRewardsAdapter: LevelRewardsAdapter

    lateinit var giftAdapter: BadgesAdapter

    @Inject
    lateinit var interestsAdapter: InterestsAdapter

    @Inject
    lateinit var userPref: UserPref

    lateinit var rewardsList: MutableList<RewardsResponseModel>

    companion object {
        const val FLAG_PROFILE = 1111
        const val FLAG_PROFILE_SELECT_GIFT = 2222
    }

    lateinit var gridLayoutManager : GridLayoutManager
    lateinit var layoutManagersHORIZONTAL: GridLayoutManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)

        setRecycleView()
        setUpData()
        setUpViewsListeners()

    }

    private fun setUpData() {
        binding?.viewModel = viewModel
        layoutManagersHORIZONTAL = GridLayoutManager(requireContext(), 2, RecyclerView.HORIZONTAL, false)
        gridLayoutManager= GridLayoutManager(requireContext(), 4)
        viewModel.clearData()
        viewModel.profileApi().observe(viewLifecycleOwner, profileApi)
        viewModel.getActiveLanguages()
    }

    private fun setUpViewsListeners() {

        iv_back.onClick {
            findNavController().popBackStack()
        }

        tv_AddItem.onClick {
            findNavController().navigate(
                ProfileFragmentDirections.actionGlobalOnBoardingStepThreeFragment(
                    OnBoardingStepThreeFragment.ISPROFILE
                )
            )
        }

        tv_EditProfile.onClick {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment())
        }

        iv_Setting.onClick {
            val settingProfileFragment = SettingProfileFragment()
            settingProfileFragment.show(parentFragmentManager, "setting Profile")
        }

        iv_edit.onClick {
            val editAccountProfileFragment = EditAccountProfileFragment()
            editAccountProfileFragment.show(parentFragmentManager, "edit Account Profile Fragment")
        }

//        ivPerson.onClick {
//            requireContext().showDialog(
//                R.drawable.ic_jawaher2,
//                "تهانينا!",
//                "حصلت على هذه الشارة لفوزك في\n ثلاث مباريات متتالية"
//            )
//        }

        tv_personId.onClick {
            vibrate(requireContext())
            requireContext().copyText(
                requireView(),
                viewModel.personId.value.toString()
            )
        }

        iv_Achievements.onClick {
            requireContext().showDialogInfoDefault(
                "الانجازات",
                "تستطيع اقتناء الملصقات المميزة واستخدامها اثناء اللعب او المحادثات ، يمكنك ايضا الحصول عليها من خلال الهدايا أو ان ترسلها انت لاصدقاءك"
            )
        }

    }

    private fun setRecycleView() {


        requireContext().let { it1 ->
            giftAdapter = BadgesAdapter(requireContext(), 1, FLAG_PROFILE_SELECT_GIFT)
            rv_Gifts.adapter = giftAdapter
            giftAdapter.itemClickListener = this
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

            achievementAdapter = AchievementAdapter(requireContext(), 1, FLAG_PROFILE)
            rv_Achievements.adapter = achievementAdapter
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

            levelRewardsAdapter = LevelRewardsAdapter(requireContext(), 1, FLAG_PROFILE)
            rv_LevelRewards.adapter = levelRewardsAdapter
            rewardsList = mutableListOf<RewardsResponseModel>()
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

            interestsAdapter = InterestsAdapter()
            rv_Interests.adapter = interestsAdapter
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

        }


    }

    private val profileApi = Observer<Result<ProfileResponseModel>> {
        when (it) {
            is Result.Success -> {
                CustomProgressBar.hide(requireContext())

                if (!it.data?.gifts.isNullOrEmpty()) {
                    viewModel.giftList.clear()
                    viewModel.giftList.addAll(it.data?.gifts!!)
                    if (it.data.gifts.filter { it.isVisible }.isNotEmpty()) {
                        giftAdapter.submitItems(it.data.gifts.filter { it.isVisible } ?: arrayListOf())
//                        if (it.data.gifts.filter { it.isVisible }.count() < 4){
//                            val item = mutableListOf<GiftProfileResponseModel>()
//                            item.addAll(it.data.gifts.filter { it.isVisible })
//                            repeat(4 - it.data.gifts.filter { it.isVisible }.count()){
//                                item.add(
//                                    GiftProfileResponseModel(
//                                        id = -1,
//                                        userPlayerId = -1,
//                                        isVisible = true,
//                                        giftId = -1,
//                                        image = null,
//                                        imageUrl = null,
//                                        title = "",
//                                        description = "",
//                                        isFree = false,
//                                        price = 1.1f,
//                                        count = 0,
//                                        language = "ar"
//                                    )
//                                )
//                            }
//                            giftAdapter.submitItems(item)
//                        }else{
//                            giftAdapter.submitItems(it.data.gifts.filter { it.isVisible } ?: arrayListOf())
//                        }

                    }
                }

                if (!it.data?.badges.isNullOrEmpty()) {
                    viewModel.badgesList.clear()
                    viewModel.badgesList.addAll(it.data?.badges!!)
                    if (it.data.badges.filter { it.isVisible }.isNotEmpty()) {
//                        if (it.data.badges.filter { it.isVisible }.count() <= 4){
//                            rv_Achievements.layoutManager = gridLayoutManager
//                        }else{
//                            rv_Achievements.layoutManager = layoutManagersHORIZONTAL
//                        }
                        achievementAdapter.submitItems(it.data.badges.filter { it.isVisible }
                            ?: arrayListOf())
                    }

                }

                if (!it.data?.rewards.isNullOrEmpty()) {
                    viewModel.rewardsList.clear()
                    viewModel.rewardsList.addAll(it.data?.rewards!!)
                    if (it.data.rewards.filter { it.isVisible }.isNotEmpty()) {
                        levelRewardsAdapter.submitItems(it.data.rewards.filter { it.isVisible }
                            ?: arrayListOf())
                    }

                }

                if (!it.data?.interests.isNullOrEmpty()) {
                    viewModel.interestsList.clear()
                    viewModel.interestsList.addAll(it.data?.interests!!)
                    if (it.data.interests.filter { it.isVisible }.isNotEmpty()) {
                        interestsAdapter.submitItems(it.data.interests.filter { it.isVisible }
                            ?: arrayListOf())
                    }

                }

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onItemClick(view: View?, position: Int, item: Any, flag: Int) {
        AudioPlayer(requireContext(), R.raw.receiving_a_gift_t30).startPlayback()

        item as GiftProfileResponseModel
        if (item.id != -1) {
            item as GiftProfileResponseModel
            requireContext().showDialogInfoGift(
                item.imageUrl,
                item.title,
                item.price.toString(),
            )
        }

    }


}