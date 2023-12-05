package com.dominate.thirtySecondsChallenge.ui.friends.loyalty

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import butterknife.ButterKnife
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.fragment.BaseBindingFragment
import com.dominate.thirtySecondsChallenge.data.model.friends.ReferralResponseModel
import com.dominate.thirtySecondsChallenge.data.response.Result
import com.dominate.thirtySecondsChallenge.databinding.FragmentLoyaltyBinding
import com.dominate.thirtySecondsChallenge.ui.friends.FriendViewModel
import com.dominate.thirtySecondsChallenge.utils.CustomProgressBar
import com.dominate.thirtySecondsChallenge.utils.HandleRequestFailedUtil
import com.dominate.thirtySecondsChallenge.utils.extensions.copyText
import com.dominate.thirtySecondsChallenge.utils.extensions.onClick
import com.dominate.thirtySecondsChallenge.utils.extensions.openShareView
import com.dominate.thirtySecondsChallenge.utils.extensions.vibrate
import com.google.firebase.FirebaseApp
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_loyalty.*

@AndroidEntryPoint
class LoyaltyFragment : BaseBindingFragment<FragmentLoyaltyBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_loyalty

    private val viewModel by activityViewModels<FriendViewModel>()

    var refCode = ""
    var referralLink = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)

        setUpData()
        setUpViewsListeners()
        FirebaseApp.initializeApp(requireContext())

    }

    private fun setUpData() {
        binding?.viewModel = viewModel
        viewModel.getReferral().observe(viewLifecycleOwner, getReferral)
    }

    private fun setUpViewsListeners() {

        iv_back.onClick {
            findNavController().popBackStack()
        }

        iv_copyLink.onClick {
            vibrate(requireContext())
            generateShortDynamicLink(refCode) { shortLink ->
                referralLink = "$shortLink"
                requireContext().copyText(
                    requireView(),
                    "$shortLink"
                )
            }

        }

        btn_shareLink.onClick {
            vibrate(requireContext())
            generateShortDynamicLink(refCode) { shortLink ->
                requireContext().openShareView(
                    "$shortLink"
                )
            }
        }

    }

    private val getReferral = Observer<Result<ReferralResponseModel>> {
        when (it) {
            is Result.Success -> {
                CustomProgressBar.hide(requireContext())
                if (it.data != null)
                    refCode = it.data.referralCode.toString()
                viewModel.referralCode.value = it.data?.referralCode.toString()
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


    fun generateShortDynamicLink(
        referralCode: String,
        callback: (String) -> Unit
    ) {
        val deepLink = "https://www.ta7adi30second.com/?referralCode=$referralCode"

        val dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
            .setLink(Uri.parse(deepLink))
            .setDomainUriPrefix("https://thirtysecondschallenge.page.link")
            .setAndroidParameters(
                DynamicLink.AndroidParameters.Builder("com.dominate.thirtySecondsChallenge")
                    .build()
            )
            .setIosParameters(
                DynamicLink.IosParameters.Builder("dominate.dev.Tahady-30")
                    .build()
            )
            .buildShortDynamicLink()

        dynamicLink.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val shortLink = task.result?.shortLink.toString()
                callback(shortLink)
            } else {
                val exception = task.exception
                if (exception != null) {
                    Log.e("Dynamic Link Error", "Failed to create short link: ${exception.message}")
                }
                callback("")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

}