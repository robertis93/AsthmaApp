package com.example.asthmaapp.viewmodel

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.asthmaapp.R
import com.example.asthmaapp.view.activities.AlarmActivityTest

class AstmaBroadcastReceiver : BroadcastReceiver() {

    companion object {
        const val NOTIFICATION_ID = 101
        const val CHANNEL_ID = "channelID"
    }

    override fun onReceive(context: Context, intent: Intent?) {
//        // Создаём уведомление
//        val builder =
//            NotificationCompat.Builder(it, CHANNEL_ID)
//                .setSmallIcon(R.drawable.ic_free_icon_inhaler_3654855)
//                .setContentTitle("Напоминание")
//                .setContentText("Пора покормить кота")
//                .setAutoCancel(true)
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//
//        with(NotificationManagerCompat.from(context)) {
//            notify(NOTIFICATION_ID, builder.build()) // посылаем уведомление
//        }
//        var i = Intent(context, AlarmActivityTest::class.java)
//        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//        if (context != null) {
//            context.startActivity(i)
//        }
    }
}