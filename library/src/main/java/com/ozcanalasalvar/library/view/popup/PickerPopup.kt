package com.ozcanalasalvar.library.view.popup

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ozcanalasalvar.library.R

open class PickerPopup : BottomSheetDialog, IPopupInterface {
    @JvmField
    var confirm: TextView? = null
    var cancel: TextView? = null
    private var container: LinearLayout? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, theme: Int) : super(context, theme) {
        init()
    }

    private fun init() {
        setContentView(R.layout.picker_popup_layout)
        confirm = findViewById(R.id.text_confirm)
        cancel = findViewById(R.id.text_cancel)
        container = findViewById(R.id.popup_container)
        cancel!!.setOnClickListener { view: View? -> dismiss() }
    }

    override fun addView(view: View?) {
        container!!.addView(view)
    }
}