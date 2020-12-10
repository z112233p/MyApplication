package com.example.myapplication.custom_view

import android.content.Context
import android.graphics.BlurMaskFilter
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.myapplication.R
import kotlinx.android.synthetic.main.item_location.view.*
import kotlinx.android.synthetic.main.item_profile_item.view.*


@Suppress("DEPRECATION")
class ItemInterestView(context: Context?) : ConstraintLayout(context) {

    private var isSelected: Boolean? = false
    private var selectedColor = "#ffffff"
    private var id: Int? = 0
    private lateinit var onClick : ItemInterestView.onItemClick

    interface onItemClick{
        fun onClick()
    }

    init {
        View.inflate(context, R.layout.item_profile_item, this)
        this.setOnClickListener {

            Log.e("Peter", "MenuItemLayoutQ setOnClickListener:  ")
            if (context != null) {
                if(this.isSelected!!){
                    tv_item_name.background = context.resources.getDrawable(R.drawable.bg_profile_item)

                    isSelected = false

                } else {
                    tv_item_name.background = context.resources.getDrawable(R.drawable.bg_profile_item_selected)
                    val gd = GradientDrawable(
                        GradientDrawable.Orientation.LEFT_RIGHT, intArrayOf(Color.parseColor("#"+selectedColor), Color.parseColor("#d18412"))
                    )
                    gd.cornerRadius = 0f
                    tv_item_name.setBackgroundDrawable(gd)

                    isSelected = true
                }
                onClick.onClick()

            }
        }
    }

    fun setItemOnclick(onClick : ItemInterestView.onItemClick){
        this.onClick = onClick
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
        val gd = GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT, intArrayOf(Color.parseColor("#$selectedColor"), Color.parseColor("#d18412"))
        )
        gd.cornerRadius = 0f
        tv_item_name.setBackgroundDrawable(gd)
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

    fun getIsSelected(): Boolean?{
        return isSelected
    }

}
