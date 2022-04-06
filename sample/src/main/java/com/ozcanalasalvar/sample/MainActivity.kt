package com.ozcanalasalvar.sample

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ozcanalasalvar.library.utils.DateUtils
import com.ozcanalasalvar.library.view.datePicker.DatePicker
import com.ozcanalasalvar.library.view.datePicker.DatePicker.DataSelectListener
import com.ozcanalasalvar.library.view.popup.DatePickerPopup
import com.ozcanalasalvar.library.view.popup.DatePickerPopup.OnDateSelectListener
import com.ozcanalasalvar.library.view.popup.TimePickerPopup
import com.ozcanalasalvar.library.view.popup.TimePickerPopup.OnTimeSelectListener
import com.ozcanalasalvar.library.view.timePicker.TimePicker

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private var pickerPopup: TimePickerPopup? = null
    private var datePickerPopup: DatePickerPopup? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val textDate = findViewById<TextView>(R.id.text_date)
        val textTime = findViewById<TextView>(R.id.text_time)
        datePickerPopup = DatePickerPopup.Builder()
            .from(this)
            .offset(3)
            .darkModeEnabled(true)
            .pickerMode(DatePicker.MONTH_ON_FIRST)
            .textSize(19)
            .endDate(DateUtils.getTimeMiles(2050, 10, 25))
            .currentDate(DateUtils.currentTime)
            .startDate(DateUtils.getTimeMiles(1995, 0, 1))
            .listener(object : OnDateSelectListener {
                override fun onDateSelected(
                    dp: DatePicker?,
                    date: Long,
                    day: Int,
                    month: Int,
                    year: Int
                ) {
                    Toast.makeText(
                        applicationContext,
                        "" + day + "/" + (month + 1) + "/" + year,
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })
            .build()
        pickerPopup = TimePickerPopup.Builder()
            .from(this)
            .offset(3)
            .textSize(17)
            .setTime(12, 12)
            .listener(object : OnTimeSelectListener {
                override fun onTimeSelected(timePicker: TimePicker?, hour: Int, minute: Int) {
                    Toast.makeText(applicationContext, "$hour:$minute", Toast.LENGTH_SHORT).show()
                }

            })
            .build()
        val datePicker = findViewById<DatePicker>(R.id.datepicker)
        datePicker.setOffset(3)
        datePicker.setDarkModeEnabled(true)
        datePicker.setTextSize(19)
        datePicker.maxDate = DateUtils.getTimeMiles(2050, 10, 25)
        datePicker.date = DateUtils.currentTime
        datePicker.minDate = DateUtils.getTimeMiles(1995, 1, 12)
        datePicker.setPickerMode(DatePicker.DAY_ON_FIRST)

        datePicker.setDataSelectListener(object : DataSelectListener {
            override fun onDateSelected(date: Long, day: Int, month: Int, year: Int) {
                textDate.text = "" + day + "/" + (month + 1) + "/" + year
            }
        })

        val timePicker = findViewById<TimePicker>(R.id.timepicker)
        timePicker.setOffset(2)
        timePicker.setTextSize(19)
        timePicker.hour = 9
        timePicker.minute = 5
        timePicker.setTimeSelectListener(object :TimePicker.TimeSelectListener{
            override fun onTimeSelected(hour: Int, minute: Int) {
                textTime.text = "$hour:$minute"
            }

        })
    }

    fun openDatePicker(view: View?) {
        datePickerPopup!!.show()
    }

    fun openTimePicker(view: View?) {
        pickerPopup!!.show()
    }
}