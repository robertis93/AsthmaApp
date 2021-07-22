package com.example.asthmaapp.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.asthmaapp.R
import com.example.asthmaapp.view.activities.AlarmMeasureNotificationActivity
import com.example.asthmaapp.view.activities.AlarmMedicamentNotificationActivity

object NotificationsHelper {
    private lateinit var showMessage: String
    private const val CHANNEL_ID = "1"

    fun showNotification(context: Context, message: String) {
        createNotificationChannel(context)

        var intent = Intent()

        if (message == "MEDICAMENT") {
            intent = Intent(context, AlarmMedicamentNotificationActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                showMessage = "Примите лекарство"
            }
        } else if (message == "MEASURE") {
            intent = Intent(context, AlarmMeasureNotificationActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                showMessage = "Сделайте замер"
            }
        }
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        val notification =
            NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(com.example.asthmaapp.R.drawable.ic_astmaicon)
                .setContentTitle("Напоминание")
                .setContentText(showMessage)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .build()
        val alarmId = 0
        NotificationManagerCompat.from(context).notify(alarmId, notification)
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID, // зачем?? как дает уведомление?? Идентификатор канала. Должен быть уникальным для каждой упаковки.
                context.getString(R.string.notification),
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description =
                    context.getString(R.string.app_description)
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}