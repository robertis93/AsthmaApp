package com.example.asthmaapp.utils

import java.text.SimpleDateFormat
import java.util.*


fun minuteShow(minute: Int): String {
    if (minute < 10) {
        return "0" + minute
    } else
        return "${minute}"
}

fun longToStringCalendar(millisecond: Long): String {
    //format date
    val currentDate = Date(millisecond)
    val dateFormat = SimpleDateFormat("dd MMM YYYY")
    return dateFormat.format(currentDate)
}

//    val dateCalendar: Calendar = GregorianCalendar(yearMeasure, mounthMeasure, dayMeasure)
//    val dateMilli  = dateCalendar.time.time
//
//    //format date
//    val currentDate = Date(dateMilli)
//    val dateFormat = SimpleDateFormat("dd MMM YYYY")
//    val ddate = dateFormat.format(currentDate)


