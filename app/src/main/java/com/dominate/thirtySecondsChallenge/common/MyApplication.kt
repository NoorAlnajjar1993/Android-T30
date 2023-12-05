package com.dominate.thirtySecondsChallenge.common

import android.annotation.SuppressLint
import android.app.Application
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.common.validation.EmptyOrLength
import com.dominate.thirtySecondsChallenge.data.common.NetworkConstants.HUB_URL_RELEASE
import com.dominate.thirtySecondsChallenge.data.notificationhelper.NotificationHelper
import com.dominate.thirtySecondsChallenge.data.pref.user.UserPref
import com.dominate.thirtySecondsChallenge.data.signalR.friendsrequest.JsonDataFriendsRequestResponse
import com.dominate.thirtySecondsChallenge.data.signalR.friendsrequest.JsonResponseError
import com.dominate.thirtySecondsChallenge.data.signalR.general.ArgumentsResponse
import com.dominate.thirtySecondsChallenge.data.signalR.general.MessageGame
import com.dominate.thirtySecondsChallenge.data.signalR.general.MessageGame.SEND_FRIEND_REQUEST
import com.dominate.thirtySecondsChallenge.data.signalR.general.MessageGame.SEND_GIFT
import com.dominate.thirtySecondsChallenge.ui.sound.MusicPlayer
import com.dominate.thirtySecondsChallenge.utils.SnackbarListener
import com.dominate.thirtySecondsChallenge.utils.extensions.showSnackbarFragment
import com.dominate.thirtySecondsChallenge.utils.pref.PrefConstants.NEW_MESSAGE_
import com.google.gson.Gson
import com.mobsandgeeks.saripaar.Validator
import com.smartarmenia.dotnetcoresignalrclientjava.HubConnectionListener
import com.smartarmenia.dotnetcoresignalrclientjava.HubEventListener
import com.smartarmenia.dotnetcoresignalrclientjava.HubMessage
import com.smartarmenia.dotnetcoresignalrclientjava.WebSocketHubConnectionP2
import dagger.hilt.android.HiltAndroidApp
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject


@HiltAndroidApp
class MyApplication : Application(), HubConnectionListener,
    HubEventListener, LifecycleObserver {

    @Inject
    lateinit var userPref: UserPref

    var connection = WebSocketHubConnectionP2(HUB_URL_RELEASE, null)

    private val maxRetryDelayMs = 60000 // Maximum retry delay in milliseconds
    private var currentRetryDelayMs = 1000 // Initial retry delay in milliseconds

    private lateinit var notificationHelper: NotificationHelper

    var isConnected = false

    var isAppBackcground = false

    lateinit var musicPlayer: MusicPlayer

    val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    lateinit var snackbarListener: SnackbarListener

    override fun onCreate() {
        super.onCreate()
        Validator.registerAnnotation(EmptyOrLength::class.java)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)

        notificationHelper = NotificationHelper(this)
        connectToHub()

    }

    fun connectToHub() {
        connection = WebSocketHubConnectionP2(
            HUB_URL_RELEASE,
            "bearer ${userPref.getToken()}"
        )
        try {
            if (userPref.getToken().isNotEmpty()) {
                if (!isConnected) {
                    Log.i("webSocketClient", "not open")
                    try {
                        connection.connect()
                        connection.addListener(this@MyApplication)
                        connection.subscribeToEvent(NEW_MESSAGE_, this)

                    } catch (e: java.lang.Exception) {
                        Log.i("errorConnect", e.message.toString())
                    }
                } else {
                    Log.i("webSocketClient", "isOpen")
                }
            }
        } catch (e: java.lang.Exception) {
            handleConnectionError(e)
        }
    }

    private fun handleConnectionError(exception: Exception) {
        if (currentRetryDelayMs < maxRetryDelayMs) {
            Handler(Looper.getMainLooper()).postDelayed({
                currentRetryDelayMs *= 2
                connectToHub()
            }, currentRetryDelayMs.toLong())
        }
        Log.e("ConnectionError", exception.message, exception)
    }

    override fun onConnected() {
        isConnected = true
        Log.i("onConnected", "isConnected")
    }

    override fun onDisconnected() {
        isConnected = false
        Log.i("onDisconnected", "isDisconnected")
//        connectToHub()
    }

    override fun onError(exception: java.lang.Exception?) {
        Log.i("onError", exception?.message.toString())
    }

    override fun onEventMessage(message: HubMessage?) {
        Log.i("onEventMessage", message.toString())
    }

    override fun onMessage(message: HubMessage?) {
        Log.i("onMessageMyApplication", "${message?.target}\n${Gson().toJson(message?.arguments)}")
        val argumentsResponse = Gson().toJson(message?.arguments)
        val responseArguments = json.decodeFromString<List<ArgumentsResponse>>(argumentsResponse)

        when (responseArguments[0].messageType) {

            SEND_FRIEND_REQUEST, SEND_GIFT -> {
                val dataJson =
                    json.decodeFromString<JsonDataFriendsRequestResponse>(responseArguments[0].jsonData)

                if (isAppBackcground) {
//                    notificationHelper.showNotification(dataJson.Message.toString())
                } else {
                    snackbarListener.showSnackbar(
                        dataJson.Message.toString(),
                        R.drawable.snackbar_success
                    )
                }
            }

            MessageGame.ERROR_HUB -> {
                val dataJson =
                    json.decodeFromString<JsonResponseError>(responseArguments[0].jsonData)
                if (responseArguments[0].messageType == 0) {
                    snackbarListener.showSnackbar(
                        dataJson.Message,
                        R.drawable.snackbar_error
                    )
                }
            }

            else -> {
            }
        }
    }

    fun statusConnection(): Boolean {
        return isConnected
    }

    // background connection
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onAppBackgrounded() {
        Log.i("onAppBackgrounded", "onAppBackgrounded")
        isAppBackcground = true
        musicPlayer = MusicPlayer.getInstance(this, R.raw.music_running_t30)
        musicPlayer.stopPlayback()
    }


    @SuppressLint("NewApi", "UseRequireInsteadOfGet")
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onAppForegrounded() {
        Log.i("onAppForegrounded", "onAppForegrounded")
        isAppBackcground = false
    }

}
