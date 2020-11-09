package com.example.myapplication.custom_view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.myapplication.R
import com.example.myapplication.datamodle.chat_room.Message
import com.google.android.material.circularreveal.CircularRevealCompat
import com.google.android.material.circularreveal.cardview.CircularRevealCardView
import com.labo.kaji.relativepopupwindow.RelativePopupWindow
import kotlin.math.hypot
import kotlin.math.max

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class CardPopup(
    context: Context,
    isTop: Boolean,
    message: Message
) : RelativePopupWindow(context) {

    interface onCopyButtonClickListener{
        fun onCopyButtonClick(message: Message)
    }

    init {
        @SuppressLint("InflateParams")
        if (isTop){
            contentView = LayoutInflater.from(context).inflate(R.layout.pop_card_down, null)
        } else {
            contentView = LayoutInflater.from(context).inflate(R.layout.pop_card_up, null)
        }
        width = ViewGroup.LayoutParams.WRAP_CONTENT
        height = ViewGroup.LayoutParams.WRAP_CONTENT
        isFocusable = true
        isOutsideTouchable = true
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // Disable default animation for circular reveal
        animationStyle = 0
        contentView.findViewById<TextView>(R.id.tv_reply).setOnClickListener {
            copyListener?.onCopyButtonClick(message)
        }
    }

    private var copyListener :onCopyButtonClickListener?= object :onCopyButtonClickListener{
        override fun onCopyButtonClick(message: Message) {
            Log.e("Peter","onCopyButtonClick")
        }

    }

    public fun setCopyButtonListener(callback: onCopyButtonClickListener){
        copyListener = callback
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun showOnAnchor(anchor: View, vertPos: Int, horizPos: Int, x: Int, y: Int, fitInScreen: Boolean) {
        super.showOnAnchor(anchor, vertPos, horizPos, x, y, fitInScreen)
//        circularReveal(anchor)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun circularReveal(anchor: View) {
        (contentView as CircularRevealCardView).run {
            post {
                val myLocation = IntArray(2).apply { getLocationOnScreen(this) }
                val anchorLocation = IntArray(2).apply { anchor.getLocationOnScreen(this) }
                val cx = anchorLocation[0] - myLocation[0] + anchor.width/2
                val cy = anchorLocation[1] - myLocation[1] + anchor.height/2
                val windowRect = Rect().apply { getWindowVisibleDisplayFrame(this) }

                measure(
                    makeDropDownMeasureSpec(this@CardPopup.width, windowRect.width()),
                    makeDropDownMeasureSpec(this@CardPopup.height, windowRect.height())
                )
                val dx = max(cx, measuredWidth - cx)
                val dy = max(cy, measuredHeight - cy)
                val finalRadius = hypot(dx.toFloat(), dy.toFloat())
                CircularRevealCompat.createCircularReveal(this, cx.toFloat(), cy.toFloat(), 0f, finalRadius).run {
                    duration = 500
                    start()
                }
            }
        }
    }

    companion object {
        private fun makeDropDownMeasureSpec(measureSpec: Int, maxSize: Int): Int {
            return View.MeasureSpec.makeMeasureSpec(
                getDropDownMeasureSpecSize(measureSpec, maxSize),
                getDropDownMeasureSpecMode(measureSpec)
            )
        }

        private fun getDropDownMeasureSpecSize(measureSpec: Int, maxSize: Int): Int {
            return when (measureSpec) {
                ViewGroup.LayoutParams.MATCH_PARENT -> maxSize
                else -> View.MeasureSpec.getSize(measureSpec)
            }
        }

        private fun getDropDownMeasureSpecMode(measureSpec: Int): Int {
            return when (measureSpec) {
                ViewGroup.LayoutParams.WRAP_CONTENT -> View.MeasureSpec.UNSPECIFIED
                else -> View.MeasureSpec.EXACTLY
            }
        }
    }
}