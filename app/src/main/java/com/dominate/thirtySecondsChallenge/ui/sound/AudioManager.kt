package com.dominate.thirtySecondsChallenge.ui.sound
import android.content.Context
import android.media.MediaPlayer
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.utils.pref.PrefConstants
import com.dominate.thirtySecondsChallenge.utils.pref.SharedPreferencesUtil

class AudioManager private constructor(context: Context , sound:Int) {

    private val mediaPlayer: MediaPlayer = MediaPlayer.create(context, sound)
    private val isSoundEnabled = SharedPreferencesUtil.getInstance(context)
        .getBooleanPreferences(PrefConstants.APP_IS_SOUND, true)

    companion object {
        @Volatile
        private var instance: AudioManager? = null

        fun getInstance(context: Context , sound:Int? = null): AudioManager {
            return instance ?: synchronized(this) {
                instance ?: AudioManager(context,sound?: R.raw.sound_shift).also { instance = it }
            }
        }
    }

    fun startPlayback() {
        if (isSoundEnabled) {
            mediaPlayer.apply {
                start()
            }
        }
    }

    fun startPlaybackLoop() {
        if (isSoundEnabled) {
            mediaPlayer.apply {
                isLooping = true
                start()
            }
        }
    }

    fun pausePlayback() {
        mediaPlayer.apply {
            if (isPlaying) {
                pause()
            }
        }
    }

    fun stopPlayback() {
        mediaPlayer.apply {
            if (isPlaying || isPaused()) {
                seekTo(0)
                pause()
            }
        }
    }

    fun release() {
        mediaPlayer.release()
        instance = null
    }

    private fun isPaused(): Boolean {
        return mediaPlayer.let {
            it.isPlaying.not() && it.currentPosition > 0
        }
    }
}
