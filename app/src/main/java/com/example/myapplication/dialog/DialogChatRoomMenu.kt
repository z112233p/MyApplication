package com.example.myapplication.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import androidx.core.view.ViewCompat.offsetLeftAndRight
import androidx.core.view.ViewCompat.offsetTopAndBottom
import com.example.myapplication.R
import com.example.myapplication.custom_view.MenuItemLayout
import kotlinx.android.synthetic.main.dialog_chat_room_menu.*


@Suppress("UNREACHABLE_CODE")
class DialogChatRoomMenu(context: Context) : Dialog(context, R.style.FullScreenDialogStyle) {

    private var lastX = 0
    private var lastY = 0
    private var upMoveTotal = 0
    private var downMoveTotal = 0

    private lateinit var dialogWindow : Window
    private lateinit var dialogWindowManager: WindowManager.LayoutParams


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_chat_room_menu)
        setWindowTransparent()
//        ll_main.addView(LinearLayout(context).apply {
//            this.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,200)
//            this.setBackgroundColor(context.resources.getColor(R.color.black))
//        })
        val itemNames = context.resources.getStringArray(R.array.chat_room_menu)
        setMenuItems(itemNames)

        dialog_main.setOnClickListener {
        dismiss()
        }

        ll_main.setOnTouchListener { p0, event -> //        return super.onTouchEvent(event)
            val x = event.x.toInt()
            val y = event.y.toInt()

            when(event.action){
                MotionEvent.ACTION_DOWN -> {
                    Log.e("Peter","DialogChatRoomMenu  ACTION_DOWN")
                    lastX = x;
                    lastY = y;
                }
                MotionEvent.ACTION_MOVE -> {
                    val offY = y - lastY
                    downMoveTotal+= offY


                    Log.e("Peter","DialogChatRoomMenu  ACTION_DOWN    $offY     $downMoveTotal     ${ll_main.measuredHeight}    $y   $lastY")
                    //                        offsetTopAndBottom(p0!!, offY)
                    //                        offsetTopAndBottom(p0!!, offY)
                    if(downMoveTotal > -200){
                        offsetTopAndBottom(p0!!, offY)
                        upMoveTotal += offY
                    }
                    //                        if(offY >= 0 ){
                    //                            offsetTopAndBottom(p0!!, offY)
                    //
                    //                        } else if(downMoveTotal < 0 && downMoveTotal > -200){
                    ////                            dialogWindowManager.height = ll_main.measuredHeight-offY
                    ////                            dialogWindow.attributes = dialogWindowManager
                    //                            offsetTopAndBottom(p0!!, offY)
                    //
                    //                        }

                }
                MotionEvent.ACTION_UP -> {
                    Log.e("Peter","DialogChatRoomMenu  ACTION_UP        $downMoveTotal     $upMoveTotal")
                    //                        dialogWindow.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

                    if(downMoveTotal >= (ll_main.measuredHeight/2)){
                        dismiss()
                    } else if(downMoveTotal>0){
                        offsetTopAndBottom(p0!!, -downMoveTotal)
                    } else {
                        if(downMoveTotal < -200){
                            offsetTopAndBottom(p0!!, -upMoveTotal)

                        } else {
                            offsetTopAndBottom(p0!!, -upMoveTotal)

                        }

                    }
                    upMoveTotal = 0
                    downMoveTotal = 0

                }
            }
            true
        }

    }


    private fun setMenuItems(itemNames: Array<String>) {
        itemNames.forEach { name ->
            ll_main.addView(MenuItemLayout(context).apply {
                setIcon(R.drawable.ic_send)
                setItemName(name)
            })
        }
    }

    private fun setWindowTransparent(){
        dialogWindow = window!!
        dialogWindowManager = dialogWindow.attributes
        dialogWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCanceledOnTouchOutside(true)
        dialogWindow.setGravity(Gravity.BOTTOM)
        dialogWindow.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        //因为我的dialog背景图片是圆弧型，不设置背景透明的话圆弧处显示黑色
        dialogWindow.setBackgroundDrawableResource(android.R.color.transparent)
    }

    override fun show() {
        super.show()
        dialogWindow.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

}
