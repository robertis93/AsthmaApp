package com.example.asthmaapp.utils

import android.app.Dialog
import android.os.Bundle
import android.os.Message
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.asthmaapp.R

class AddFragmentDialog(var message: String) : DialogFragment()  {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Важное сообщение!")
                .setMessage(message)
                //.setMessage("Вы забыли указать дату!")
                .setIcon(R.drawable.attentionicon)
                .setPositiveButton("ОК, сейчас сделаю") {
                        dialog, id ->  dialog.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}