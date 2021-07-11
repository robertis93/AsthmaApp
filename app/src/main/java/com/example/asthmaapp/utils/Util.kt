package com.example.asthmaapp.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

fun timeConvert(minute: Int): String {
    return if (minute < 10) {
        "0$minute"
    } else {
        "$minute"
    }
}
// TODO: add another DateSimpleFormat
// TODO: use and rename val dateFormat = SimpleDateFormat("dd MMM YYYY")
@SuppressLint("SimpleDateFormat")
fun millisecondsToStringDateDayMonthYear(millisecond: Long): String {
    //format date
    val currentDate = Date(millisecond)
    val dateFormat = SimpleDateFormat("dd MMM YYYY")
    return dateFormat.format(currentDate)
}



