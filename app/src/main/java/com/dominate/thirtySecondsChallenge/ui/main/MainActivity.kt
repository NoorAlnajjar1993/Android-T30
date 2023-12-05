package com.dominate.thirtySecondsChallenge.ui.main

import android.graphics.Color
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.dominate.thirtySecondsChallenge.MainOperationFragmentDirections
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.activity.BaseBindingActivity
import com.dominate.thirtySecondsChallenge.common.MyApplication
import com.dominate.thirtySecondsChallenge.data.model.user.UserDetailsResponseModel
import com.dominate.thirtySecondsChallenge.data.pref.user.UserPref
import com.dominate.thirtySecondsChallenge.data.response.Result
import com.dominate.thirtySecondsChallenge.data.signalR.friendsrequest.JsonDataFriendsRequestResponse
import com.dominate.thirtySecondsChallenge.data.signalR.general.ArgumentsResponse
import com.dominate.thirtySecondsChallenge.data.signalR.general.MessageGame
import com.dominate.thirtySecondsChallenge.databinding.ActivityMainBinding
import com.dominate.thirtySecondsChallenge.ui.auth.AuthFragment
import com.dominate.thirtySecondsChallenge.ui.auth.AuthViewModel
import com.dominate.thirtySecondsChallenge.ui.endgame.wingame.WinGameFragment
import com.dominate.thirtySecondsChallenge.ui.levelup.LevelUpFragment
import com.dominate.thirtySecondsChallenge.ui.profile.ProfileFragmentDirections
import com.dominate.thirtySecondsChallenge.ui.shoppingcart.ShoppingCartFragment
import com.dominate.thirtySecondsChallenge.utils.SnackbarListener
import com.dominate.thirtySecondsChallenge.utils.anim.Shaking
import com.dominate.thirtySecondsChallenge.utils.extensions.onClick
import com.dominate.thirtySecondsChallenge.utils.extensions.shortToast
import com.dominate.thirtySecondsChallenge.utils.extensions.showSnackbar
import com.dominate.thirtySecondsChallenge.utils.extensions.showSnackbarActivity
import com.dominate.thirtySecondsChallenge.utils.pref.PrefConstants.SEND_FRIEND_REQUEST_
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.smartarmenia.dotnetcoresignalrclientjava.HubConnectionListener
import com.smartarmenia.dotnetcoresignalrclientjava.HubEventListener
import com.smartarmenia.dotnetcoresignalrclientjava.HubMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : BaseBindingActivity<ActivityMainBinding>(),
    HubConnectionListener,
    HubEventListener, SnackbarListener {

    private lateinit var bindings: ActivityMainBinding

    lateinit var navHostFragment: NavHostFragment
    lateinit var navController: NavController
    lateinit var graph: NavGraph

    private val viewModel: MainViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()

    var view: View? = null

    @Inject
    lateinit var userPref: UserPref

    var myApplication: MyApplication? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindings = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindings.root)

        view = bindings.root
        setData()
        setTransparentStatusBar()
        setNavigationView()
        setOnClick()
        setNavigation()
        bindings.lifecycleOwner = this

    }

    private fun setData() {
        bindings.viewModel = viewModel
        bindings.authViewModel = authViewModel

        myApplication = (this.application as MyApplication)
        myApplication?.connection?.addListener(this@MainActivity)
        myApplication?.snackbarListener = this

    }

    private fun setTransparentStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // For Lollipop and above versions
            window.statusBarColor = Color.TRANSPARENT
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // For KitKat to Lollipop
            window.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            )
        }
    }

    private fun setNavigationView() {
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.onBoardingNavContainer) as NavHostFragment
        navController = navHostFragment.navController
        val inflater = navHostFragment.navController.navInflater
        graph = inflater.inflate(R.navigation.onboarding_nav_graph)
        navController.graph = graph

    }

    private fun setOnClick() {

        cl_levels.onClick {
//            val winGameFragment = WinGameFragment()
//            winGameFragment.show(supportFragmentManager, "win Game Fragment")

//            navController.navigate(R.id.action_from_included_graph)
            val levelUpFragment = LevelUpFragment()
            levelUpFragment.show(supportFragmentManager, "Level Up Fragment")
        }

        iv_notification.onClick {
            if (userPref.getToken().isNullOrEmpty()) {
                val authFragment = AuthFragment()
                authFragment.show(supportFragmentManager, "auth Fragment")
            } else {
                navController.navigate(MainOperationFragmentDirections.actionGlobalNotificationFragment())
            }
        }

        iv_Profile.onClick {
            if (userPref.getToken().isNullOrEmpty()) {
                val authFragment = AuthFragment()
                authFragment.show(supportFragmentManager, "auth Fragment")
            } else {
                navController.navigate(ProfileFragmentDirections.actionGlobalMainProfileFragment())
            }
        }

        iv_paper.onClick {
            navController.navigate(
                ProfileFragmentDirections.actionGlobalWebViewFragment(
                    "title",
                    "https://www.google.com/"
                )
            )
        }

        iv_diamondPlus.onClick {
            navController.navigate(
                MainOperationFragmentDirections.actionGlobalShoppingCartFragment()
                    .setTypeScreen(ShoppingCartFragment.DIAMONDS_ID)
            )
        }

        iv_CoinPlus.onClick {
            navController.navigate(
                MainOperationFragmentDirections.actionGlobalShoppingCartFragment()
                    .setTypeScreen(ShoppingCartFragment.CURRENCIES_ID)
            )
        }

    }

    private fun setNavigation() {

        bnv_main.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.splashFragment -> {

                }

                R.id.homeFragment -> {

                }

                R.id.shoppingCartFragment -> {

                }

                else -> {

                }
            }

            if (destination.id == R.id.homeFragment ||
                destination.id == R.id.friendsFragment ||
                destination.id == R.id.menuFragment ||
                destination.id == R.id.shoppingCartFragment ||
                destination.id == R.id.createGameGroupFragment) {
                Handler(Looper.getMainLooper()).postDelayed({
                    cl_general.visibility = View.VISIBLE
                }, 250)
                bnv_main.visibility = View.VISIBLE
                Shaking(iv_iconRate)
                Shaking(iv_CoinPlus)
                Shaking(iv_diamondPlus)
                try {
                    viewModel.loginApi().observe(this, loginApi)
                } catch (e: Exception) {
                    Log.i("error", e.message.toString())
                }
            } else {
                cl_general.visibility = View.GONE
                bnv_main.visibility = View.GONE
            }

        }

    }

    fun setStartDestination(activity: FragmentActivity, fragment: Int) {
        navController = activity.findNavController(R.id.onBoardingNavContainer)
        val navGraph = navController.navInflater.inflate(R.navigation.onboarding_nav_graph)
        navGraph.startDestination = fragment
        navController.graph = navGraph

    }

    private val loginApi = Observer<Result<UserDetailsResponseModel>> {
        when (it) {
            is Result.Success -> {
                it.data?.let {
                    userPref.setToken(it.token)
                    userPref.setUser(it)
                    authViewModel.levelXpResult.value =
                        "${it.nextLevelXP.toString()} / ${it.xp.toString()}"
                    authViewModel.xp.value = it.xp.toString()
                    authViewModel.nextXp.value = it.nextLevelXP.toString()
                    authViewModel.coins.value = it.coins.toString()
                    authViewModel.diamonds.value = it.diamonds.toString()
                    authViewModel.levelUpCount.value = it.level

                    // when count 0 or count < 1 this code hide notification or else it show
                    if (it.unReadNotifications < 1){
                        authViewModel.countNotification.value = null
                    }else if(it.unReadNotifications > 99){
                        authViewModel.countNotification.value = "99"
                    }else{
                        authViewModel.countNotification.value = it.unReadNotifications.toString()
                    }

                }

            }

            is Result.Error -> {
                handleError(it.throwable)
            }

            is Result.CustomError -> {
                authViewModel.xp.value = "0"
                authViewModel.coins.value = "0"
                authViewModel.diamonds.value = "0"
                authViewModel.nextXp.value = "0"
                authViewModel.levelXpResult.value = "0/0"
                authViewModel.levelUpCount.value = "0"

//                HandleRequestFailedUtil.showDialogMessage(
//                    it.message, this, supportFragmentManager
//                )
            }

            is Result.Loading -> {
            }
        }
    }

    override fun onConnected() {
        Log.i("onConnectedHubMainActivity", "onConnected")
    }

    override fun onDisconnected() {
        Log.i("onDisconnectedHubMainActivity", "onDisconnected")
    }

    override fun onMessage(message: HubMessage?) {
        try {
            val json = Json {
                ignoreUnknownKeys = true
                isLenient = true
            }
            val argumentsResponse = Gson().toJson(message?.arguments)
            val responseArguments = json.decodeFromString<List<ArgumentsResponse>>(argumentsResponse)
            when (responseArguments[0].messageType) {
                MessageGame.SEND_FRIEND_REQUEST, MessageGame.SEND_GIFT -> {
                    lifecycleScope.launchWhenStarted {
                    val dataJson =
                        json.decodeFromString<JsonDataFriendsRequestResponse>(responseArguments[0].jsonData)

                    // when count 0 or count < 1 this code hide notification or else it show
                    if (dataJson.NotificationCount?:0 < 1){
                        authViewModel.countNotification.value = null
                    }else if(dataJson.NotificationCount?:0 > 99){
                        authViewModel.countNotification.value = "99"
                    }else{
                        authViewModel.countNotification.value = dataJson.NotificationCount?.toString()
                    }
//                    showSnackbarActivity(dataJson.Message.toString(),R.drawable.snackbar_success)
                    }

                }

                else -> {
//                    this.runOnUiThread(Runnable {
//                        shortToast(message?.target.toString())
//                    })
                }

            }
        } catch (e: Exception) {
            Log.i("ExceptionSerializationMain", e.message.toString())
        }

    }

    override fun onError(exception: java.lang.Exception?) {
        Log.i("errorHubMainActivity", exception?.message.toString())
    }

    override fun onEventMessage(message: HubMessage?) {
        Log.i("onEventMessageHubMainActivity", message.toString())
    }

    override fun showSnackbar(message: String, iconResId: Int) {
        showSnackbarActivity(message,iconResId)
    }

//    override fun onDestroy() {
//        MediaPlayer().release()
//        super.onDestroy()
//    }

}