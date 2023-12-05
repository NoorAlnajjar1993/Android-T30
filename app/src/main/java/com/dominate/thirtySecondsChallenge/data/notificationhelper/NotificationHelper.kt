package com.dominate.thirtySecondsChallenge.data.notificationhelper

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.ui.main.MainActivity

class NotificationHelper(private val context: Context) {

    private val notificationManager: NotificationManager by lazy {
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    init {

        try {
            // Create a notification channel if needed (Android 8.0+)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel()
            }
        }catch (e:Exception){
            Log.i("error", e.message.toString())
        }


    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "your_channel_id"
            val channelName = "Your Channel Name"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, channelName, importance)
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showNotification(message:String) {

        val intent = Intent(context, MainActivity::class.java)
        intent.putExtra("message", message) // You can add any additional data you want to pass

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_MUTABLE
        )

        val notificationBuilder = NotificationCompat.Builder(context, "your_channel_id")
            .setSmallIcon(R.drawable.ic_logo)
            .setContentTitle(context.getString(R.string.app_name))
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)


        // Show the notification
        val notificationId = 1 // Use a unique ID for each notification
        notificationManager.notify(notificationId, notificationBuilder.build())


    }
}