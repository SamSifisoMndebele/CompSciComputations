package com.compscicomputations.utils.pdfviewer.scroll

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.compscicomputations.R
import com.compscicomputations.utils.pdfviewer.PDFView
import com.compscicomputations.utils.pdfviewer.util.Util

open class MyScrollHandle(context: Context) : RelativeLayout(context), ScrollHandle {
    private var relativeHandlerMiddle = 0f
    private var textView: TextView
    private var pdfView: PDFView? = null
    private var currentPos = 0f
    private val myHandler = Handler(Looper.getMainLooper())
    private val hidePageScrollerRunnable = Runnable { hide() }
    private var i = false
    override fun setupLayout(pdfView: PDFView) {
        val width: Int = HANDLE_WIDTH
        val height: Int = HANDLE_HEIGHT
        val align: Int = ALIGN_PARENT_RIGHT
        val background: Drawable? = ContextCompat.getDrawable(context, R.drawable.scroll_handle)
        setBackground(background)
        val lp = LayoutParams(
            Util.getDP(
                context, width
            ), Util.getDP(context, height)
        )
        lp.setMargins(0, 0, 0, 0)
        val tvlp = LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        tvlp.addRule(CENTER_IN_PARENT, TRUE)
        addView(textView, tvlp)
        lp.addRule(align)
        pdfView.addView(this, lp)
        this.pdfView = pdfView
    }

    override fun destroyLayout() {
        pdfView!!.removeView(this)
    }

    override fun setScroll(position: Float) {
        if (!shown() && i) {
            show()
        } else {
            myHandler.removeCallbacks(hidePageScrollerRunnable)
        }
        i = true
        if (pdfView != null) {
            setPosition((if (pdfView!!.isSwipeVertical) pdfView!!.height else pdfView!!.width) * position)
        }
    }

    private fun setPosition(position: Float) {
        var pos = position
        if (java.lang.Float.isInfinite(pos) || java.lang.Float.isNaN(pos)) {
            return
        }
        val pdfViewSize: Float = if (pdfView!!.isSwipeVertical) {
            pdfView!!.height.toFloat()
        } else {
            pdfView!!.width.toFloat()
        }
        pos -= relativeHandlerMiddle
        if (pos < 0) {
            pos = 0f
        } else if (pos > pdfViewSize - Util.getDP(context, HANDLE_HEIGHT)) {
            pos = pdfViewSize - Util.getDP(context, HANDLE_HEIGHT)
        }
        if (pdfView!!.isSwipeVertical) {
            y = pos
        } else {
            x = pos
        }
        calculateMiddle()
        invalidate()
    }

    private fun calculateMiddle() {
        val pos: Float = y
        val viewSize: Float = height.toFloat()
        val pdfViewSize: Float = pdfView!!.height.toFloat()
        relativeHandlerMiddle = (pos + relativeHandlerMiddle) / pdfViewSize * viewSize
    }

    override fun hideDelayed() {
        myHandler.postDelayed(hidePageScrollerRunnable, 500)
    }

    override fun setPageNum(pageNum: Int) {
        val text = pageNum.toString()
        if (textView.text != text) {
            textView.text = text
        }
    }

    override fun shown(): Boolean {
        return visibility == VISIBLE
    }

    override fun show() {
        visibility = VISIBLE
    }

    override fun hide() {
        visibility = INVISIBLE
    }

    private val isPDFViewReady: Boolean
        get() = pdfView != null && pdfView!!.pageCount > 0 && !pdfView!!.documentFitsView()

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isPDFViewReady) {
            return super.onTouchEvent(event)
        }
        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                pdfView!!.stopFling()
                myHandler.removeCallbacks(hidePageScrollerRunnable)
                currentPos = if (pdfView!!.isSwipeVertical) {
                    event.rawY - y
                } else {
                    event.rawX - x
                }
                if (pdfView!!.isSwipeVertical) {
                    setPosition(event.rawY - currentPos + relativeHandlerMiddle)
                    pdfView!!.setPositionOffset(relativeHandlerMiddle / height.toFloat(), false)
                } else {
                    setPosition(event.rawX - currentPos + relativeHandlerMiddle)
                    pdfView!!.setPositionOffset(relativeHandlerMiddle / width.toFloat(), false)
                }
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                if (pdfView!!.isSwipeVertical) {
                    setPosition(event.rawY - currentPos + relativeHandlerMiddle)
                    pdfView!!.setPositionOffset(relativeHandlerMiddle / height.toFloat(), false)
                } else {
                    setPosition(event.rawX - currentPos + relativeHandlerMiddle)
                    pdfView!!.setPositionOffset(relativeHandlerMiddle / width.toFloat(), false)
                }
                return true
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                hideDelayed()
                pdfView!!.performPageSnap()
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    companion object {
        private const val HANDLE_WIDTH = 50
        private const val HANDLE_HEIGHT = 30
        private const val DEFAULT_TEXT_SIZE = 16
    }

    init {
        textView = TextView(context)
        visibility = INVISIBLE
        textView.setTextColor(Color.WHITE)
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_TEXT_SIZE.toFloat())
    }
}