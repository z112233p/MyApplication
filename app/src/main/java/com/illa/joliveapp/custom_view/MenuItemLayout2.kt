package com.illa.joliveapp.custom_view

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.MotionEvent
import android.widget.LinearLayout
import androidx.core.view.ViewCompat
import kotlin.math.abs


@Suppress("DEPRECATION", "UNREACHABLE_CODE")
class MenuItemLayout2(context: Context?) : LinearLayout(context) {
    private var lastY = 0
    private var upMoveTotal = 0
    private var downMoveTotal = 0
    private var callbacks: AnimListener = object :AnimListener{
        override fun closeView(upMoveTotal: Int) {
            Log.e("Peter","AnimListener default")
        }

    }

    init {
        alpha = 0.8F
    }
    interface AnimListener{
        fun closeView(upMoveTotal: Int)
    }

    fun setCallBack(callbacks: AnimListener){
        this.callbacks = callbacks
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        Log.e("Peter","DialogChatRoomMenu MotionEvent  onTouchEventonTouchEventonTouchEvent")
        val y = event.y.toInt()

        when(event.action){
            MotionEvent.ACTION_DOWN -> {

                lastY = y
            }
            MotionEvent.ACTION_MOVE -> {
                val offY = y - lastY
                downMoveTotal+= offY

                Log.e("Peter","DialogChatRoomMenu MotionEvent  ACTION_MOVE    $offY     $downMoveTotal     $y   $lastY")

                if(downMoveTotal > -1500){
                    ViewCompat.offsetTopAndBottom(this, offY)
                    upMoveTotal += offY
                }

            }
            MotionEvent.ACTION_UP -> {
                Log.e("Peter","DialogChatRoomMenu  ACTION_UP        $downMoveTotal     $upMoveTotal")

                if(downMoveTotal >= (this.measuredHeight / 3)){
                    callbacks.closeView(upMoveTotal)

                } else if(downMoveTotal > 0){
                    ViewCompat.offsetTopAndBottom(this, -downMoveTotal)
                } else {
                    ViewCompat.offsetTopAndBottom(this, -upMoveTotal)
                }
                upMoveTotal = 0
                downMoveTotal = 0
            }
        }
        return true
    }
    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val y = ev.y.toInt()

        return when(ev.action){
            MotionEvent.ACTION_DOWN -> {
                lastY = y
                false

            }
            MotionEvent.ACTION_MOVE -> {
                val offY = y - lastY
                Log.e("Peter","abs(offY)    ${abs(offY)}")
                abs(offY) > 15
            }
            else -> false
        }

    }

}
