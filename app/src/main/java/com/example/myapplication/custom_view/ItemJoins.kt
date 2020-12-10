package com.example.myapplication.custom_view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.example.myapplication.BuildConfig
import com.example.myapplication.R
import com.example.myapplication.tools.ImgHelper
import kotlinx.android.synthetic.main.fragment_event_main_v2.*
import kotlinx.android.synthetic.main.item_joins.view.*

class ItemJoins(context: Context?) : LinearLayout(context) {

    init {
        View.inflate(context, R.layout.item_joins, this)
    }

    fun setJoinsPhoto(url: String){
        ImgHelper.loadNormalImg(context, url, iv_joins)
    }
}