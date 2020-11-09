package com.example.myapplication.custom_view

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.MotionEvent
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.core.view.ViewCompat
import kotlin.math.abs


@Suppress("DEPRECATION", "UNREACHABLE_CODE")
class MenuItemLayout3(context: Context) : FrameLayout(context) {
    private var lastY = 0
    private var upMoveTotal = 0
    private var downMoveTotal = 0
    private var callbacks: AnimListener = object :AnimListener{
        override fun closeView(upMoveTotal: Int) {
            Log.e("Peter","AnimListener default")
        }

        override fun setAnimAlpha(offY: Int) {
            Log.e("Peter","AnimListener default")
        }
    }

    init {
//        alpha = 0.8F
    }
    interface AnimListener{
        fun closeView(upMoveTotal: Int)
        fun setAnimAlpha(offY: Int)
    }

    fun setCallBack(callbacks: AnimListener){
        this.callbacks = callbacks
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        Log.e("Peter","DialogChatRoomMenu2 MotionEvent  onTouchEventonTouchEventonTouchEvent")
        val y = event.y.toInt()

        when(event.action){
            MotionEvent.ACTION_DOWN -> {
                Log.e("Peter","DialogChatRoomMenu2 ACTION_DOWN")

                lastY = y
            }
            MotionEvent.ACTION_MOVE -> {
                val offY = y - lastY
                downMoveTotal+= offY

                Log.e("Peter","DialogChatRoomMenu2 MotionEvent  ACTION_MOVE   ${this.measuredHeight}   $offY     $downMoveTotal     $y   $lastY")

                if(downMoveTotal > -250){
                    ViewCompat.offsetTopAndBottom(this, offY)
                    upMoveTotal += offY
                    val params = this.layoutParams as LinearLayout.LayoutParams
                    params.topMargin += offY
                    this.setLayoutParams(params)
                }

                if(downMoveTotal >= 0){
                    callbacks.setAnimAlpha(offY)

                }
//                val params = this.layoutParams as LinearLayout.LayoutParams
//
//                params.topMargin += offY
//                this.setLayoutParams(params)

            }
            MotionEvent.ACTION_UP -> {
//                Log.e("Peter","DialogChatRoomMenu2  ACTION_UP        $downMoveTotal     $upMoveTotal")
                val params = this.layoutParams as LinearLayout.LayoutParams

                if(downMoveTotal >= (this.measuredHeight / 3)){
                    callbacks.closeView(upMoveTotal)
                    params.topMargin += -upMoveTotal
                    this.setLayoutParams(params)

                } else if(downMoveTotal > 0){
                    Log.e("Peter","DialogChatRoomMenu2  ACTION_UP        $downMoveTotal     $upMoveTotal")

                    ViewCompat.offsetTopAndBottom(this, -downMoveTotal)
                    params.topMargin += -downMoveTotal
                    this.setLayoutParams(params)
                } else {
                    ViewCompat.offsetTopAndBottom(this, -upMoveTotal)
                    params.topMargin += -upMoveTotal
                    this.setLayoutParams(params)
                }
                Log.e("Peter","DialogChatRoomMenu2  ACTION_UP22     $upMoveTotal")

                if(upMoveTotal >= 0){
                    callbacks.setAnimAlpha(-upMoveTotal)

                }

                upMoveTotal = 0
                downMoveTotal = 0
            }
        }
        return true
    }
    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val y = ev.y.toInt()
        Log.e("Peter","abs(DialogChatRoomMenu2) onInterceptTouchEvent")

        return when(ev.action){
            MotionEvent.ACTION_DOWN -> {
                lastY = y
                false

            }
            MotionEvent.ACTION_MOVE -> {
                val offY = y - lastY
                Log.e("Peter","abs(offY)2    ${abs(offY)}")
                abs(offY) > 15
            }
            else -> false
        }

    }

}
