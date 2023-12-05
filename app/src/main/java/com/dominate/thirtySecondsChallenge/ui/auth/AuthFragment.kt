package com.dominate.thirtySecondsChallenge.ui.auth

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import butterknife.ButterKnife
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.buttomsheet.BaseValidationBottomSheetFragment
import com.dominate.thirtySecondsChallenge.data.model.user.UserDetailsResponseModel
import com.dominate.thirtySecondsChallenge.data.response.Result
import com.dominate.thirtySecondsChallenge.databinding.FragmentAuthBinding
import com.dominate.thirtySecondsChallenge.ui.home.HomeViewModel
import com.dominate.thirtySecondsChallenge.utils.CustomProgressBar
import com.dominate.thirtySecondsChallenge.utils.HandleRequestFailedUtil
import com.dominate.thirtySecondsChallenge.utils.extensions.longToast
import com.dominate.thirtySecondsChallenge.utils.extensions.onClick
import com.dominate.thirtySecondsChallenge.utils.extensions.shortToast
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.android.synthetic.main.fragment_auth.cv_facebook
import kotlinx.android.synthetic.main.fragment_auth.cv_gmail
import kotlinx.android.synthetic.main.fragment_auth.iv_close
import java.net.MalformedURLException

@AndroidEntryPoint
class AuthFragment() : BaseValidationBottomSheetFragment<FragmentAuthBinding>() {

    override val layoutId: Int = R.layout.fragment_auth

    companion object {
        const val message = "message"

        fun newInstance(argValue: String): AuthFragment {
            val fragment = AuthFragment()
            val args = Bundle()
            args.putString(message, argValue)
            fragment.arguments = args
            return fragment
        }
    }

    private val viewModel by activityViewModels<AuthViewModel>()
    private val homeViewModel by activityViewModels<HomeViewModel>()

    private val REQ_ONE_TAP = 2222

    //    lateinit var auth : Firebase
    var facebookLoginManager: LoginManager? = null
    var callBackManager: CallbackManager? = null

    private var mGoogleSignInClient: GoogleSignInClient? = null
    private lateinit var gso: GoogleSignInOptions

    private lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpData()
        setUpViewsListeners()

    }

    private fun setUpData() {
        binding?.viewModel = viewModel

        // gmail
        if (isGooglePlayServicesAvailable(requireContext())) {
            gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()

            mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        }

        FacebookSdk.sdkInitialize(requireContext())
        AppEventsLogger.activateApp(Application())
    }

    fun isGooglePlayServicesAvailable(context: Context): Boolean {
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = googleApiAvailability.isGooglePlayServicesAvailable(context)
        return resultCode == ConnectionResult.SUCCESS
    }

    private fun setUpViewsListeners() {

        iv_close.onClick {
            dismiss()
        }

        cv_gmail.onClick {
            mGoogleSignInClient?.signOut()
            signInGoogleAccount()
        }

        cv_facebook.onClick {
            setUpSocialMediaData()
        }

    }

    private fun signInFacebookAccount() {
        var permissions = ArrayList<String>()
        permissions.add("email")
        facebookLoginManager?.logInWithReadPermissions(this@AuthFragment, callBackManager!!, permissions)
    }

    private fun setUpSocialMediaData() {

        facebookLoginManager = LoginManager.getInstance()
        callBackManager = CallbackManager.Factory.create()
        facebookLoginManager?.registerCallback(callBackManager, fbCallbackManager)
        signInFacebookAccount()

    }

    private fun authWithFacebook() {

        // val currentUser = auth.currentUser // check user login facebook
//        auth.auth

    }

    private var fbCallbackManager: FacebookCallback<LoginResult> = object :
        FacebookCallback<LoginResult> {
        override fun onSuccess(result: LoginResult) {
            try {
                val accessToken = result.accessToken
                shortToast(accessToken.toString())
                Log.i("token", accessToken.token.toString())
                Log.i("userId", accessToken.userId.toString())
                Log.i("applicationId", accessToken.applicationId.toString())
                handleFacebookAccessToken(accessToken)

            } catch (e: MalformedURLException) {
                Log.i("error",e.message.toString())
            }
        }

        override fun onCancel() {
            shortToast("onCancel")
        }

        override fun onError(error: FacebookException) {
            shortToast("onCancel \n ${error.message.toString()}")
        }
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI
                    val user = FirebaseAuth.getInstance().currentUser
                } else {
                    longToast("Authentication failed.")
                }
            }
    }

    private fun signInGoogleAccount() {
        val signInIntent = mGoogleSignInClient?.signInIntent
        startActivityForResult(signInIntent, REQ_ONE_TAP)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callBackManager?.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_ONE_TAP) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }

    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            viewModel.socialMediaId.value = account.id.toString()
            viewModel.firstName.value = account.givenName.toString()
            viewModel.lastName.value = account.familyName.toString()
            viewModel.email.value = account.email.toString()

            Log.i("account.id",account.id.toString())
            Log.i("account.account",account.account.toString())
            Log.i("account.idToken",account.email.toString())
            Log.i("account.photoUrl",account.photoUrl.toString())

            viewModel.registerApi().observe(viewLifecycleOwner,registerApi)

        } catch (e: ApiException) {
            Log.i("error",e.toString())
        }
    }

    private val registerApi = Observer<Result<UserDetailsResponseModel>> {
        when (it) {
            is Result.Success -> {
                CustomProgressBar.hide(requireContext())
                homeViewModel.getHome()
                dismiss()
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

    override fun onValidationSucceeded() {

    }


}