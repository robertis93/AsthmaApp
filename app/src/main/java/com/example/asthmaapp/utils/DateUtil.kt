package com.example.asthmaapp.utils

import android.annotation.SuppressLint
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*

object DateUtil {
    @SuppressLint("SimpleDateFormat")
    private val dateFormat = SimpleDateFormat("dd MMM yyyy")

    @SuppressLint("SimpleDateFormat")
    private val timeFormat = SimpleDateFormat("HH : mm")

    @SuppressLint("SimpleDateFormat")
    private val hourFormat = SimpleDateFormat("HH")

    @SuppressLint("SimpleDateFormat")
    private val minuteFormat = SimpleDateFormat("mm")

    @SuppressLint("SimpleDateFormat")
    fun timestampToDisplayDate(dayTimeStamp: Long): String {
        val currentDate = Date(dayTimeStamp)
        return dateFormat.format(currentDate)
    }

    fun timestampToDisplayTime(dayTimeStamp: Long): String {
        val currentDate = Date(dayTimeStamp)
        return timeFormat.format(currentDate)
    }

    fun dayTimeStamp(dayTimeStamp: Long, hour: Int, minute: Int): Long {
        val cal = GregorianCalendar()
        cal.timeInMillis = dayTimeStamp
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val dayOfMonth = cal.get(Calendar.DAY_OF_MONTH)
        val dateCalendar: Calendar = GregorianCalendar(year, month, dayOfMonth, hour, minute)
        return dateCalendar.time.time
    }


    fun timeCorrectDisplay(minute: Int): String {
        return if (minute < 10) {
            "0$minute"
        } else {
            "$minute"
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun timestampToDisplayHour(dayTimeStamp: Long): String {
        val currentDate = Date(dayTimeStamp)
        return hourFormat.format(currentDate)
    }

    @SuppressLint("SimpleDateFormat")
    fun timestampToDisplayMinute(dayTimeStamp: Long): String {
        val currentDate = Date(dayTimeStamp)
        return minuteFormat.format(currentDate)
    }

    fun dateStringToDayTimeStamp(date: String): Long {
        val day: Date = dateFormat.parse(date)
        return day.time
    }

    fun checkingEnableButton(medicamentNameEditText: TextInputEditText, medicamentDoseEditText: TextInputEditText, btnSave: MaterialButton) {
        medicamentNameEditText.doAfterTextChanged {
            btnSave.isEnabled =
                medicamentDoseEditText.text.toString().length in 2..5 && medicamentNameEditText.text.toString().length in 2..20
        }
        medicamentDoseEditText.doAfterTextChanged {
            btnSave.isEnabled =
                medicamentDoseEditText.text.toString().length in 2..5 && medicamentNameEditText.text.toString().length in 2..20
        }
    }
}



