package com.appdem.localNotificationsFeature

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import com.appdem.MainActivity
import com.appdem.R

class NotificationBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val notificationUtils = NotificationUtils(context)
        notificationUtils.showNotification("Reminder", "Your class will start soon!")

        val pendingIntent = NavDeepLinkBuilder(context)
            .setComponentName(MainActivity::class.java)
            .setGraph(R.navigation.nav_graph_home)
            .setDestination(R.id.fragmentThree)
            .createPendingIntent()

        val notification = NotificationCompat.Builder(context, NotificationUtils.NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Reminder")
            .setContentText("Your class will start soon!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.notify(NotificationUtils.NOTIFICATION_ID, notification)
    }

    companion object {
        fun getIntent(context: Context, notificationId: Int): Intent {
            val intent = Intent(context, NotificationBroadcastReceiver::class.java)
            intent.putExtra("notification_id", notificationId)
            return intent
        }
    }
}