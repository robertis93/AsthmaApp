package com.example.asthmaapp.utils

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.asthmaapp.R

class MedicalDialog(var message: String) : DialogFragment()  {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder
                .setMessage(message)
                //.setMessage("Вы забыли указать дату!")
                .setIcon(R.drawable.succsess)
            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed(Runnable { // Close dialog after 1000ms
                dialog?.cancel()
            }, 1000)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}