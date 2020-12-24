package com.illa.joliveapp.custom_view

import android.content.Context
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.illa.joliveapp.BuildConfig
import com.illa.joliveapp.R
import com.illa.joliveapp.datamodle.profile.user_info.User
import com.illa.joliveapp.datamodle.profile.user_info.UserInfo
import com.illa.joliveapp.tools.ImgHelper
import com.illa.joliveapp.tools.IntentHelper
import com.illa.joliveapp.tools.Tools
import kotlinx.android.synthetic.main.item_anim_view_btn.view.*
import kotlinx.android.synthetic.main.layout_personal_data.view.*

@Suppress("DEPRECATION")
class PersonalDataLayout(context: Context) : ConstraintLayout(context) {

    private lateinit var userInfo: UserInfo
    private var label = ""

    init {
        View.inflate(context, R.layout.layout_personal_data, this)

        tv_see_detail.tv_btn_text.text = "查看檔案"
        tv_event_status.tv_btn_text.text = "活動瀏覽"
        tv_score.tv_btn_text.text = "評價"
        tv_report.tv_btn_text.text = "檢舉"

        tv_see_detail.iv_btn_img.setImageDrawable(context.resources.getDrawable(R.mipmap.ic_eye))
        tv_event_status.iv_btn_img.setImageDrawable(context.resources.getDrawable(R.mipmap.ic_event))
        tv_score.iv_btn_img.setImageDrawable(context.resources.getDrawable(R.mipmap.ic_score_start))
        tv_report.iv_btn_img.setImageDrawable(context.resources.getDrawable(R.mipmap.ic_report))

        tv_see_detail.setOnClickListener {
            Log.e("Peter","personalLayout personalLayout   ${userInfo.data.user.label}")
            IntentHelper.gotoMyInfoActivity(context, label)
        }
        tv_event_status.setOnClickListener {
            IntentHelper.gotoMyInfoActivity(context, label, 1)
        }
    }

    private var onClick = OnClickListener {
        TODO("Not yet implemented")
    }

    fun setLabel(label: String){
        this.label = label

    }

    fun setData(it: UserInfo) {
        Log.e("Peter","personalLayout setData   $it")

        userInfo = it
        tv_user_name.text = it.data.user.nickname
        ImgHelper.loadNormalImg(context, BuildConfig.IMAGE_URL + it.data.user.photos!![0].url, iv_user_photo)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return super.onInterceptTouchEvent(ev)
    }
}
