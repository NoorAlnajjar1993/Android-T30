package com.dominate.thirtySecondsChallenge.ui.home

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import butterknife.ButterKnife
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.adapters.BaseBindingRecyclerViewAdapter
import com.dominate.thirtySecondsChallenge.base.adapters.OnItemClickListener
import com.dominate.thirtySecondsChallenge.base.fragment.BaseBindingFragment
import com.dominate.thirtySecondsChallenge.common.MyApplication
import com.dominate.thirtySecondsChallenge.data.model.home.AdsResponseModel
import com.dominate.thirtySecondsChallenge.data.model.home.HomeResponseModel
import com.dominate.thirtySecondsChallenge.data.pref.user.UserPref
import com.dominate.thirtySecondsChallenge.data.response.Result
import com.dominate.thirtySecondsChallenge.databinding.FragmentHomeBinding
import com.dominate.thirtySecondsChallenge.ui.auth.AuthFragment
import com.dominate.thirtySecondsChallenge.ui.auth.AuthViewModel
import com.dominate.thirtySecondsChallenge.ui.gamelobbyinterface.GameLobbyInterfaceFragment
import com.dominate.thirtySecondsChallenge.ui.gamelobbyinterface.GameLobbyInterfaceFragment.Companion.IS_INVITE_GAME_ID
import com.dominate.thirtySecondsChallenge.ui.gamelobbyinterface.GameLobbyInterfaceFragmentArgs
import com.dominate.thirtySecondsChallenge.ui.gamelobbyinterface.GameLobbyViewModel
import com.dominate.thirtySecondsChallenge.ui.home.adapter.BannerAdapter
import com.dominate.thirtySecondsChallenge.ui.home.adapter.SportAdapter
import com.dominate.thirtySecondsChallenge.ui.home.model.SportModel
import com.dominate.thirtySecondsChallenge.ui.levelup.fragment.ShowInProfileFragment
import com.dominate.thirtySecondsChallenge.ui.profile.ProfileFragmentDirections
import com.dominate.thirtySecondsChallenge.utils.CustomProgressBar
import com.dominate.thirtySecondsChallenge.utils.HandleRequestFailedUtil
import com.dominate.thirtySecondsChallenge.utils.dialog.showDialogCollectReward
import com.dominate.thirtySecondsChallenge.utils.extensions.*
import com.google.firebase.dynamiclinks.PendingDynamicLinkData
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseBindingFragment<FragmentHomeBinding>(),
    OnItemClickListener<SportModel> {

    override fun getLayoutId(): Int = R.layout.fragment_home

    companion object {
        const val TOURNAMENT_ID = 1
        const val GAME_Id = 2
        const val CREATE_GAME_Id = 3
        const val RV_SPORT_ID = 11111

        const val COLLECT_WIN_ID = 22222
    }

    private val viewModel by activityViewModels<HomeViewModel>()
    private val authViewModel by activityViewModels<AuthViewModel>()
    private val gameLobbyViewModel by activityViewModels<GameLobbyViewModel>()

    @Inject
    lateinit var sportAdapter: SportAdapter

    lateinit var bannerAdapter: BannerAdapter

    @Inject
    lateinit var userPref: UserPref

    private lateinit var animationShine: Animation

    var myApplication: MyApplication? = null

    private val args: HomeFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)

