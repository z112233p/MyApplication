package me.illa.jolive.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.illa.joliveapp.BuildConfig
import com.illa.joliveapp.R
import com.illa.joliveapp.datamodle.notice.template.Data
import com.illa.joliveapp.tools.ImgHelper
import com.illa.joliveapp.tools.IntentHelper

import com.stfalcon.chatkit.utils.DateFormatter
import java.text.SimpleDateFormat

class Adapter_Notice() :RecyclerView.Adapter<Adapter_Notice.ViewHolder>() {
    private var dataList: MutableList<com.illa.joliveapp.datamodle.notice.notice_data.Data> = ArrayList()
    private var templateList: MutableList<Data> = ArrayList()
    private lateinit var mContext: Context
    private var mOnItemClickListener: OnItemClickListener? = null
    private var hasData = false
    private var hasTemplate = false

    private var customDateFormat: SimpleDateFormat = SimpleDateFormat("MM月dd日")
    private var customTodayFormat: SimpleDateFormat = SimpleDateFormat("a hh:mm")
    private var sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")


    constructor(context: Context?) : this(){
        if (context != null) {
            mContext = context
        }
    }

    interface OnItemClickListener {
        fun onItemClick(
            view: View?,
            position: Int,
            noticeId: String,
            template: String,
            link: String
        )
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mOnItemClickListener = listener
    }

    fun setData(dealData: List<com.illa.joliveapp.datamodle.notice.notice_data.Data>) {
        if (dealData == null || dealData.isEmpty()) {
            return
        }
        dataList.clear()
        dataList.addAll(dealData)
        Log.e("Peter","Adapter_Notice   ${dataList.size}")
        hasData = true
        if(hasData && hasTemplate){
            notifyDataSetChanged()
        }
    }

    fun setTemplate(dealData: List<Data>) {
        Log.e("Peter","Adapter_Notice tem")

        if (dealData == null || dealData.isEmpty()) {
            return
        }
        templateList.clear()
        templateList.addAll(dealData)
        Log.e("Peter","Adapter_Notice tem   ${dataList.size}")

        hasTemplate = true
        if(hasData && hasTemplate){
            notifyDataSetChanged()
        }
    }

    private fun getFormat(template: String): String{
        templateList.forEach {
            if (it.template == template){
                return it.zhTW
            }
        }
        return ""
    }

    fun formatMessages(format: String, indicators: Array<Object>): CharSequence {
        return mContext.getString(0, *indicators)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val cell = LayoutInflater.from(mContext).inflate(R.layout.item_notice, parent, false)
        val viewHolder = ViewHolder(cell)
        viewHolder.ivUserPhoto = cell.findViewById(R.id.iv_user_photo)
        viewHolder.tvNoticeInfo = cell.findViewById(R.id.tv_notice_info)
        viewHolder.tvNoticeTime = cell.findViewById(R.id.tv_notice_time)

        return viewHolder
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(dataList.size == 0){return}
        val data = dataList[position]

        when(data.template){
            "EVENT_TO_START",
            "EVENT_CLOSE",
            "EVENT_UPDATE",
            "YOUR_EVENT_APPROVED",
            "EVENT_CHANGE_NAME" -> {
                //BuildConfig.CHATROOM_IMAGE_URL+"event/"+data.fname.replace("event_", "")+".jpg"
                ImgHelper.loadNormalImg(mContext, BuildConfig.CHATROOM_IMAGE_URL+"event/label/"+ data.navigation.image +".jpg", holder.ivUserPhoto)
            }
            "SOMEBODY_PEEK_YOU",
            "SOMEBODY_CANCEL_EVENT",
            "SOMEBODY_FOLLOW_YOU",
            "SOMEBODY_CHANGE_NAME",
            "REVIEW_END_EXPIRED",
            "YOUR_FOLLOW_CREATE_NEW_EVENT",
            "SOMEBODY_WANT_TO_JOIN" -> {
                ImgHelper.loadNormalImg(mContext, BuildConfig.CHATROOM_IMAGE_URL+"dating/"+ data.navigation.image +".jpg", holder.ivUserPhoto)
            }

        }
        Log.e("Peter","data.template  ${(data.template)}    ${data.args}")

        val templateSize = getFormat(data.template).split("%s").size
        val templateSize2 = "你報名%s主辦的『%s".split("%s").size

        Log.e("Peter","data.template  templateSize2   ${templateSize2}    ${data.args}")

        if(templateSize-1 == data.args.size){
            holder.tvNoticeInfo.text = getFormat(data.template).format(*(data.args).toTypedArray())

        } else {
            holder.tvNoticeInfo.text = ""

        }

//        Log.e("Peter","e04 xup6su;6   ${sdf.parse("2008-07-10 19:20:00")}")
        when {
            DateFormatter.isToday(sdf.parse(data.created_at)) -> {
                holder.tvNoticeTime.text = customTodayFormat.format(sdf.parse(data.created_at))
            }
            DateFormatter.isYesterday(sdf.parse(data.created_at)) -> {
                holder.tvNoticeTime.text = "昨天"
            }
            else -> {
                holder.tvNoticeTime.text = customDateFormat.format(sdf.parse(data.created_at))
            }
        }

        if(data.is_read){
            holder.itemView.alpha = 0.5F
        } else {
            holder.itemView.alpha = 1F

        }

        holder.itemView.setOnClickListener {
            mOnItemClickListener?.onItemClick(it, position, data._id.`$oid`, data.template, data.navigation.link)
        }
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        lateinit var ivUserPhoto: ImageView
        lateinit var tvNoticeInfo: TextView
        lateinit var tvNoticeTime: TextView

    }

}