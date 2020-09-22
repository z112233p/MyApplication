package com.example.myapplication.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import com.davemorrissey.labs.subscaleview.ImageSource
import com.example.myapplication.MyApp
import com.example.myapplication.R
import com.example.myapplication.datamodle.chat_room.Message
import com.example.myapplication.tools.ImgHelper
import kotlinx.android.synthetic.main.dialog_full_screen_image.*

class DialogFullScreenImage(
    context: Context,
    private val message: Message
) : Dialog(context,R.style.MyFullScreenDialog){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_full_screen_image)
//        setWindowTransparent()
        init()
    }

    private fun setWindowTransparent(){
        val window : Window?= window
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    }

    private fun init(){
        Log.e("Peter","DialogFullScreenImage    "+message.imageUrl)
        tv_close.setOnClickListener(onClick)
        ImgHelper.loadNormalImg(context, message.imageUrl, iv_photo)

//        iv_photo.setImage(ImageSource.uri(message.imageUrl.toString()))
    }

    private var onClick : View.OnClickListener ?= View.OnClickListener {
        when (it.id) {
            R.id.tv_close -> dismiss()
        }
    }
}