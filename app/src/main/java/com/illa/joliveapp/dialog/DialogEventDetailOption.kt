package com.illa.joliveapp.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.view.ViewCompat.offsetTopAndBottom
import com.illa.joliveapp.R
import com.illa.joliveapp.datamodle.event.detailv2.Data
import kotlinx.android.synthetic.main.dialog_event_detail_option.*
import kotlinx.android.synthetic.main.item_anim_view_btn.view.*
import java.text.SimpleDateFormat
import java.util.*


@Suppress("UNREACHABLE_CODE", "RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class DialogEventDetailOption(
    context: Context,
    joinType: Any?,
    isOwner: Boolean,
    eventDetailData: Data
) : Dialog(context, R.style.FullScreenDialogStyle) {

    private var lastX = 0
    private var lastY = 0
    private var upMoveTotal = 0
    private var downMoveTotal = 0
    private var joinType: Any = 0
    private var isOwner = false
    private var nowTime =  Date()
    private var diffTime = 0
    private var dateSdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")


    private lateinit var eventDetailData : Data
    private lateinit var dialogWindow : Window
    private lateinit var dialogWindowManager: WindowManager.LayoutParams
    private lateinit var seeDetailOnclick: View.OnClickListener
    private lateinit var goChatOnclick: View.OnClickListener
    private lateinit var reportOnclick: View.OnClickListener
    private lateinit var closeEventClick: View.OnClickListener

    init {
        if (joinType != null) {
            this.joinType = joinType
        }
        this.isOwner = isOwner
        this.eventDetailData = eventDetailData
        diffTime = (dateSdf.parse(eventDetailData.start_time).time - nowTime.time).toInt()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_event_detail_option)
        setWindowTransparent()
        if(isOwner){
            tv_see_detail.tv_btn_text.text = "報名審核"

        } else {
            tv_see_detail.tv_btn_text.text = "出席名單"

        }
        tv_event_status.tv_btn_text.text = "前往聊天"
        tv_report.tv_btn_text.text = "刪除活動"

        if(eventDetailData.is_full_join == 0){
            tv_score.tv_btn_text.text = "宣告滿團"
            tv_score.iv_btn_img.setImageDrawable(context.resources.getDrawable(R.mipmap.ic_white_fire))

        } else {
            tv_score.tv_btn_text.text = "重啟報名"
            tv_score.iv_btn_img.setImageDrawable(context.resources.getDrawable(R.mipmap.ic_reset))

        }

        tv_see_detail.iv_btn_img.setImageDrawable(context.resources.getDrawable(R.mipmap.ic_eye))
        tv_event_status.iv_btn_img.setImageDrawable(context.resources.getDrawable(R.mipmap.ic_chat_white))
        tv_report.iv_btn_img.setImageDrawable(context.resources.getDrawable(R.mipmap.ic_close))

        tv_report.setOnClickListener(closeEventClick)
        tv_score.setOnClickListener(reportOnclick)
        tv_see_detail.setOnClickListener(seeDetailOnclick)
        tv_event_status.setOnClickListener(goChatOnclick)

        Log.e("Peter","DialogEventDetailOption  joinType   $joinType")

        if(joinType != 9.0){
            tv_report.alpha = 0.2F
            tv_report.isClickable = false
            tv_score.alpha = 0.2F
            tv_score.isClickable = false
        } else {
//            tv_score.alpha = 0.2F
//            tv_score.isClickable = false
            if(diffTime > 0){
                if(eventDetailData.is_need_approved == 0){
                    tv_score.alpha = 0.2F
                    tv_score.isClickable = false
                }

            } else {
                tv_score.alpha = 0.2F
                tv_score.isClickable = false
            }
        }

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

    fun setReportOnclick(onClick: View.OnClickListener){
        reportOnclick = onClick
    }

    fun setCloseEventClick(onClick: View.OnClickListener){
        closeEventClick = onClick
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
