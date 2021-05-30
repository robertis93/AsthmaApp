package com.example.asthmaapp.viewmodel

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.asthmaapp.view.activities.AlarmActivityTest

class AstmaBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        var i = Intent(context, AlarmActivityTest::class.java)
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (context != null) {
            context.startActivity(i)
        }
    }
}