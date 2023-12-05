package com.dominate.thirtySecondsChallenge.ui.splash

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.NotificationManager
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import butterknife.ButterKnife
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.activity.BaseBindingActivity
import com.dominate.thirtySecondsChallenge.base.fragment.BaseBindingFragment
import com.dominate.thirtySecondsChallenge.common.CommonEnums
import com.dominate.thirtySecondsChallenge.databinding.FragmentSplashBinding
import com.dominate.thirtySecondsChallenge.ui.main.MainActivity
import com.dominate.thirtySecondsChallenge.ui.sound.AudioPlayer
import com.dominate.thirtySecondsChallenge.utils.extensions.longToast
import com.dominate.thirtySecondsChallenge.utils.extensions.onClick
import com.dominate.thirtySecondsChallenge.utils.pref.PrefConstants
import com.dominate.thirtySecondsChallenge.utils.pref.SharedPreferencesUtil
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_splash.*

@AndroidEntryPoint
class SplashFragment : BaseBindingFragment<FragmentSplashBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_splash

    companion object {
        const val LANGUAGE_EN = "en"
        const val LANGUAGE_AR = "ar"
        const val LANGUAGE_EN_ID = 1
        const val LANGUAGE_AR_ID = 2
    }

    var selectedLanguage: CommonEnums.Languages? = null

    private val viewModel by activityViewModels<SplashViewModel>()

    lateinit var audioPlayer: AudioPlayer

    var handler = Handler()
    var isOnBoarding = false
    var onReOpen = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)

        setUpData()
        setUpViewsListeners()
        getTokenFirebase()
    }


    private fun setUpData() {
        binding?.viewModel = viewModel

        audioPlayer = AudioPlayer(requireContext(), R.raw.splash_t30)

//        showNotificationPermissionDialog(requireContext())

        isOnBoarding =
            SharedPreferencesUtil.getInstance(requireContext()).getBooleanPreferences(
                PrefConstants.IS_ONBOARDING, true
            )
        if (!isOnBoarding) {
            viewModel.isVisible.value = false
        }

        selectedLanguage = CommonEnums.Languages.getLanguageByValue(
            SharedPreferencesUtil.getInstance(requireContext()).getStringPreferences(
                PrefConstants.APP_LANGUAGE_VALUE,
                CommonEnums.Languages.English.value
            )
        )

        when (selectedLanguage?.value ?: LANGUAGE_EN) {
            LANGUAGE_EN -> {
                viewModel.languageName.value = LANGUAGE_EN_ID
                updateLanguage(
                    CommonEnums.Languages.Arabic,
                    CommonEnums.Languages.Arabic.value
                )
            }

            LANGUAGE_AR -> {
                viewModel.languageName.value = LANGUAGE_AR_ID
            }
        }

        if (viewModel.hasStartedSoundPlay.value != true) {
            lifecycleScope.launchWhenStarted {
                audioPlayer.stopPlayback()
                audioPlayer.startPlayback()
                viewModel.hasStartedSoundPlay.value = true
            }
        }

    }

    fun startHandler() {
        handler.postDelayed(myRunnable, 4000)
    }
    private val myRunnable = Runnable {
        if (isOnBoarding) {
            try {
                goToNextPage()  // remove this when add en language
                viewModel.isVisible.value = true
            } catch (e: Exception) {
                Log.i("error", e.message.toString())
            }

        } else {
            MainActivity().setStartDestination(
                requireActivity(),
                R.id.mainOperationFragment
            )
        }
    }

    private fun goToNextPage() {
        viewModel.hasStartedSoundPlay.value = false
        findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToOnBoardingStepTwoFragment())  // // remove this when add en language
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setUpViewsListeners() {

        tv_English.onClick {
            when (selectedLanguage?.value ?: "en") {
                "en" -> {
                }

                "ar" -> {
                    updateLanguage(
                        CommonEnums.Languages.English,
                        CommonEnums.Languages.English.value
                    )
                    viewModel.languageName.value = LANGUAGE_EN_ID
                }
            }
        }

        tv_Arabic.onClick {
            when (selectedLanguage?.value ?: "en") {
                "en" -> {
                    updateLanguage(
                        CommonEnums.Languages.Arabic,
                        CommonEnums.Languages.Arabic.value
                    )
                    viewModel.languageName.value = LANGUAGE_AR_ID
                }

                "ar" -> {

                }
            }
        }

        btn_Next.onClick {
            if (selectedLanguage?.value == "en") {
                longToast("Select Arabic Language!")
            } else {
                findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToOnBoardingStepTwoFragment())
            }
        }

    }

    private fun updateLanguage(selectedLanguage: CommonEnums.Languages, flag: String) {
        viewModel.isVisible.value = false
        SharedPreferencesUtil.getInstance(requireContext()).setStringPreferences(
            PrefConstants.APP_LANGUAGE_VALUE, flag
        )
        requireActivity().let {
            (it as BaseBindingActivity<*>).setLanguage(selectedLanguage.value)
        }

    }

    private fun playSoundSplash() {

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

//    fun checkNotificationPermission(): Boolean {
//        val notificationManager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        return notificationManager.areNotificationsEnabled()
//    }

    fun requestNotificationPermission(context: Context) {
        if (!isNotificationPermissionGranted(context)) {
            // If permission is not granted, prompt the user to grant permission
            val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
            context.startActivity(intent)
        }
    }

    fun isNotificationPermissionGranted(context: Context): Boolean {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        return notificationManager.areNotificationsEnabled()
    }

    fun showNotificationPermissionDialog(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.apply {
            setTitle("Allow Notifications")
            setMessage("This app requires access to notifications to provide timely updates.")
            setPositiveButton("Allow") { _, _ ->
                requestNotificationPermission(context)
            }
            setNegativeButton("Deny") { dialog, _ ->
                dialog.dismiss()
                // Handle denial if needed
            }
        }
        val dialog = builder.create()
        dialog.show()
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(myRunnable)
        onReOpen = true
    }

    override fun onResume() {
        super.onResume()
        if (onReOpen) {
            try {
                if (isOnBoarding) {
                    goToNextPage()  // remove this when add en language
                    viewModel.isVisible.value = true
                } else {
                    MainActivity().setStartDestination(
                        requireActivity(),
                        R.id.mainOperationFragment
                    ) // remove this when add en language
                }
            } catch (e: Exception) {
                Log.i("error", e.message.toString())
            }
        }else{
            startHandler()
        }
    }

    private fun getTokenFirebase(){
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(ContentValues.TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result
            viewModel.tokenFireBase.value = task.result
            viewModel.addTokenFirebaseResult()
            Log.i("firebaseToken",token)
        })
    }

}