package com.illa.joliveapp.controller

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.illa.joliveapp.R
import kotlin.collections.ArrayList

class BannerController (private val context: Context,
                        private val listSize: Int, dealPager: ViewPager?,
                        private val dotsLayout: LinearLayout?) {

    private var currentPosition = 0
    private val handler: Handler
    private var runnable: Runnable? = null
    private var isAutoPlay = false

    init {
        val handlerThread = HandlerThread("background-handler")
        handlerThread.start()
        val looper = handlerThread.looper
        handler = Handler(looper)
        dealPager?.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                var position = position
                Log.d("SWIPECHECK", "onPageSelected ==$position")
                //If Swipe too much and the gap between pages  is too high when doing runnable will cause ANR
                //取position除listSize的餘数就是當前廣告小白點的位置
                prepareDots(position % listSize)
                currentPosition = ++position
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        prepareDots(0)
        initRunnable(dealPager)
        startSwipe()
    }

    fun setImageList(imageResourceList: ArrayList<Int>) {

    }

    fun startSwipe() {
        Log.d("SWIPECHECK", "START")
        isAutoPlay = true
        handler.removeCallbacks(runnable!!)
        handler.postDelayed(runnable!!, 5000)
    }

    fun stopSwipe() {
        Log.d("SWIPECHECK", "STOP")
        if (isAutoPlay) {
            isAutoPlay = false
            handler.removeCallbacks(runnable!!)
        }
    }

    fun stopLooper() {
        handler.removeCallbacks(runnable!!)
        handler.looper.quit()
    }

    private fun initRunnable(dealPager: ViewPager?) {
        isAutoPlay = true
        runnable = object : Runnable {
            override fun run() {
                Log.d("SWIPECHECK", "run ==$isAutoPlay")
                if (isAutoPlay) {
                    if (currentPosition == Int.MAX_VALUE) {
                        currentPosition = 0
                    }
                    (context as Activity).runOnUiThread {
                        dealPager!!.setCurrentItem(
                            currentPosition++,
                            true
                        )
                    }
                    handler.postDelayed(this, 3000)
                }
            }
        }
    }

    private fun prepareDots(currentPosition: Int) {
        if (dotsLayout == null) {
            return
        }
        if (dotsLayout.childCount > 0) {
            dotsLayout.removeAllViews()
        }
        val dots = ArrayList<ImageView>()
        for (i in 0 until listSize) {
            dots.add(ImageView(context))
            if (i == currentPosition) {
                dots[i].setImageResource(R.drawable.ic_active_dot)
            } else {
                dots[i].setImageResource(R.drawable.ic_inactive_dot)
            }
            val layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            layoutParams.setMargins(0, 0, 30, 0)
            dotsLayout.addView(dots[i], layoutParams)
        }
    }

}