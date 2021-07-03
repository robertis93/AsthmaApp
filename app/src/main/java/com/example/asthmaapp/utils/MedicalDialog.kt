package com.example.asthmaapp.utils

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.asthmaapp.R

class MedicalDialog(var message: Int) : DialogFragment()  {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder
                .setIcon(R.drawable.success)
                .setTitle(R.string.message)
                .setMessage(message)

            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed(Runnable { // Close dialog after 1000ms
                dialog?.cancel()
            }, 1000)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}