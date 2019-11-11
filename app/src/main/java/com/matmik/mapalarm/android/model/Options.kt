package com.matmik.mapalarm.android.model

import java.util.*

enum class Options {
    Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday;

    companion object {
        fun calendarDayOfWeek(x: Int) = when (x) {
            Calendar.SUNDAY -> Sunday
            Calendar.MONDAY -> Monday
            Calendar.TUESDAY -> Tuesday
            Calendar.WEDNESDAY -> Wednesday
            Calendar.THURSDAY -> Thursday
            Calendar.FRIDAY -> Friday
            Calendar.SATURDAY -> Saturday
            else -> Exception("No such day of week")
        }
    }
}