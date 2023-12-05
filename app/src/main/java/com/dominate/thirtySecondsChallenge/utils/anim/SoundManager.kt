package com.dominate.thirtySecondsChallenge.utils.anim

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.os.Handler
import com.dominate.thirtySecondsChallenge.R

class SoundManager(context: Context) {
    private val soundPool: SoundPool
    private val soundMap: HashMap<Int, Int> = HashMap()
    private var currentlyPlayingSoundId: Int? = null
    private val replayDelayMillis = 1000L // Adjust this as needed
    private val handler: Handler = Handler()

    init {
        val audioAttributes: AudioAttributes = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
        } else {
            AudioAttributes.Builder()
                .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                .build()
        }

        soundPool = SoundPool.Builder()
            .setAudioAttributes(audioAttributes)
            .setMaxStreams(5) // You can adjust the number of streams as needed
            .build()

        // Load your sound effect(s) here
        val soundId = soundPool.load(context, R.raw.select, 1)
        soundMap[R.raw.select] = soundId
    }

    fun playSound(soundResId: Int = R.raw.select) {
        val soundId = soundMap[soundResId]
        soundId?.let {
            currentlyPlayingSoundId = it
            soundPool.setOnLoadCompleteListener { soundPool, sampleId, status ->
                if (status == 0) {
                    // The sound has been loaded successfully
                    soundPool.play(sampleId, 1.0f, 1.0f, 1, 0, 1.0f)

                    // Automatically replay the sound after a delay
                    handler.postDelayed({
                        replaySound()
                    }, replayDelayMillis)
                }
            }
        }
    }

    private fun replaySound() {
        currentlyPlayingSoundId?.let { soundId ->
            soundPool.stop(soundId)
            soundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f)
        }
    }

    fun pauseSound() {
        currentlyPlayingSoundId?.let {
            soundPool.pause(it)
        }
    }

    fun resumeSound() {
        currentlyPlayingSoundId?.let {
            soundPool.resume(it)
        }
    }

    fun stopSound() {
        currentlyPlayingSoundId?.let {
            soundPool.stop(it)
        }
    }
}