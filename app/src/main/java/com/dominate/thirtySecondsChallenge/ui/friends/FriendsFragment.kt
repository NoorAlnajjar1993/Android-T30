package com.dominate.thirtySecondsChallenge.ui.friends

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import butterknife.ButterKnife
import com.dominate.thirtySecondsChallenge.MainOperationFragmentDirections
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.adapters.OnItemClickListener
import com.dominate.thirtySecondsChallenge.base.fragment.BaseBindingFragment
import com.dominate.thirtySecondsChallenge.data.pref.user.UserPref
import com.dominate.thirtySecondsChallenge.databinding.FragmentFriendsBinding
import com.dominate.thirtySecondsChallenge.ui.auth.AuthFragment
import com.dominate.thirtySecondsChallenge.ui.friends.adapter.FriendAdapter
import com.dominate.thirtySecondsChallenge.ui.friends.model.FriendModel
import com.dominate.thirtySecondsChallenge.ui.home.HomeFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_friends.rvFriends
import kotlinx.android.synthetic.main.fragment_shopping_cart.rv_ItemCart
import javax.inject.Inject

@AndroidEntryPoint
class FriendsFragment : BaseBindingFragment<FragmentFriendsBinding>(),
    OnItemClickListener<FriendModel> {

    override fun getLayoutId(): Int = R.layout.fragment_friends

    private val viewModel by activityViewModels<FriendViewModel>()

    companion object {
        const val LEADER_BOARD_ID = 11
        const val CHAT_ID = 22
        const val FRIENDS_ID = 33
        const val CLUBS_ID = 44
        const val LOYALTY_ID = 55
        const val PLAYERS_ID = 66
    }

    @Inject
    lateinit var friendAdapter: FriendAdapter

    @Inject
    lateinit var userPref: UserPref

    var animation: LayoutAnimationController? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)

        setUpData()
        setUpViewsListeners()
        setRecycleView()

    }


    private fun setUpData() {

    }

    private fun setUpViewsListeners() {

    }

    private fun setRecycleView() {
        requireContext().let {
            friendAdapter = FriendAdapter()
            rvFriends.adapter = friendAdapter
            friendAdapter.onItemClickListener = this
            friendAdapter.submitItems(
                listOf(
                    FriendModel(
                        LEADER_BOARD_ID,
                        R.drawable.ic_statistic,
                        "المتصدرون"
                    ),
//                    FriendModel(
//                        CHAT_ID,
//                        R.drawable.ic_chat,
//                        "الدردشة"
//                    ),
                    FriendModel(
                        FRIENDS_ID,
                        R.drawable.ic_friend,
                        "الأصدقاء"
                    ),
//                    FriendModel(
//                        CLUBS_ID,
//                        R.drawable.ic_club,
//                        "الأندية"
//                    ),
                    FriendModel(
                        PLAYERS_ID,
                        R.drawable.ic_users_icon,
                        "اللاعبون"
                    ),
                    FriendModel(
                        LOYALTY_ID,
                        R.drawable.ic_loaylty,
                        "نظام الولاء"
                    ),
                )
            )

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onItemClicked(view: View?, item: FriendModel, position: Int) {
        when (item.id) {

            LEADER_BOARD_ID -> {
                if (userPref.getToken().isNullOrEmpty()) {
                    val authFragment = AuthFragment()
                    authFragment.show(parentFragmentManager, "auth Fragment")
                } else {
                    findNavController().navigate(FriendsFragmentDirections.actionGlobalLeaderBoardsFragment())
                }
            }

            CHAT_ID -> {

            }

            FRIENDS_ID -> {
                if (userPref.getToken().isNullOrEmpty()) {
                    val authFragment = AuthFragment()
                    authFragment.show(parentFragmentManager, "auth Fragment")
                } else {
                    findNavController().navigate(FriendsFragmentDirections.actionFriendsFragmentToFriendsGroupFragment())
                }
            }

            CLUBS_ID -> {

            }

            PLAYERS_ID -> {
                if (userPref.getToken().isNullOrEmpty()) {
                    val authFragment = AuthFragment()
                    authFragment.show(parentFragmentManager, "auth Fragment")
                } else {
                    findNavController().navigate(FriendsFragmentDirections.actionFriendsFragmentToSearchPlayersFragment())
                }
            }

            LOYALTY_ID -> {
                if (userPref.getToken().isNullOrEmpty()) {
                    val authFragment = AuthFragment()
                    authFragment.show(parentFragmentManager, "auth Fragment")
                } else {
                    findNavController().navigate(FriendsFragmentDirections.actionFriendsFragmentToLoyaltyFragment())
                }
            }

        }

    }

}