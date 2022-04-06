package com.ozcanalasalvar.library.factory

import com.ozcanalasalvar.library.model.DateModel
import com.ozcanalasalvar.library.utils.DateUtils
import java.text.DateFormatSymbols
import java.util.*

class DatePickerFactory(private val listener: DateFactoryListener) {
    var maxDate: DateModel
        private set
    var minDate: DateModel
        private set
    var selectedDate: DateModel
        private set
    var monthMin = 0
        private set

    fun setSelectedYear(year: Int) {
        selectedDate.year = year
        if (selectedDate.year == minDate.year) {
            if (selectedDate.month < minDate.month) {
                selectedDate.month = minDate.month
            } else if (selectedDate.month > maxDate.month) {
                selectedDate.month = maxDate.month
            }
        }
        selectedDate.updateModel()
        listener.onYearChanged()
    }

    fun setSelectedMonth(month: Int) {
        selectedDate.month = month
        selectedDate.updateModel()
        listener.onMonthChanged()
    }

    fun setSelectedDay(day: Int) {
        selectedDate.day = day
        selectedDate.updateModel()
        listener.onDayChanged()
    }

    fun setMaxDate(maxDate: Long) {
        this.maxDate = DateModel(maxDate)
        listener.onConfigsChanged()
    }

    fun setMinDate(minDate: Long) {
        this.minDate = DateModel(minDate)
        listener.onConfigsChanged()
    }

    fun setSelectedDate(selectedDate: Long) {
        this.selectedDate = DateModel(selectedDate)
        listener.onConfigsChanged()
    }

    val dayList: List<String>
        get() {
            var max = DateUtils.getMonthDayCount(selectedDate.getDate())
            var min = 0
            if (selectedDate.year == maxDate.year && selectedDate.month == maxDate.month) {
                max = maxDate.day
            }
            if (selectedDate.year == minDate.year && selectedDate.month == minDate.month) {
                min = minDate.day - 1
            }
            val days: MutableList<String> = ArrayList()
            for (i in min until max) {
                days.add("" + (i + 1))
            }
            return days
        }
    val monthList: List<String>
        get() {
            val monthsArray = dfs.months
            val monthsList = Arrays.asList(*monthsArray)
            var max = monthsList.size
            if (selectedDate.year == maxDate.year) {
                max = maxDate.month + 1
            }
            if (selectedDate.year == minDate.year) {
                monthMin = minDate.month
            } else monthMin = 0
            val months: MutableList<String> = ArrayList()
            for (i in monthMin until max) {
                months.add(monthsList[i])
            }
            return months
        }
    val yearList: List<String>
        get() {
            val yearCount = Math.abs(minDate.year - maxDate.year) + 1
            val years: MutableList<String> = ArrayList()
            for (i in 0 until yearCount) {
                years.add("" + (minDate.year + i))
            }
            return years
        }

    companion object {
        private val dfs = DateFormatSymbols()
    }

    init {
        minDate = DateModel(
            DateUtils.getTimeMiles(
                FactoryConstant.MIN_YEAR,
                FactoryConstant.MIN_MONTH,
                1
            )
        )
        maxDate = DateModel(
            DateUtils.getTimeMiles(
                FactoryConstant.MAX_YEAR,
                FactoryConstant.MAX_MONTH,
                1
            )
        )
        selectedDate = DateModel(DateUtils.currentTime)
    }
}