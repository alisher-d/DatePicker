package com.uznewmax.mytaxi.navigationFragments.ui.corporate.helper.datePicker.utils

import com.uznewmax.mytaxi.util.Settings
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import java.util.*

object DateUtils : KoinComponent {
    val settings by inject<Settings>()
    val locale = Locale(settings.language)
    fun getTimeMiles(year: Int, month: Int, day: Int): Long {
        val calendar = Calendar.getInstance(locale)
        calendar[Calendar.YEAR] = year
        calendar[Calendar.MONTH] = month
        val maxDayCount = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        calendar[Calendar.DAY_OF_MONTH] = day.coerceAtMost(maxDayCount)
        return calendar.timeInMillis
    }

    val currentTime: Long
        get() {
            val calendar = Calendar.getInstance(locale)
            calendar[Calendar.HOUR_OF_DAY] = 0
            calendar[Calendar.MINUTE] = 0
            calendar[Calendar.SECOND] = 0
            calendar[Calendar.MILLISECOND] = 0
            return calendar.timeInMillis
        }

    fun getMonthDayCount(timeStamp: Long): Int {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timeStamp
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    }

    fun getDay(timeStamp: Long): Int {
        val calendar = Calendar.getInstance(locale)
        calendar.timeInMillis = timeStamp
        return calendar[Calendar.DAY_OF_MONTH]
    }

    fun getMonth(timeStamp: Long): Int {
        val calendar = Calendar.getInstance(locale)
        calendar.timeInMillis = timeStamp
        return calendar[Calendar.MONTH]
    }

    fun getYear(timeStamp: Long): Int {
        val calendar = Calendar.getInstance(locale)
        calendar.timeInMillis = timeStamp
        return calendar[Calendar.YEAR]
    }
}