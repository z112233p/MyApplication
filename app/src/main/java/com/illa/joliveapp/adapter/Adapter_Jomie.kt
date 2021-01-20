package com.illa.joliveapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.illa.joliveapp.BuildConfig
import com.illa.joliveapp.MyApp
import com.illa.joliveapp.R
import com.illa.joliveapp.datamodle.jomie.Data
import com.illa.joliveapp.tools.ImgHelper

class Adapter_Jomie() :RecyclerView.Adapter<Adapter_Jomie.ViewHolder>() {
    companion object {
        var itemWidth: Int = 100
    }
        private var dataList: ArrayList<Data> = ArrayList()
    private lateinit var mContext: Context
    private var mOnItemClickListener: OnItemClickListener? = null

    constructor(context: Context?) : this(){
        if (context != null) {
            mContext = context
        }
    }

    interface OnItemClickListener {
        fun onItemClick(label: String)
    }


    fun setOnItemClickListener(listener: Adapter_Jomie.OnItemClickListener) {
        mOnItemClickListener = listener

    }

    fun setData(dealData: List<Data>) {
        if (dealData == null || dealData.isEmpty()) {
            return
        }
        dataList.clear()
        dataList.addAll(dealData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val cell = LayoutInflater.from(mContext).inflate(R.layout.item_new_jomie, parent, false)
        val viewHolder = ViewHolder(cell)
        viewHolder.tvUserJob = cell.findViewById(R.id.tv_user_job)
        viewHolder.tvUserName = cell.findViewById(R.id.tv_user_name)
        viewHolder.ivUserPhoto = cell.findViewById(R.id.iv_user_photo)
        viewHolder.llUserPhoto = cell.findViewById(R.id.ll_user_photo)

        val parentWidth = parent.width
        val layoutParams = viewHolder.itemView.layoutParams
        if(parentWidth / 3 <= 0){

        } else {
            itemWidth = parentWidth / 3

        }
        layoutParams.width = itemWidth
        return viewHolder
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(dataList.size == 0){return}
        val data = dataList[position]
        holder.tvUserJob.text = MyApp.get()!!.getJob(data.job_id)
        holder.tvUserName.text = data.nickname
        ImgHelper.loadNormalImg(mContext,BuildConfig.CHATROOM_IMAGE_URL+"dating/"+ data.label +".jpg", holder.ivUserPhoto)
        if(data.is_open){
            holder.llUserPhoto.background = mContext.resources.getDrawable(R.drawable.bg_ring_gradient_main)
        } else {
            holder.llUserPhoto.background = mContext.resources.getDrawable(R.drawable.bg_oval_frame_white)

        }

        holder.itemView.setOnClickListener {
            mOnItemClickListener?.onItemClick(data.label)
        }
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        lateinit var tvUserJob: TextView
        lateinit var tvUserName: TextView
        lateinit var ivUserPhoto: ImageView
        lateinit var llUserPhoto: LinearLayout

    }

}