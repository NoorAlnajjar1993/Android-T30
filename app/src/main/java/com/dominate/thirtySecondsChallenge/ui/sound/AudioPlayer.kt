package com.dominate.thirtySecondsChallenge.ui.sound

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaPlayer
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.utils.pref.PrefConstants
import com.dominate.thirtySecondsChallenge.utils.pref.SharedPreferencesUtil

class AudioPlayer(context: Context, resid: Int) {


    val _context = context
    private val mediaPlayer: MediaPlayer = MediaPlayer.create(context, resid)

    val isSound = SharedPreferencesUtil.getInstance(context).getBooleanPreferences(
        PrefConstants.APP_IS_SOUND,
        true
    )

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: AudioPlayer? = null

        fun getInstance(context: Context , sound:Int? = null): AudioPlayer {
            return instance ?: synchronized(this) {
                instance ?: AudioPlayer(context,sound?: R.raw.sound_shift).also { instance = it }
            }
        }
    }

    // Start playback from a given URI
    @SuppressLint("SuspiciousIndentation")
    fun startPlayback() {
        val isSound = SharedPreferencesUtil.getInstance(_context).getBooleanPreferences(
            PrefConstants.APP_IS_SOUND,
            true
        )
        if (isSound)
        mediaPlayer.apply {
            start()
        }
    }

    // Play sound in a loop
    fun startPlaybackLoop() {
        val isSound = SharedPreferencesUtil.getInstance(_context).getBooleanPreferences(
            PrefConstants.APP_IS_SOUND,
            true
        )
        if (isSound) {
            mediaPlayer.apply {
                // Set looping to true to repeat the sound
                isLooping = true
                start()
            }
        }
    }

    // Stop playback, including looped playback
    fun stopPlaybackWithLoop() {
        mediaPlayer.apply {
            if (isPlaying || isPaused()) {
                isLooping = false // Disable looping
                seekTo(0)  // Rewind to the beginning
                pause()
            }
        }
    }

    // Pause playback
    fun pausePlayback() {
        mediaPlayer.apply {
            if (isPlaying) {
                pause()
            }
        }
    }

    // Stop playback
    fun stopPlayback() {
        mediaPlayer.apply {
            if (isPlaying || isPaused()) {
                seekTo(0)  // Rewind to the beginning
                pause()
            }
        }
    }

    // Release resources when the player is no longer needed
    fun release() {
        mediaPlayer.release()
        instance = null
    }

    // Check if the MediaPlayer is in a paused state
    private fun isPaused(): Boolean {
        return mediaPlayer?.let {
            it.isPlaying.not() && it.currentPosition > 0
        } ?: false
    }
}
