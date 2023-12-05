package com.dominate.thirtySecondsChallenge.ui.sound

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaPlayer
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.utils.pref.PrefConstants
import com.dominate.thirtySecondsChallenge.utils.pref.SharedPreferencesUtil

class MusicPlayer private constructor(context: Context, resid: Int) {

    val _context = context
    private val mediaPlayer: MediaPlayer = MediaPlayer.create(context, resid)

    val isMusic = SharedPreferencesUtil.getInstance(context).getBooleanPreferences(
        PrefConstants.APP_IS_MUSIC,
        true
    )

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: MusicPlayer? = null

        fun getInstance(context: Context , sound:Int? = null): MusicPlayer {
            return instance ?: synchronized(this) {
                instance ?: MusicPlayer(context,sound?: R.raw.sound_shift).also { instance = it }
            }
        }
    }

//    init {
//        // Initialize the MediaPlayer in the constructor
//        mediaPlayer = MediaPlayer.create(context, resid)
//    }

    // Start playback from a given URI
    @SuppressLint("SuspiciousIndentation")
    fun startPlayback() {
        val isMusic = SharedPreferencesUtil.getInstance(_context).getBooleanPreferences(
            PrefConstants.APP_IS_MUSIC,
            true
        )
        if (isMusic)
            mediaPlayer.apply {
                start()
            }
    }

    // Play sound in a loop
    fun startPlaybackLoop() {
        val isMusic = SharedPreferencesUtil.getInstance(_context).getBooleanPreferences(
            PrefConstants.APP_IS_MUSIC,
            true
        )
        if (isMusic) {
            mediaPlayer.apply {
                // Set looping to true to repeat the sound
                isLooping = true
                start()
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
        return mediaPlayer.let {
            it.isPlaying.not() && it.currentPosition > 0
        } ?: false
    }
}