//        binding?.executePendingBindings()

        setUpData()
        setUpViewsListeners()
        setRecycleView()
        onBackPressed()
    }

    override fun onViewVisible() {
        super.onViewVisible()

    }

    private fun setUpData() {
        binding?.viewModel = viewModel

        viewModel.getHome().observe(viewLifecycleOwner, getHome)
        setAnimation()

        when (args.endGame) {

            COLLECT_WIN_ID -> {
                if (gameLobbyViewModel.collectionFinishDataJsonModel?.AchievedBadges != null) {
                    if (gameLobbyViewModel.collectionFinishDataJsonModel?.AchievedBadges!!.isEmpty()) {
                        if (gameLobbyViewModel.userCoins.value != 0 && gameLobbyViewModel.userXp.value != 0) {
                            requireContext().showDialogCollectReward(
                                userCoins = gameLobbyViewModel.userCoins.value,
                                userXp = gameLobbyViewModel.userXp.value,
                                userLevel = userPref.getUser()?.level?.toInt() ?: 0
                            )
                        }
                        gameLobbyViewModel.collectionFinishDataJsonModel?.AchievedBadges = null
                    } else {
                        val showInProfileFragment = ShowInProfileFragment.newInstance(
                            message = null,
                            imageUrl = gameLobbyViewModel.collectionFinishDataJsonModel?.AchievedBadges!![0].ImageUrl
                        )
                        showInProfileFragment.show(
                            parentFragmentManager,
                            "show In Profile Fragment"
                        )
                        gameLobbyViewModel.collectionFinishDataJsonModel?.AchievedBadges = null
                    }
                }
            }
            else -> {
            }

        }

    }

    private fun isInviteGame() {
        // check invite game
        Firebase.dynamicLinks
            .getDynamicLink(requireActivity().intent)
            .addOnSuccessListener(requireActivity()) { pendingDynamicLinkData: PendingDynamicLinkData? ->
                var deepLink: Uri? = null
                if (pendingDynamicLinkData != null) {
                    deepLink = pendingDynamicLinkData.link
                    Log.d("deepLink", deepLink.toString())
                    if (deepLink.toString().contains("inviteGame")){
                        val inviteGame = deepLink?.getQueryParameter("inviteGame")
                        if (!deepLink.toString().isNullOrEmpty() && gameLobbyViewModel.inviteGameId.value.toString() != inviteGame.toString()) {
                            gameLobbyViewModel.inviteGameId.value = inviteGame.toString().toInt()
                            findNavController().navigate(
                                HomeFragmentDirections.actionGlobalGameLobbyFragment(
                                    IS_INVITE_GAME_ID
                                )
                            )
                        }
                    }

                    if (deepLink.toString().contains("referralCode")){
                        val refCode = deepLink?.getQueryParameter("referralCode")
                        authViewModel.referralCode.value = refCode
                    }

                }else{
                    Log.d("deepLink", "null")
                }
            }
            .addOnFailureListener(requireActivity()) { e -> Log.i("sssss", "getDynamicLink:onFailure", e) }

    }


    private fun setUpViewsListeners() {

        tv_LeaderBoard.onClick {
            if (userPref.getToken().isNullOrEmpty()) {
                val authFragment = AuthFragment()
                authFragment.show(parentFragmentManager, "auth Fragment")
            } else {
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToLeaderBoardsFragment()
                )
            }

        }

        iv_banners.onClick {
            findNavController().navigate(
                ProfileFragmentDirections.actionGlobalWebViewFragment(
                    "title",
                    viewModel.urlBanner.value.toString()
                )
            )
        }

        tv_onGoingGame.onClick {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToOngoingGamesFragment())
        }


    }

    private fun setRecycleView() {
        requireContext().let {
            sportAdapter = SportAdapter()
            rv_Sport.adapter = sportAdapter
            sportAdapter.onItemClickListener = this
            sportAdapter.submitItems(
                listOf(
                    SportModel(
                        id = CREATE_GAME_Id,
                        R.drawable.ic_create_game_img,
                        R.drawable.ic_create_tournament,
                        "انشئ لعبة"
                    ),
                    SportModel(
                        id = GAME_Id,
                        R.drawable.ic_game_img,
                        R.drawable.ic_game,
                        "العب"
                    ),
                    SportModel(
                        id = TOURNAMENT_ID,
                        R.drawable.ic_create_tournament_img,
                        R.drawable.ic_create_game,
                        "انشئ بطولة"
                    ),

                    )
            )
        }

        requireContext().let {
            bannerAdapter = BannerAdapter(it)
            rv_banner.adapter = bannerAdapter
        }
        viewModel.adsLiveData.observe(
            viewLifecycleOwner
        ) { items ->
            bannerAdapter.submitItems(items)
        }

        bannerAdapter.itemClickListener =
            object : BaseBindingRecyclerViewAdapter.OnItemClickListener {
                override fun onItemClick(view: View?, position: Int, item: Any) {
                    item as AdsResponseModel
                    if (item.redirectUrl != null && item.redirectUrl != "null" && !item.redirectUrl.isNullOrEmpty() && !item.redirectUrl.contains(
                            "http"
                        )
                    ) {
                        findNavController().navigate(
                            HomeFragmentDirections.actionGlobalWebViewFragment(
                                "title",
                                item.redirectUrl
                            )
                        )
                    }
                }
            }

    }

    private val getHome = Observer<Result<HomeResponseModel>> {
        when (it) {
            is Result.Success -> {
                CustomProgressBar.hide(requireContext())

                if (viewModel.adsLiveData.value.isNullOrEmpty()) {
                    bannerAdapter.submitItems(it.data?.ads ?: arrayListOf())
                }

                val myApplication = requireActivity().application as MyApplication
                if (!myApplication.statusConnection()) {
                    myApplication.connectToHub()
                }
                isInviteGame()

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
                if (viewModel.isGetHome.value == true) {
                    CustomProgressBar.show(requireContext())
                }
            }
        }
    }

    private fun setAnimation() {

        animationShine = AnimationUtils.loadAnimation(
            requireContext(), R.anim.shine_anim
        )
        shine.visibility = View.GONE
        shine.startAnimation(animationShine)

        animationShine.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationEnd(p0: Animation?) {
                shine.visibility = View.GONE
            }

            override fun onAnimationStart(p0: Animation?) {
                shine.startAnimation(animationShine)
                shine.visibility = View.VISIBLE

            }

            override fun onAnimationRepeat(p0: Animation?) {
                shine.visibility = View.GONE

            }
        })

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onItemClicked(view: View?, item: SportModel, position: Int) {
        vibrate(requireContext())
        when (item.id) {

            CREATE_GAME_Id -> {
                if (userPref.getToken().isNullOrEmpty()) {
                    val authFragment = AuthFragment()
                    authFragment.show(parentFragmentManager, "auth Fragment")
                } else {
                    findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToCreateGameGroupFragment())
                }
            }

            TOURNAMENT_ID -> {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToCreateTournamentFragment())

            }

            GAME_Id -> {
                if (userPref.getToken().isNullOrEmpty()) {
                    val authFragment = AuthFragment()
                    authFragment.show(parentFragmentManager, "auth Fragment")
                } else {
                    findNavController().navigate(
                        HomeFragmentDirections.actionGlobalGameLobbyFragment(
                            GameLobbyInterfaceFragment.DIRECT_GAME_ID
                        )
                    )
                }
            }
        }

    }

    private fun onBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finishAffinity()
                }
            })
    }

}