package com.example.myapplication.adapter

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.BuildConfig
import com.example.myapplication.R
import com.example.myapplication.datamodle.profile.MyInfoPhoto
import com.example.myapplication.tools.ImgHelper
import kotlin.properties.Delegates

class Adapter_Profile_Photo(): RecyclerView.Adapter<Adapter_Profile_Photo.ViewHolder>(){

    private lateinit var dataList: MutableList<MyInfoPhoto>
    private lateinit var mContext: Context
    private var isEditMode by Delegates.notNull<Boolean>()
    private var mOnItemClickListener: OnItemClickListener? = null


    constructor(context: Context, isEditMode: Boolean) : this(){
        this.isEditMode = isEditMode
        dataList = ArrayList()
        mContext = context

    }

    interface OnItemClickListener {
        fun onItemClick(view: View?, position: Int, url: String)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mOnItemClickListener = listener
    }

    fun setData(dealData: List<MyInfoPhoto>?) {
        if (dealData == null || dealData.isEmpty()) {
            return
        }
        dataList.clear()
        dataList.addAll(dealData)
        if (isEditMode){
            for (i in dataList.size .. 6){
                dataList.add(i,MyInfoPhoto("", i))
            }
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val cell = LayoutInflater.from(mContext).inflate(R.layout.item_profile_photo, parent, false)
        val viewHolder = ViewHolder(cell)
        cell.layoutParams.height = 300
        viewHolder.ivProfilePhoto = cell.findViewById(R.id.iv_profile_photo)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(dataList.size == 0){return}
        Log.e("Peter", "Adapter_Profile_Photo   "+dataList[position].url)
        holder.itemView.setOnClickListener {
            mOnItemClickListener?.onItemClick(holder.itemView, position, dataList[position].url)
        }
        holder.ivProfilePhoto.setImageDrawable(mContext.resources.getDrawable(R.drawable.ic_add_attachment))
        if (TextUtils.isEmpty(dataList[position].url)){return}
        ImgHelper.loadNormalImg(mContext, BuildConfig.IMAGE_URL+dataList[position].url, holder.ivProfilePhoto)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var ivProfilePhoto: ImageView
    }
}
