package com.illa.joliveapp.adapter

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
import com.illa.joliveapp.datamodle.event.review.User
import com.illa.joliveapp.tools.ImgHelper
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Adapter_Event_Review() :RecyclerView.Adapter<Adapter_Event_Review.ViewHolder>() {

    private lateinit var mContext: Context
    private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    private var dataList: MutableList<User> = ArrayList()
    private var mOnItemClickListener: OnItemClickListener? = null
    private var startTime: String = ""

    constructor(context: Context?) : this(){
        if (context != null) {
            mContext = context
        }
    }

    interface OnItemClickListener {
        fun onItemClick(view: View?, status: Int, userId: Int)
        fun omAvatarClick(label: String)
    }

    fun setOnItemClickListener(listener: Adapter_Event_Review.OnItemClickListener) {
        mOnItemClickListener = listener
    }

    fun setData(
        dealData: List<User>?,
        startTime: String
    ) {
        this.startTime = startTime
        dataList.clear()
//
//        if (dealData == null || dealData.isEmpty()) {
//        } else {
//            dataList.addAll(dealData.filter {
//                it.id.toString() != PrefHelper.userID
//            })
//        }
        dealData?.let { dataList.addAll(it) }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val cell = LayoutInflater.from(mContext).inflate(R.layout.item_participant_review, parent, false)
        val viewHolder = ViewHolder(cell)
        viewHolder.ivProfilePhoto = cell.findViewById(R.id.iv_profile_photo)
        viewHolder.tvUserName = cell.findViewById(R.id.tv_user_name)
        viewHolder.tvReview = cell.findViewById(R.id.tv_review)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    private fun getTime(): Date {
        val curDate = Date(System.currentTimeMillis())
        return curDate
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(dataList.size == 0){return}

        val data = dataList[position]

        ImgHelper.loadNormalImg(mContext, BuildConfig.IMAGE_URL+ data.photos[0].url, holder.ivProfilePhoto)
        holder.tvUserName.text = data.nickname

//        when(data.status){
//            0 -> holder.tvReview.text = "未審核"
//            1 -> holder.tvReview.text = "已審核"
//            2 -> holder.tvReview.text = "已點名"
//        }


        val start = formatter.parse(startTime)

        Log.e("Peter","Adapter_Event_Review   start  $start")
        Log.e("Peter","Adapter_Event_Review   getTime()  ${getTime()}")
        Log.e("Peter","Adapter_Event_Review   diff()  ${getTime().time - start.time}")

        if(getTime().time - start.time < 0){
            when(data.status){
                0 -> holder.tvReview.visibility = View.VISIBLE
                else -> holder.tvReview.visibility = View.GONE
            }
        } else {
            holder.tvReview.visibility = View.GONE
        }

        holder.tvReview.setOnClickListener {
            mOnItemClickListener?.onItemClick(holder.itemView, data.status, data.id)
        }

        holder.ivProfilePhoto.setOnClickListener {
            mOnItemClickListener?.omAvatarClick(data.label)
        }

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        lateinit var ivProfilePhoto: ImageView
        lateinit var tvUserName: TextView
        lateinit var tvReview: TextView
    }

}