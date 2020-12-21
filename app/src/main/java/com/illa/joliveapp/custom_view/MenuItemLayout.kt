package com.illa.joliveapp.custom_view

import android.content.Context
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.illa.joliveapp.R
import kotlinx.android.synthetic.main.item_menu.view.*

@Suppress("DEPRECATION")
class MenuItemLayout(context: Context?) : ConstraintLayout(context) {

    init {
        View.inflate(context, R.layout.item_menu, this)
        this.setOnClickListener {
            Log.e("Peter", "MenuItemLayoutQ setOnClickListener:  ")
        }
        iv_icon.setOnClickListener {
            Log.e("Peter", "MenuItemLayoutQ iv_icon.setOnClickListen:  ")

        }
    }

    fun setIcon(resID: Int){
        iv_icon.setImageDrawable(context.resources.getDrawable(resID))
    }

    fun setItemName(name: String){
        tv_item_name.text = name
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return super.onInterceptTouchEvent(ev)
    }
}
