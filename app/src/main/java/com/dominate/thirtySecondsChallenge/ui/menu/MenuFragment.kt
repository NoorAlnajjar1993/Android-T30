package com.dominate.thirtySecondsChallenge.ui.menu

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import butterknife.ButterKnife
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.fragment.BaseBindingFragment
import com.dominate.thirtySecondsChallenge.data.model.menu.CategoryGetMainResponseModel
import com.dominate.thirtySecondsChallenge.data.pref.user.UserPref
import com.dominate.thirtySecondsChallenge.data.response.Result
import com.dominate.thirtySecondsChallenge.databinding.FragmentMenuBinding
import com.dominate.thirtySecondsChallenge.ui.auth.AuthFragment
import com.dominate.thirtySecondsChallenge.ui.friends.FriendsFragmentDirections
import com.dominate.thirtySecondsChallenge.ui.menu.adapter.AllGameAdapter
import com.dominate.thirtySecondsChallenge.ui.menu.adapter.FavoriteGameAdapter
import com.dominate.thirtySecondsChallenge.utils.CustomProgressBar
import com.dominate.thirtySecondsChallenge.utils.HandleRequestFailedUtil
import com.dominate.thirtySecondsChallenge.utils.anim.Shaking
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.bnv_main
import kotlinx.android.synthetic.main.activity_main.cl_general
import kotlinx.android.synthetic.main.activity_main.iv_CoinPlus
import kotlinx.android.synthetic.main.activity_main.iv_diamondPlus
import kotlinx.android.synthetic.main.activity_main.iv_iconRate
import kotlinx.android.synthetic.main.fragment_menu.*
import javax.inject.Inject

@AndroidEntryPoint
class MenuFragment : BaseBindingFragment<FragmentMenuBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_menu

    @Inject
    lateinit var allGameAdapter: AllGameAdapter

    @Inject
    lateinit var favoriteGameAdapter: FavoriteGameAdapter

    @Inject
    lateinit var userPref: UserPref

    private val viewModel by activityViewModels<MenuViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)

        setUpData()
        setUpViewsListeners()
        setRecycleView()

    }


    private fun setUpData() {

        if (userPref.getToken().isNullOrEmpty()) {
            val authFragment = AuthFragment()
            authFragment.show(parentFragmentManager, "auth Fragment")
        } else {
            viewModel.getCategoryGetMain().observe(viewLifecycleOwner, getCategoryGetMain)
        }

    }

    private fun setUpViewsListeners() {

    }

    private fun setRecycleView() {
        requireContext().let {
            allGameAdapter = AllGameAdapter()
            rvAllGame.adapter = allGameAdapter

        }

//        requireContext().let {
//            favoriteGameAdapter = FavoriteGameAdapter()
//            rvFavorite.adapter = favoriteGameAdapter
//            favoriteGameAdapter.submitItems(
//                listOf(
//                    AllGameModel(
//                        itemName = "رياضة",
//                        image = R.drawable.ic_test_game,
//                        isFav = true
//                    ),
//
//                    AllGameModel(
//                        itemName = "معلومات عامة",
//                        image = R.drawable.ic_test_game,
//                        isFav = true
//                    ),
//                )
//            )
//        }

    }

    private val getCategoryGetMain = Observer<Result<List<CategoryGetMainResponseModel>>> {
        when (it) {
            is Result.Success -> {
                CustomProgressBar.hide(requireContext())
                viewModel._categoryListLiveData.value = it.data?: arrayListOf()
                allGameAdapter.submitItems(it.data?: arrayListOf())

            }

            is Result.Error -> {
                CustomProgressBar.hide(requireContext())
                handleError(it.throwable)

                viewModel.categoryListLiveData.observe(
                    viewLifecycleOwner
                ) { items ->
                    allGameAdapter.submitItems(items)
                }
            }

            is Result.CustomError -> {
                CustomProgressBar.hide(requireContext())
                HandleRequestFailedUtil.showDialogMessage(
                    it.message, requireContext(), childFragmentManager
                )

                viewModel.categoryListLiveData.observe(
                    viewLifecycleOwner
                ) { items ->
                    allGameAdapter.submitItems(items)
                }
            }

            is Result.Loading -> {
                if (viewModel.categoryListLiveData.value.isNullOrEmpty()) {
                    CustomProgressBar.show(requireContext())
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

}