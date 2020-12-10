package com.example.myapplication.custom_view

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.myapplication.R
import kotlinx.android.synthetic.main.item_profile_item.view.*


@Suppress("DEPRECATION")
class ItemJobView(context: Context?) : ConstraintLayout(context) {

    private var isSelected: Boolean? = false
    private var selectedColor = "#ffffff"
    private var id: Int? = 0

    init {
        View.inflate(context, R.layout.item_profile_item, this)

    }


    fun setSelectedColor(color: String){
        selectedColor = color
    }

    fun unselected(){
        tv_item_name.background = context.resources.getDrawable(R.drawable.bg_profile_item)
        isSelected = false
    }

    fun selected(){
        tv_item_name.background = context.resources.getDrawable(R.drawable.bg_profile_item_selected)
//        val gd = GradientDrawable(
//            GradientDrawable.Orientation.LEFT_RIGHT, intArrayOf(Color.parseColor(selectedColor), Color.parseColor("#d18412"))
//        )
//        gd.cornerRadius = 0f
//        tv_item_name.setBackgroundDrawable(gd)
        isSelected = true
    }

    fun setItemId(id: Int?){
        this.id = id
    }

    fun getItemId() : Int?{
        return id
    }

    fun setItemName(name: String){
        tv_item_name.text = name
    }

}
