package com.example.myapplication.custom_view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.myapplication.R
import com.example.myapplication.adapter.Adapter_Events
import kotlinx.android.synthetic.main.layout_input_rv.view.*


class HorizontalItemLayout(mContext: Context?, attrs: AttributeSet?) : LinearLayout(
    mContext, attrs) {

    init {
        View.inflate(context, R.layout.layout_input_rv, this)
        val layoutManager =  LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
        rv_events.layoutManager = layoutManager
    }

    fun getRecycleView(): RecyclerView{
        return rv_events
    }

    fun setTitle(title: String){
        tv_category_type.text = title
    }

    fun setShowMoreOnclick(onClickListener: OnClickListener){
        tv_show_more.setOnClickListener(onClickListener)
    }


}