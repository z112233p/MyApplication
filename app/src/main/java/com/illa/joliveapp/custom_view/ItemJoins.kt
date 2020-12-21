package com.illa.joliveapp.custom_view

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import com.illa.joliveapp.R
import com.illa.joliveapp.tools.ImgHelper
import kotlinx.android.synthetic.main.item_joins.view.*

class ItemJoins(context: Context?) : LinearLayout(context) {

    init {
        View.inflate(context, R.layout.item_joins, this)
    }

    fun setJoinsPhoto(url: String){
        ImgHelper.loadNormalImg(context, url, iv_joins)
    }
}