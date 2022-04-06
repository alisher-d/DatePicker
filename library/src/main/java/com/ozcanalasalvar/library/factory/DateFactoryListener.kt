package com.ozcanalasalvar.library.factory

interface DateFactoryListener {
    fun onYearChanged()
    fun onMonthChanged()
    fun onDayChanged()
    fun onConfigsChanged()
}