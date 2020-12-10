package com.example.myapplication.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.view.ViewCompat.offsetTopAndBottom
import com.example.myapplication.R
import com.example.myapplication.custom_view.MenuItemLayout
import kotlinx.android.synthetic.main.dialog_event_detail_option.*
import kotlinx.android.synthetic.main.item_anim_view_btn.view.*
import kotlinx.android.synthetic.main.layout_personal_data.view.*


@Suppress("UNREACHABLE_CODE")
class DialogEventDetailOption(context: Context) : Dialog(context, R.style.FullScreenDialogStyle) {

    private var lastX = 0
    private var lastY = 0
    private var upMoveTotal = 0
    private var downMoveTotal = 0

    private lateinit var dialogWindow : Window
    private lateinit var dialogWindowManager: WindowManager.LayoutParams
    private lateinit var seeDetailOnclick: View.OnClickListener
    private lateinit var goChatOnclick: View.OnClickListener


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_event_detail_option)
        setWindowTransparent()
        tv_see_detail.tv_btn_text.text = "出席名單"
        tv_event_status.tv_btn_text.text = "前往聊天"
        tv_score.tv_btn_text.text = "檢舉活動"
        tv_report.tv_btn_text.text = "給予評價"


        tv_see_detail.iv_btn_img.setImageDrawable(context.resources.getDrawable(R.mipmap.ic_eye))
        tv_event_status.iv_btn_img.setImageDrawable(context.resources.getDrawable(R.mipmap.ic_chat_white))
        tv_score.iv_btn_img.setImageDrawable(context.resources.getDrawable(R.mipmap.ic_report))
        tv_report.iv_btn_img.setImageDrawable(context.resources.getDrawable(R.mipmap.ic_score_start))

        tv_see_detail.setOnClickListener(seeDetailOnclick)
        tv_event_status.setOnClickListener(goChatOnclick)


        dialog_main.setOnClickListener {
            Log.e("Peter"," dialog_main.setOnClickListener  dismiss")

            dismiss()
        }

        main.setOnTouchListener { p0, event -> //        return super.onTouchEvent(event)
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

                    if(downMoveTotal > -200){
                        offsetTopAndBottom(p0!!, offY)
                        upMoveTotal += offY
                    }
                }
                MotionEvent.ACTION_UP -> {
                    Log.e("Peter","DialogChatRoomMenu  ACTION_UP        $downMoveTotal     $upMoveTotal")

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

    fun setSeeDetailOnclick(onClick: View.OnClickListener){
        seeDetailOnclick = onClick
    }

    fun setGoChatOnclick(onClick: View.OnClickListener){
        goChatOnclick = onClick
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
