package com.uznewmax.mytaxi.navigationFragments.ui.corporate.helper.datePicker.factory

import com.uznewmax.mytaxi.navigationFragments.ui.corporate.helper.datePicker.model.DateModel
import com.uznewmax.mytaxi.navigationFragments.ui.corporate.helper.datePicker.utils.DateUtils
import com.uznewmax.mytaxi.navigationFragments.ui.corporate.util.formatC
import java.text.DateFormatSymbols
import kotlin.math.abs

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
                val day = (i+1).formatC()
                days.add(day)
            }
            return days
        }
    val monthList: List<String>
        get() {
            val monthsArray = dfs.months
            val monthsList = listOf(*monthsArray)
            var max = monthsList.size
            if (selectedDate.year == maxDate.year) {
                max = maxDate.month + 1
            }
            monthMin = if (selectedDate.year == minDate.year) {
                minDate.month
            } else 0
            val months: MutableList<String> = ArrayList()
            for (i in monthMin until max) {
                months.add(monthsList[i])
            }
            return months
        }
    val yearList: List<String>
        get() {
            val yearCount = abs(minDate.year - maxDate.year) + 1
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