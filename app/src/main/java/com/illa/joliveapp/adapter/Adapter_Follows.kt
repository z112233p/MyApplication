package com.illa.joliveapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.illa.joliveapp.BuildConfig
import com.illa.joliveapp.R
import com.illa.joliveapp.datamodle.event.review.User
import com.illa.joliveapp.datamodle.follows.Fan
import com.illa.joliveapp.datamodle.follows.Follows
import com.illa.joliveapp.tools.ImgHelper
import com.illa.joliveapp.tools.PrefHelper

class Adapter_Follows() :RecyclerView.Adapter<Adapter_Follows.ViewHolder>() {
    private var dataList: MutableList<Fan> = ArrayList()
    private lateinit var mContext: Context
    private var mOnItemClickListener: OnItemClickListener? = null


    constructor(context: Context?) : this(){
        if (context != null) {
            mContext = context
        }
    }

    interface OnItemClickListener {
        fun onItemClick(view: View?, status: Int, userId: Int)
        fun omAvatarClick(label: String)
    }

    fun setOnItemClickListener(listener: Adapter_Follows.OnItemClickListener) {
        mOnItemClickListener = listener
    }

    fun setData(dealData: List<Fan>?) {
        dataList.clear()
        dealData?.let { dataList.addAll(it) }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val cell = LayoutInflater.from(mContext).inflate(R.layout.item_participant_review, parent, false)
        val viewHolder = ViewHolder(cell)
        viewHolder.ivProfilePhoto = cell.findViewById(R.id.iv_profile_photo)
        viewHolder.tvUserName = cell.findViewById(R.id.tv_user_name)
        viewHolder.tvReview = cell.findViewById(R.id.tv_review)
        viewHolder.ivGender = cell.findViewById(R.id.iv_gender)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(dataList.size == 0){return}

        val data = dataList[position]
        ImgHelper.loadNormalImg(mContext, BuildConfig.CHATROOM_IMAGE_URL+"dating/"+ data.label +".jpg", holder.ivProfilePhoto)


        holder.tvUserName.text = data.nickname
        holder.tvReview.visibility = View.GONE
        if(data.gender == 0){
            holder.ivGender.setImageDrawable(mContext.resources.getDrawable(R.mipmap.ic_gender_woman))
        } else if(data.gender == 1){
            holder.ivGender.setImageDrawable(mContext.resources.getDrawable(R.mipmap.ic_gender_man))

        } else {
            holder.ivGender.setImageDrawable(mContext.resources.getDrawable(R.mipmap.ic_close))

        }

        holder.ivProfilePhoto.setOnClickListener {
            mOnItemClickListener?.omAvatarClick(data.label)
        }

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        lateinit var ivProfilePhoto: ImageView
        lateinit var tvUserName: TextView
        lateinit var tvReview: TextView
        lateinit var ivGender: ImageView

    }

}