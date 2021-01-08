package com.illa.joliveapp.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.illa.joliveapp.BuildConfig
import com.illa.joliveapp.R
import com.illa.joliveapp.datamodle.profile.MyInfoPhoto
import com.illa.joliveapp.tools.ImgHelper

class Adapter_Instagram_Photo(): RecyclerView.Adapter<Adapter_Instagram_Photo.ViewHolder>(){

    private lateinit var dataList: MutableList<String>
    private lateinit var mContext: Context
    private var mOnItemClickListener: OnItemClickListener? = null


    constructor(context: Context) : this(){
        dataList = ArrayList()
        mContext = context

    }

    interface OnItemClickListener {
        fun onItemClick(view: View?, position: Int, url: String)
        fun onDeletePhoto(view: View?, position: Int, url: String)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mOnItemClickListener = listener
    }

    fun setData(dealData: Array<String>?) {
        dataList.clear()

        if (dealData == null || dealData.isEmpty()) {
        } else {
            dataList.addAll(dealData)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val cell = LayoutInflater.from(mContext).inflate(R.layout.item_instagram_photo, parent, false)
        val viewHolder = ViewHolder(cell)
        viewHolder.ivProfilePhoto = cell.findViewById(R.id.iv_profile_photo)
        viewHolder.ivUploadPhotoIcon = cell.findViewById(R.id.iv_upload_photo_icon)
        viewHolder.ivDeletePhoto = cell.findViewById(R.id.iv_delete_photo)

        return viewHolder
    }

    override fun getItemCount(): Int {
        return if(dataList.size > 9){
            9
        } else {
            dataList.size
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(dataList.size == 0){return}
        Log.e("Peter2", "Adapter_Instagram_Photo  :  "+BuildConfig.IMAGE_URL+dataList[position])

        ImgHelper.loadNormalImg(mContext, BuildConfig.IMAGE_URL+dataList[position], holder.ivProfilePhoto)

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var ivProfilePhoto: ImageView
        lateinit var ivUploadPhotoIcon: ImageView
        lateinit var ivDeletePhoto: ImageView
    }
}
