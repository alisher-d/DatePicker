package com.ozcanalasalvar.library.view.popup

import android.content.Context
import android.view.View
import com.ozcanalasalvar.library.view.timePicker.TimePicker

class TimePickerPopup : PickerPopup {
    private var listener: OnTimeSelectListener? = null

    constructor(context: Context) : super(context) {
        val timePicker = TimePicker(context)
        init(timePicker)
    }

    constructor(context: Context, timePicker: TimePicker) : super(context) {
        init(timePicker)
    }

    constructor(context: Context, theme: Int, timePicker: TimePicker) : super(context, theme) {
        init(timePicker)
    }

    private fun init(timePicker: TimePicker) {
        setCancelable(false)
        setCanceledOnTouchOutside(true)
        confirm!!.setOnClickListener { view: View? ->
            if (listener != null) listener!!.onTimeSelected(
                timePicker,
                timePicker.hour,
                timePicker.minute
            )
            dismiss()
        }
        addView(timePicker)
    }

    fun setListener(listener: OnTimeSelectListener?) {
        this.listener = listener
    }

    class Builder {
        private var context: Context? = null
        private var timePicker: TimePicker? = null
        private var listener: OnTimeSelectListener? = null
        fun from(context: Context?): Builder {
            this.context = context
            timePicker = context?.let { TimePicker(it) }
            return this
        }

        fun textSize(textSize: Int): Builder {
            timePicker!!.setTextSize(textSize)
            return this
        }

        fun offset(offset: Int): Builder {
            timePicker!!.setOffset(offset)
            return this
        }

        fun setTime(hour: Int, minute: Int): Builder {
            timePicker!!.setTime(hour, minute)
            return this
        }

        fun listener(listener: OnTimeSelectListener?): Builder {
            this.listener = listener
            return this
        }

        fun build(): TimePickerPopup {
            val popup = TimePickerPopup(context!!, timePicker!!)
            popup.setListener(listener)
            return popup
        }

        fun build(theme: Int): TimePickerPopup {
            val popup = TimePickerPopup(context!!, theme, timePicker!!)
            popup.setListener(listener)
            return popup
        }
    }

    interface OnTimeSelectListener {
        fun onTimeSelected(timePicker: TimePicker?, hour: Int, minute: Int)
    }
}