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
import kotlin.properties.Delegates

class Adapter_Profile_PhotoV2(): RecyclerView.Adapter<Adapter_Profile_PhotoV2.ViewHolder>(){

    private lateinit var dataList: MutableList<MyInfoPhoto>
    private lateinit var mContext: Context
    private var isEditMode by Delegates.notNull<Boolean>()
    private var mOnItemClickListener: OnItemClickListener? = null
    private var hasPhoto: Boolean = false


    constructor(context: Context, isEditMode: Boolean) : this(){
        this.isEditMode = isEditMode
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

    fun setData(dealData: List<MyInfoPhoto>?) {
        dataList.clear()

        if (dealData == null || dealData.isEmpty()) {
            dataList.add(0,MyInfoPhoto("", 0))
        } else {
            dataList.addAll(dealData)
        }
        if (isEditMode){
            for (i in dataList.size .. 6){
                dataList.add(i,MyInfoPhoto("", -1))
            }
        }

        Log.e("Peter", "Adapter_Profile_Photo  deallist $dealData    AFTER   $dataList")

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val cell = LayoutInflater.from(mContext).inflate(R.layout.item_profile_photo_v2, parent, false)
        val viewHolder = ViewHolder(cell)
        viewHolder.ivProfilePhoto = cell.findViewById(R.id.iv_profile_photo)
        viewHolder.ivUploadPhotoIcon = cell.findViewById(R.id.iv_upload_photo_icon)
        viewHolder.ivDeletePhoto = cell.findViewById(R.id.iv_delete_photo)

        return viewHolder
    }

    override fun getItemCount(): Int {
        return 6
    }

    override fun onBindViewHolder(holder: ViewHolder, hplderPosition: Int) {
        if(dataList.size == 0){return}
        Log.e("Peter", "Adapter_Profile_Photo   "+dataList[hplderPosition].url)
        holder.ivProfilePhoto.setImageDrawable(mContext.resources.getDrawable(R.drawable.bg_upload_photo))
        holder.itemView.setOnClickListener {
            mOnItemClickListener?.onItemClick(holder.itemView, hplderPosition, dataList[hplderPosition].url)
        }
        holder.ivDeletePhoto.setOnClickListener {

            dataList.forEach {
                if(it.sort == hplderPosition){
                    mOnItemClickListener?.onDeletePhoto(holder.itemView, hplderPosition, it.url)

                }
            }

        }

        Log.e("Peter", "Adapter_Profile_Photo   "+dataList[hplderPosition].sort+  "    URL:  "+dataList[hplderPosition].url+"    INDEXT   "+hplderPosition)


//        if (!TextUtils.isEmpty(dataList[hplderPosition].url) &&  dataList[hplderPosition].sort != hplderPosition){
//            holder.ivDeletePhoto.visibility = View.GONE
//            holder.ivUploadPhotoIcon.visibility = View.VISIBLE
//
//        }

        hasPhoto = false
        dataList.forEach {
            if(it.sort == hplderPosition){
                Log.e("Peter", "Adapter_Profile_Photo forEach   ${it.sort}  $hplderPosition     ${it.url}")
                hasPhoto = true
                ImgHelper.loadNormalImg(mContext, BuildConfig.IMAGE_URL+it.url, holder.ivProfilePhoto)
            }
        }

        if (hasPhoto){
            holder.ivDeletePhoto.visibility = View.VISIBLE
            holder.ivUploadPhotoIcon.visibility = View.GONE
        } else {

            holder.ivDeletePhoto.visibility = View.GONE
            holder.ivUploadPhotoIcon.visibility = View.VISIBLE
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var ivProfilePhoto: ImageView
        lateinit var ivUploadPhotoIcon: ImageView
        lateinit var ivDeletePhoto: ImageView
    }
}
