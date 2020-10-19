package com.debts.debtstracker.util

import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit

const val MINUTES_PAST_CREATION = "minutes"
const val HOURS_PAST_CREATION = "hours"
const val DAYS_PAST_CREATION = "days"


fun getStringDate(milliseconds: Long): String {
    val simpleDateFormat = SimpleDateFormat("HH:mm dd/MM")
    return simpleDateFormat.format(milliseconds)
}

fun getHourDate(milliseconds: Long): Long {
    return TimeUnit.MILLISECONDS.toHours(milliseconds)
}

fun getMinutesDate(milliseconds: Long): Long {
    return TimeUnit.MILLISECONDS.toMinutes(milliseconds)
}

fun getCreationDateType(milliseconds: Long): String {
    val hours = getHourDate(System.currentTimeMillis() - milliseconds)

    return when {
        hours < 1 -> HOURS_PAST_CREATION
        hours < 24 -> HOURS_PAST_CREATION
        else -> DAYS_PAST_CREATION
    }
}
