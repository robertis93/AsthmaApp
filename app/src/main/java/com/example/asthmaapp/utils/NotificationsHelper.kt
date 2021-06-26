package com.example.asthmaapp.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.asthmaapp.view.activities.AlarmActivityTest
import com.example.asthmaapp.view.activities.MedicTimeAlarmActivityTest

object  NotificationsHelper {
    const val CHANNEL_ID = "1"
    fun showNotification(context: Context, message: String) {
        createNotificationChannel(context)

        var intent = Intent()

        //intent для перехода при нажатие на уведомление

        if (message == "Примите лекарство"){
            Log.i("myLogs", "intent = Intent(context, MedicTimeAlarmActivityTest::class.java).apply ")
            intent = Intent(context, MedicTimeAlarmActivityTest::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
        }
        else if (message == "Сделайте замер") {
            intent = Intent(context, AlarmActivityTest::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                Log.i("myLogs", "intent =  Intent(context, AlarmActivityTest::class.java")
            }
        }
        // передаем время которое сейчас
        //intent.putExtra("dateTime", LocalDateTime.now().toString())
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        // Создаём уведомление
        val notification =
            NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(com.example.asthmaapp.R.drawable.ic_free_icon_inhaler_3654855)
                .setContentTitle("Напоминание")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    //реакция на уведомление, Нажатие на уведомление
                .setContentIntent(pendingIntent)
                .build()
        val alarmId = 0   //доделать alarmId, это индификатор сообщения , для обработки клика сообщения
        // посылаем уведомление
        NotificationManagerCompat.from(context).notify(alarmId, notification)
    }

    //Начиная с Android 8 поддерживаются каналы уведомлений.
    // Это нужно для того, чтобы пользователь смог отключить только те уведомления, которые ему не интересны.
    // Например можно отключить все, что касается “новостей в приложении”,
    // но оставить важные “системные” уведомления.
    private fun createNotificationChannel(context: Context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID, // зачем?? как дает уведомление?? Идентификатор канала. Должен быть уникальным для каждой упаковки.
                "уведовление",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description =
                    "уведомление о приеме лекарства и времени замера скорости выдоха при помощи пикфлометра"
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}