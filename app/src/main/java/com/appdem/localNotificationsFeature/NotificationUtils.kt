package com.appdem.localNotificationsFeature

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import java.util.*

class NotificationUtils(private val context: Context) {

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "default_notification_channel_id"
        private const val NOTIFICATION_CHANNEL_NAME = "Default Channel"
        private const val NOTIFICATION_CHANNEL_DESCRIPTION = "Notification Channel Description"
        const val NOTIFICATION_ID = 100

        /** 2 min in milliseconds (let's say that this time is equivalent of 2 hours) **/
        private const val NOTIFICATION_INTERVAL = 2 * 60 * 1000
    }

    fun scheduleNotification() {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()

        /** Set the start time to 12 PM -> teacher start working hour **/
        calendar.set(Calendar.HOUR_OF_DAY, 12)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = PendingIntent.getBroadcast(
            context,
            NOTIFICATION_ID,
            NotificationBroadcastReceiver.getIntent(context, NOTIFICATION_ID),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        /** Schedule the notification to repeat every 2 hours from 12 PM to 8 PM(teacher end working hour) **/
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            NOTIFICATION_INTERVAL.toLong(),
            intent
        )
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = NOTIFICATION_CHANNEL_DESCRIPTION
                enableVibration(true)
                setShowBadge(true)
            }

            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        } else {
            val notification = NotificationCompat.Builder(context)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Reminder")
                .setContentText("Your class will start soon!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .build()

            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.notify(NOTIFICATION_ID, notification)
        }

    }

    fun showNotification(title: String, message: String) {
        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.notify(NOTIFICATION_ID, notification)
    }
}
