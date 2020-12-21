package com.illa.joliveapp.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import com.illa.joliveapp.R
import kotlinx.android.synthetic.main.dialog_choose_gender.*

class DialogChooseGender(context: Context) : Dialog(context) {
    private lateinit var dialogWindow : Window
    private lateinit var dialogWindowManager: WindowManager.LayoutParams

    private var onClickListener: OnItemClickListener = object : OnItemClickListener{
        override fun onFemaleClick(gender: String) {
            Log.e("Peter","onFemaleClick")
        }

        override fun onMaleClick(gender: String) {
            Log.e("Peter","onMaleClick")
        }


    }

    interface OnItemClickListener {
        fun onFemaleClick(gender: String)
        fun onMaleClick(gender: String)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.dialog_choose_gender)
        setWindowTransparent()
        tv_female.setOnClickListener(onClick)
        tv_male.setOnClickListener(onClick)
    }

    private fun setWindowTransparent(){
        dialogWindow = window!!
        dialogWindowManager = dialogWindow.attributes
        dialogWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCanceledOnTouchOutside(true)
//        dialogWindow.setGravity(Gravity.CENTER)
//        dialogWindow.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        //因为我的dialog背景图片是圆弧型，不设置背景透明的话圆弧处显示黑色
        dialogWindow.setBackgroundDrawableResource(android.R.color.transparent)
    }

    private var onClick = View.OnClickListener {
        when(it.id){
            R.id.tv_female -> {
                onClickListener.onFemaleClick(tv_female.text.toString())
                dismiss()
            }
            R.id.tv_male -> {
                onClickListener.onFemaleClick(tv_male.text.toString())
                dismiss()
            }
        }
    }

    fun setOnItemClick(param: OnItemClickListener){
        this.onClickListener = param
    }


}