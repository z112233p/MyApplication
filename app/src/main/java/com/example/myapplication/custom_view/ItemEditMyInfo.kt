package com.example.myapplication.custom_view

import android.content.Context
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.myapplication.R
import kotlinx.android.synthetic.main.item_edit_my_info.view.*

@Suppress("DEPRECATION")
class ItemEditMyInfo(context: Context?) : ConstraintLayout(context) {

    init {
        View.inflate(context, R.layout.item_edit_my_info, this)
        this.setOnClickListener {
            Log.e("Peter", "MenuItemLayoutQ setOnClickListener:  ")
        }

    }


    fun setItemName(name: String){
        tv_title.text = name
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return super.onInterceptTouchEvent(ev)
    }
}
