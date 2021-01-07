package com.illa.joliveapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.illa.joliveapp.BuildConfig
import com.illa.joliveapp.R
import com.illa.joliveapp.datamodle.profile.MyInfoPhoto
import com.illa.joliveapp.interface_class.ItemMoveSwipeListener
import com.illa.joliveapp.tools.ImgHelper
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates

class Adapter_Profile_PhotoV2(): RecyclerView.Adapter<Adapter_Profile_PhotoV2.ViewHolder>(),
    ItemMoveSwipeListener {

    private lateinit var dataList: MutableList<MyInfoPhoto>
    private lateinit var sortDataList: MutableList<Int>
    private lateinit var oldDataList: ArrayList<Int>

    private lateinit var sortData: String
    private lateinit var mContext: Context
    private var isEditMode by Delegates.notNull<Boolean>()
    private var mOnItemClickListener: OnItemClickListener? = null
    private var hasPhoto: Boolean = false
    private var hasMove = false

    constructor(context: Context, isEditMode: Boolean) : this(){
        this.isEditMode = isEditMode
        dataList = ArrayList()
        mContext = context
        sortDataList = ArrayList()
        oldDataList = ArrayList()

        oldDataList.clear()
        sortDataList.clear()

        dataList.forEach {
            if(it.sort != -1){
                sortDataList.add(it.sort)
                oldDataList.add(it.sort)
            }
        }
        if(sortDataList.size < 6){
            for(t in sortDataList.size until  6){
                sortDataList.add(t)
                oldDataList.add(t)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(view: View?, position: Int, url: String)
        fun onDeletePhoto(view: View?, position: Int, url: String)
        fun onSort(sortDataList: String)
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

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val cell = LayoutInflater.from(mContext).inflate(R.layout.item_profile_photo_v2, parent, false)
        val viewHolder = ViewHolder(cell)
        viewHolder.ivProfilePhoto = cell.findViewById(R.id.iv_profile_photo)
        viewHolder.ivUploadPhotoIcon = cell.findViewById(R.id.iv_upload_photo_icon)
        viewHolder.ivDeletePhoto = cell.findViewById(R.id.iv_delete_photo)


        viewHolder.itemView.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    Log.e("Peter","itemMOVE setOnTouchListener!!!   ACTION_DOWN  ")

                }
                MotionEvent.ACTION_MOVE -> {
                    Log.e("Peter","itemMOVE setOnTouchListener!!!   ACTION_MOVE  ")

                }
                MotionEvent.ACTION_UP -> {
                    Log.e("Peter","itemMOVE setOnTouchListener!!!   ACTION_UP  ")




                }
                else -> Log.e("Peter","itemMOVE setOnTouchListener!!!   else  ${motionEvent.action}")

            }
            false
        }

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

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        Log.e("Peter","itemMOVE onItemMove   $fromPosition      $toPosition")
        hasMove = true

        val old = ArrayList<Int>()
        oldDataList.forEach {
            old.add(it)
        }

        sortDataList.clear()
        for(i in 0 until 6){
            sortDataList.add(i)
        }
        sortDataList.removeAt(fromPosition)
        sortDataList.add(toPosition,fromPosition)
        Log.e("Peter","itemMOVEsortDataList   $sortDataList      ")
        Log.e("Peter","itemMOVEsortDataList old  $old      ")

        oldDataList.clear()
        Log.e("Peter","itemMOVEsortDataList old  $old      ")

        sortDataList.forEach {
            oldDataList.add(old[it])
        }

        sortData = oldDataList.toString().replace("[","").replace("]","").replace(" ","")
        Collections.swap(dataList, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
//        notifyDataSetChanged()
        return true
    }

    override fun onItemSwipe(position: Int) {
        Log.e("Peter","itemMOVE onItemSwipe    $position")

    }

    override fun onFinish() {
        if(hasMove){
            mOnItemClickListener?.onSort(sortData)
            oldDataList.clear()
            for(i in 0 until 6){
                oldDataList.add(i)
            }
        }
        hasMove = false
    }
}
