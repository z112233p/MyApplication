package com.illa.joliveapp.custom_view

import android.content.Context
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.illa.joliveapp.R
import kotlinx.android.synthetic.main.item_anim_view_btn.view.*
import kotlinx.android.synthetic.main.layout_personal_data.view.*

@Suppress("DEPRECATION")
class PersonalDataLayout(context: Context) : ConstraintLayout(context) {

    init {
        View.inflate(context, R.layout.layout_personal_data, this)

        tv_see_detail.tv_btn_text.text = "查看主揪"
        tv_event_status.tv_btn_text.text = "活動動態"
        tv_score.tv_btn_text.text = "評價"
        tv_report.tv_btn_text.text = "檢舉"

        tv_see_detail.iv_btn_img.setImageDrawable(context.resources.getDrawable(R.mipmap.ic_eye))
        tv_event_status.iv_btn_img.setImageDrawable(context.resources.getDrawable(R.mipmap.ic_event))
        tv_score.iv_btn_img.setImageDrawable(context.resources.getDrawable(R.mipmap.ic_score_start))
        tv_report.iv_btn_img.setImageDrawable(context.resources.getDrawable(R.mipmap.ic_report))

    }



    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return super.onInterceptTouchEvent(ev)
    }
}
