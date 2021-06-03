package com.example.asthmaapp.utils


     fun minuteShow(minute: Int): String {
        if (minute < 10){
            return "0" + minute
        }
        else
            return "${minute}"
    }


