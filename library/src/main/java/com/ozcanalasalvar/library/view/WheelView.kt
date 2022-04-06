package com.ozcanalasalvar.library.view

import android.widget.ScrollView
import android.widget.LinearLayout
import android.widget.TextView
import android.util.TypedValue
import com.ozcanalasalvar.library.R
import android.graphics.drawable.Drawable
import android.app.Activity
import android.graphics.ColorFilter
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.*
import java.util.ArrayList

class WheelView : ScrollView {
    var TAG = WheelView::class.java.simpleName
    private var displayItemCount = 0
    private var selectedIndex = 1
    private var offset = OFF_SET_DEFAULT
    private var configChanged = false
    private var initialY = 0
    private var scrollerTask: Runnable? = null
    private val newCheck = 50
    @JvmField
    var isNightTheme = false

    interface OnWheelViewListener {
        fun onSelected(selectedIndex: Int, item: String?)
    }

    private var context1: Context? = null
    private var views: LinearLayout? = null

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(context)
    }

    private var items: MutableList<String>? = null
    private var textSize = 19
    private var ALIGNMENT = TEXT_ALIGNMENT_CENTER
    private var GRAVITY = Gravity.CENTER
    fun getItems(): List<String>? {
        return items
    }

    fun setItems(list: List<String>?) {
        if (null == items) {
            items = ArrayList()
        }
        items!!.clear()
        items!!.addAll(list!!)
        for (i in 0 until offset) {
            items!!.add(0, "")
            items!!.add("")
        }
        initData()
    }

    fun setOffset(offset: Int) {
        if (this.offset != offset) {
            configChanged = true
        }
        this.offset = offset
    }

    private fun init(context: Context) {
        this.context1 = context
        Log.d(TAG, "parent: " + this.parent)
        this.isVerticalScrollBarEnabled = false
        this.overScrollMode = OVER_SCROLL_NEVER
        views = LinearLayout(context)
        views!!.orientation = LinearLayout.VERTICAL
        this.addView(views)
        scrollerTask = Runnable {
            val newY = scrollY
            if (initialY - newY == 0) { // stopped
                val remainder = initialY % itemHeight
                val divided = initialY / itemHeight
                if (remainder == 0) {
                    selectedIndex = divided + offset
                    onSelectedCallBack()
                } else {
                    if (remainder > itemHeight / 2) {
                        post {
                            smoothScrollTo(0, initialY - remainder + itemHeight)
                            selectedIndex = divided + offset + 1
                            onSelectedCallBack()
                        }
                    } else {
                        post {
                            smoothScrollTo(0, initialY - remainder)
                            selectedIndex = divided + offset
                            onSelectedCallBack()
                        }
                    }
                }
            } else {
                initialY = scrollY
                postDelayed(scrollerTask, newCheck.toLong())
            }
        }
    }

    fun startScrollerTask() {
        initialY = scrollY
        postDelayed(scrollerTask, newCheck.toLong())
    }

    private fun initData() {
        views!!.removeAllViews()
        displayItemCount = offset * 2 + 1
        for (item in items!!) {
            views!!.addView(createView(item))
        }
        val scrollHeight = (selectedIndex - offset) * itemHeight
        refreshItemView(scrollHeight)
    }

    fun setTextSize(textSize: Int) {
        if (this.textSize != textSize) {
            configChanged = true
        }
        this.textSize = textSize
    }

    fun setAlignment(alignment: Int) {
        ALIGNMENT = alignment
    }

    fun setGravity(gravity: Int) {
        GRAVITY = gravity
    }

    var itemHeight = 0
    private fun createView(item: String): LinearLayout {
        val viewContainer = LinearLayout(context1)
        val tv = TextView(context1)
        tv.isClickable = true
        val textLP =
            LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        textLP.gravity = GRAVITY
        tv.layoutParams = textLP
        tv.isSingleLine = true
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize.toFloat())
        tv.text = " $item"
        tv.textAlignment = ALIGNMENT
        tv.gravity = Gravity.CENTER
        val padding = dip2px(1f)
        tv.setPadding(padding, padding, padding, padding)
        val containerParam =
            LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getViewMeasuredHeight(tv))
        viewContainer.layoutParams = containerParam
        viewContainer.addView(tv)
        if (0 == itemHeight || configChanged) {
            itemHeight = getViewMeasuredHeight(tv)
            Log.d(TAG, "itemHeight: $itemHeight")
            views!!.layoutParams =
                LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, itemHeight * displayItemCount)
            val lp = this.layoutParams as LinearLayout.LayoutParams
            this.layoutParams = LinearLayout.LayoutParams(lp.width, itemHeight * displayItemCount)
            configChanged = false
        }
        return viewContainer
    }

    override fun onScrollChanged(l: Int, t: Int, oldL: Int, oldT: Int) {
        super.onScrollChanged(l, t, oldL, oldT)
        refreshItemView(t)
        scrollDirection = if (t > oldT) {
            SCROLL_DIRECTION_DOWN
        } else {
            SCROLL_DIRECTION_UP
        }
    }

    private fun refreshItemView(y: Int) {
        var position = y / itemHeight + offset
        val remainder = y % itemHeight
        val divided = y / itemHeight
        if (remainder == 0) {
            position = divided + offset
        } else {
            if (remainder > itemHeight / 2) {
                position = divided + offset + 1
            }
        }
        val childSize = views!!.childCount
        for (i in 0 until childSize) {
            val itemView = views!!.getChildAt(i) as LinearLayout
            val item = itemView.getChildAt(0) as TextView ?: return
            if (position == i) {
                item.setTextColor(context1!!.getColor(R.color.color_text))
                if (item.textSize != textSize.toFloat()) item.setTextSize(
                    TypedValue.COMPLEX_UNIT_SP,
                    textSize.toFloat()
                )
                val text = item.text.toString()
                item.text = text.trim { it <= ' ' }
            } else if (i < position) {
                if (i == position - 1) {
                    item.setTextSize(TypedValue.COMPLEX_UNIT_SP, (textSize - 2).toFloat())
                    item.setTextColor(context1!!.getColor(R.color.color_grey))
                } else if (i == position - 2) {
                    item.setTextSize(TypedValue.COMPLEX_UNIT_SP, (textSize - 3).toFloat())
                    item.setTextColor(context1!!.getColor(R.color.color_grey1))
                } else {
                    item.setTextSize(TypedValue.COMPLEX_UNIT_SP, (textSize - 4).toFloat())
                    item.setTextColor(context1!!.getColor(R.color.color_grey2))
                }
                var text = item.text.toString()
                text = "  " + text.trim { it <= ' ' }
                item.text = text
            }
            if (i > position) {
                if (i == position + 1) {
                    item.setTextSize(TypedValue.COMPLEX_UNIT_SP, (textSize - 2).toFloat())
                    item.setTextColor(context1!!.getColor(R.color.color_grey))
                } else if (i == position + 2) {
                    item.setTextSize(TypedValue.COMPLEX_UNIT_SP, (textSize - 3).toFloat())
                    item.setTextColor(context1!!.getColor(R.color.color_grey1))
                } else {
                    item.setTextSize(TypedValue.COMPLEX_UNIT_SP, (textSize - 4).toFloat())
                    item.setTextColor(context1!!.getColor(R.color.color_grey2))
                }
                var text = item.text.toString()
                text = "  " + text.trim { it <= ' ' }
                item.text = text
            }
        }
    }

    var selectedAreaBorder: IntArray? = null
    private fun obtainSelectedAreaBorder(): IntArray {
        if (null == selectedAreaBorder) {
            selectedAreaBorder = IntArray(2)
            selectedAreaBorder!![0] = itemHeight * offset
            selectedAreaBorder!![1] = itemHeight * (offset + 1)
        }
        return selectedAreaBorder as IntArray
    }

    private var scrollDirection = -1
    private var paint: Paint? = null
    private var viewWidth = 0
    override fun setBackground(background: Drawable?) {
        var background: Drawable? = background
        if (viewWidth == 0) {
            viewWidth = (context1 as Activity?)!!.windowManager.defaultDisplay.width
            Log.d(TAG, "viewWidth: $viewWidth")
        }
        if (null == paint) {
            paint = Paint()
            paint!!.color = context1!!.getColor(R.color.color_grey2)
            paint!!.strokeWidth = dip2px(1f).toFloat()
        }
        background = object : Drawable() {
            override fun draw(canvas: Canvas) {
                canvas.drawLine(
                    0f,
                    obtainSelectedAreaBorder()[0].toFloat(),
                    viewWidth.toFloat(),
                    obtainSelectedAreaBorder()[0]
                        .toFloat(),
                    paint!!
                )
                canvas.drawLine(
                    0f,
                    obtainSelectedAreaBorder()[1].toFloat(),
                    viewWidth.toFloat(),
                    obtainSelectedAreaBorder()[1]
                        .toFloat(),
                    paint!!
                )
            }

            override fun setAlpha(alpha: Int) {}
            override fun setColorFilter(cf: ColorFilter?) {}
            @SuppressLint("WrongConstant")
            override fun getOpacity(): Int {
                return 0
            }
        }
        super.setBackground(background)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        Log.d(TAG, "w: $w, h: $h, oldw: $oldw, oldh: $oldh")
        viewWidth = w
        setBackground(null)
    }

    private fun onSelectedCallBack() {
        if (null != onWheelViewListener) {
            if (selectedIndex - offset >= 0) onWheelViewListener!!.onSelected(
                selectedIndex - offset,
                items!![selectedIndex]
            )
        }
    }

    fun setSelection(position: Int) {
        selectedIndex = position + offset
        post { smoothScrollTo(0, position * itemHeight) }
    }

    val selectedItem: String
        get() = items!![selectedIndex]

    fun getSelectedIndex(): Int {
        return selectedIndex - offset
    }

    override fun fling(velocityY: Int) {
        super.fling(velocityY / 3)
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_UP) {
            startScrollerTask()
        }
        return super.onTouchEvent(ev)
    }

    private var onWheelViewListener: OnWheelViewListener? = null
    fun setOnWheelViewListener(onWheelViewListener: OnWheelViewListener?) {
        this.onWheelViewListener = onWheelViewListener
    }

    private fun dip2px(dpValue: Float): Int {
        val scale = context1!!.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    private fun getViewMeasuredHeight(view: View): Int {
        val width = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        val expandSpec = MeasureSpec.makeMeasureSpec(Int.MAX_VALUE shr 2, MeasureSpec.AT_MOST)
        view.measure(width, expandSpec)
        return view.measuredHeight
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        // Stop ScrollView from getting involved once you interact with the View
        if (ev.actionMasked == MotionEvent.ACTION_DOWN) {
            val p = parent
            p?.requestDisallowInterceptTouchEvent(true)
        }
        return super.onInterceptTouchEvent(ev)
    }

    companion object {
        const val OFF_SET_DEFAULT = 1
        private const val SCROLL_DIRECTION_UP = 0
        private const val SCROLL_DIRECTION_DOWN = 1
    }
}