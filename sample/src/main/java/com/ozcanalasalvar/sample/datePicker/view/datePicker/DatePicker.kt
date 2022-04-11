package com.uznewmax.mytaxi.navigationFragments.ui.corporate.helper.datePicker.view.datePicker

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import com.uznewmax.mytaxi.navigationFragments.ui.corporate.helper.datePicker.factory.DateFactoryListener
import com.uznewmax.mytaxi.navigationFragments.ui.corporate.helper.datePicker.factory.DatePickerFactory
import com.uznewmax.mytaxi.navigationFragments.ui.corporate.helper.datePicker.view.WheelView

class DatePicker : LinearLayout, DateFactoryListener {
    private var context1: Context? = null
    private var container: LinearLayout? = null
    private var offset = 2
    private var factory: DatePickerFactory? = null
    private var dayView: WheelView? = null
    private var monthView: WheelView? = null
    private var yearView: WheelView? = null

    private var textSize = 22
    private var pickerMode = 0


    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    private fun init(context: Context) {
        this.context1 = context
        this.orientation = HORIZONTAL
        factory = DatePickerFactory(this)
        container = LinearLayout(context)
        val layoutParams =
            LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        container!!.layoutParams = layoutParams
        container!!.orientation = HORIZONTAL
        this.addView(container)
        setUpInitialViews()
    }


    private fun setUpInitialViews() {
        container!!.removeAllViews()
        container!!.addView(createDayView(context1))
        container!!.addView(createMonthView(context1))
        container!!.addView(createYearView(context1))
        setUpCalendar()
    }

    private fun setUpCalendar() {
        setUpYearView()
        setUpMonthView()
        setUpDayView()
    }


    private fun setUpYearView() {
        val date = factory!!.selectedDate
        val years = factory!!.yearList
        yearView!!.setOffset(offset)
        yearView!!.setTextSize(textSize)
        yearView!!.setAlignment(TEXT_ALIGNMENT_CENTER)
        yearView!!.setGravity(Gravity.CENTER)
        yearView!!.setItems(years)
        yearView!!.setSelection(years.indexOf("${date.year}"))
    }

    private fun setUpMonthView() {
        val months = factory!!.monthList
        val date = factory!!.selectedDate
        monthView!!.setTextSize(textSize)
        monthView!!.setGravity(Gravity.CENTER)
        monthView!!.setAlignment(TEXT_ALIGNMENT_CENTER)
        monthView!!.setOffset(offset)
        monthView!!.setItems(months)
        monthView!!.setSelection(date.month - factory!!.monthMin)
    }

    private fun setUpDayView() {
        val date = factory!!.selectedDate
        val days = factory!!.dayList
        dayView!!.setOffset(offset)
        dayView!!.setTextSize(textSize)
        dayView!!.setGravity(Gravity.CENTER)
        dayView!!.setAlignment(TEXT_ALIGNMENT_CENTER)
        dayView!!.setOffset(offset)
        dayView!!.setItems(days)
        dayView!!.setSelection(date.day - 1) //Day start from 1
    }

    private fun createYearView(context: Context?): LinearLayout {
        yearView = WheelView(context!!)
        yearView!!.layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        yearView!!.setOnWheelViewListener(object : WheelView.OnWheelViewListener {
            override fun onSelected(selectedIndex: Int, item: String?) {
                factory!!.setSelectedYear(
                    item!!.toInt()
                )
            }
        })

        val ly = wheelContainerView()
        ly.addView(yearView)
        return ly
    }

    private fun createMonthView(context: Context?): LinearLayout {
        monthView = WheelView(context!!)
        monthView!!.layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        monthView!!.setOnWheelViewListener(object : WheelView.OnWheelViewListener {
            override fun onSelected(selectedIndex: Int, item: String?) {
                factory!!.setSelectedMonth(
                    factory!!.monthMin + selectedIndex
                )
            }

        })
        val ly = wheelContainerView()
        ly.addView(monthView)
        return ly
    }

    private fun createDayView(context: Context?): LinearLayout {
        dayView = WheelView(context!!)
        dayView!!.layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        dayView!!.setOnWheelViewListener(object : WheelView.OnWheelViewListener {
            override fun onSelected(selectedIndex: Int, item: String?) {
                factory!!.setSelectedDay(
                    selectedIndex + 1
                )
            }

        })
        val ly = wheelContainerView()
        ly.addView(dayView)
        return ly
    }

    private fun wheelContainerView(weight: Float = 1.0f): LinearLayout {
        val layout = LinearLayout(context1)
        val layoutParams = LayoutParams(0, WRAP_CONTENT, weight)
        layout.layoutParams = layoutParams
        layout.orientation = VERTICAL
        return layout
    }

    var minDate: Long
        get() = factory!!.minDate.getDate()
        set(date) {
            factory!!.setMinDate(date)
        }

    var maxDate: Long
        get() = factory!!.maxDate.getDate()
        set(date) {
            factory!!.setMaxDate(date)
        }

    var date: Long
        get() = factory!!.selectedDate.getDate()
        set(date) {
            factory!!.setSelectedDate(date)
        }

    fun getOffset(): Int {
        return offset
    }

    fun setOffset(offset: Int) {
        this.offset = offset
        setUpCalendar()
    }

    fun setTextSize(textSize: Int) {
        this.textSize = textSize.coerceAtMost(MAX_TEXT_SIZE)
        setUpCalendar()
    }

    fun setPickerMode(pickerMode: Int) {
        this.pickerMode = pickerMode
        setUpInitialViews()
    }

    override fun onYearChanged() {
        setUpMonthView()
        setUpDayView()
        notifyDateSelect()
    }

    override fun onMonthChanged() {
        setUpDayView()
        notifyDateSelect()
    }

    override fun onDayChanged() {
        notifyDateSelect()
    }

    override fun onConfigsChanged() {
        setUpCalendar()
    }

    interface DataSelectListener {
        fun onDateSelected(date: Long, day: Int, month: Int, year: Int)
    }

    private var dataSelectListener: DataSelectListener? = null
    fun setDataSelectListener(dataSelectListener: DataSelectListener?) {
        this.dataSelectListener = dataSelectListener
    }

    private fun notifyDateSelect() {
        val date = factory!!.selectedDate
        if (dataSelectListener != null) dataSelectListener!!.onDateSelected(
            date.getDate(),
            date.day,
            date.month,
            date.year
        )
    }

    companion object {
        private const val MAX_TEXT_SIZE = 24
    }
}