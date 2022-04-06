package com.ozcanalasalvar.library.model

import com.ozcanalasalvar.library.utils.DateUtils

class DateModel(date: Long) {
    var year = 0
    var month = 0
    var day = 0
    private var date: Long = 0

    init {
        setDate(date)
    }

    fun getDate(): Long {
        return date
    }

    fun setDate(date: Long) {
        this.date = date
        day = DateUtils.getDay(date)
        month = DateUtils.getMonth(date)
        year = DateUtils.getYear(date)
    }

    fun updateModel() {
        setDate(DateUtils.getTimeMiles(year, month, day))
    }

    override fun toString(): String {
        return "DateModel{" +
                "year=" + year +
                ", month=" + month +
                ", day=" + day +
                ", date=" + date +
                '}'
    }
}