package com.illa.joliveapp.custom_view

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.illa.joliveapp.R
import com.illa.joliveapp.datamodle.event.detailv2.EventDetailV2
import com.illa.joliveapp.tools.IntentHelper
import kotlinx.android.synthetic.main.item_anim_view_btn.view.*
import kotlinx.android.synthetic.main.layout_event_data.view.*
import kotlinx.android.synthetic.main.layout_personal_data.view.*
import kotlinx.android.synthetic.main.layout_personal_data.view.tv_event_status
import kotlinx.android.synthetic.main.layout_personal_data.view.tv_report
import kotlinx.android.synthetic.main.layout_personal_data.view.tv_score
import kotlinx.android.synthetic.main.layout_personal_data.view.tv_see_detail

@Suppress("DEPRECATION")
class EventDataLayout(context: Context) : ConstraintLayout(context) {

    private lateinit var data: EventDetailV2

    init {
        View.inflate(context, R.layout.layout_event_data, this)

        tv_see_detail.tv_btn_text.text = "導航"
        tv_event_status.tv_btn_text.text = "查看活動"
        tv_score.tv_btn_text.text = "評價"
        tv_report.tv_btn_text.text = "檢舉"

        tv_see_detail.iv_btn_img.setImageDrawable(context.resources.getDrawable(R.mipmap.ic_event_detail_location))
        tv_event_status.iv_btn_img.setImageDrawable(context.resources.getDrawable(R.mipmap.ic_event))
        tv_score.iv_btn_img.setImageDrawable(context.resources.getDrawable(R.mipmap.ic_score_start))
        tv_report.iv_btn_img.setImageDrawable(context.resources.getDrawable(R.mipmap.ic_report))

        tv_see_detail.setOnClickListener {
            val myIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.google.com/maps/search/?api=1&query="+data.data.location_gps_latitude+","+data.data.location_gps_longitude))
            context.startActivity(myIntent)
        }

        tv_event_status.setOnClickListener {
            IntentHelper.gotoEventDetailActivity(context, data.data.label, false)
        }
    }

    fun setData(it: EventDetailV2) {
        data = it
        tv_event_time.text = it.data.start_time
        tv_event_location.text = it.data.location_title
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return super.onInterceptTouchEvent(ev)
    }
}
