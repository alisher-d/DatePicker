package com.ozcanalasalvar.library.factory

import com.ozcanalasalvar.library.utils.DateUtils

class TimeFactory(private val listener: TimeFactoryListener) {
    private var hour: Int
    private var minute: Int
    fun getHour(): Int {
        return hour
    }

    fun setHour(hour: Int) {
        var hour = hour
        if (hour > 23) hour = 23
        this.hour = hour
        listener.onHourChanged(hour)
    }

    fun getMinute(): Int {
        return minute
    }

    fun setMinute(minute: Int) {
        var minute = minute
        if (minute > 59) minute = 59
        this.minute = minute
        listener.onMinuteChanged(minute)
    }

    init {
        hour = DateUtils.currentHour
        minute = DateUtils.currentMinute
    }
}