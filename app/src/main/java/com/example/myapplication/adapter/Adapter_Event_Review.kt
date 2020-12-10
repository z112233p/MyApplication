package com.example.myapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.BuildConfig
import com.example.myapplication.R
import com.example.myapplication.datamodle.event.review.User
import com.example.myapplication.tools.ImgHelper
import com.example.myapplication.tools.PrefHelper

class Adapter_Event_Review() :RecyclerView.Adapter<Adapter_Event_Review.ViewHolder>() {
    private var dataList: MutableList<User> = ArrayList()
    private lateinit var mContext: Context
    private var mOnItemClickListener: OnItemClickListener? = null


    constructor(context: Context?) : this(){
        if (context != null) {
            mContext = context
        }
    }

    interface OnItemClickListener {
        fun onItemClick(view: View?, status: Int, userId: Int)
    }

    fun setOnItemClickListener(listener: Adapter_Event_Review.OnItemClickListener) {
        mOnItemClickListener = listener
    }

    fun setData(dealData: List<User>?) {
        dataList.clear()

        if (dealData == null || dealData.isEmpty()) {
        } else {
            dataList.addAll(dealData.filter {
                it.id.toString() != PrefHelper.userID
            })
        }

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val cell = LayoutInflater.from(mContext).inflate(R.layout.item_participant_review, parent, false)
        val viewHolder = ViewHolder(cell)
        viewHolder.ivProfilePhoto = cell.findViewById(R.id.iv_profile_photo)
        viewHolder.tvUserName = cell.findViewById(R.id.tv_user_name)
        viewHolder.btnReview = cell.findViewById(R.id.btn_review)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(dataList.size == 0){return}

        val data = dataList[position]

        ImgHelper.loadNormalImg(mContext, BuildConfig.IMAGE_URL+ data.photos[0].url, holder.ivProfilePhoto)
        holder.tvUserName.text = data.nickname

        when(data.status){
            0 -> holder.btnReview.text = "未審核"
            1 -> holder.btnReview.text = "已審核"
            2 -> holder.btnReview.text = "已點名"
        }

        holder.itemView.setOnClickListener {
            mOnItemClickListener?.onItemClick(holder.itemView, data.status, data.id)
        }

    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        lateinit var ivProfilePhoto: ImageView
        lateinit var tvUserName: TextView
        lateinit var btnReview: Button
    }

}