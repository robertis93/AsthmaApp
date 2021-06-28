package com.example.asthmaapp.utils

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.asthmaapp.R

class AddFragmentDialog(var message: String) : DialogFragment()  {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(R.string.important_message)
                .setMessage(message)
                .setIcon(R.drawable.attentionicon)
                .setPositiveButton(R.string.ok_i_do_it) {
                        dialog, id ->  dialog.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}