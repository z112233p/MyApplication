package com.illa.joliveapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.palette.graphics.Palette
import androidx.palette.graphics.Palette.Swatch
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.illa.joliveapp.BuildConfig
import com.illa.joliveapp.R
import com.illa.joliveapp.datamodle.event.index.EventIndexData
import com.illa.joliveapp.tools.ImgHelper
import java.lang.Exception
import java.text.SimpleDateFormat


@Suppress("UNREACHABLE_CODE")
class Adapter_Events() :RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val TYPE_NORMAL = 1
    val TYPE_GRADUAL = 2
    val TYPE_FULL_IMG = 3
    val TYPE_SEE_MORE = 4
    private var customDateFormat: SimpleDateFormat = SimpleDateFormat("MM月dd日 hh:mm (E)")
    private var customTodayFormat: SimpleDateFormat = SimpleDateFormat("a hh:mm")
    private var sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    private lateinit var dataList: MutableList<EventIndexData>
    private lateinit var mContext: Context
    private var isMainPage: Boolean = false
    private var holderType: Int = TYPE_NORMAL
    private var mOnItemClickListener: OnItemClickListener? = null


    constructor(context: Context?, holderType: Int, isMainPage: Boolean) : this(){
        this.isMainPage = isMainPage
        dataList = ArrayList()
        this.holderType = holderType
        if (context != null) {
            mContext = context
        }
    }

    interface OnItemClickListener {
        fun onItemClick(view: View?, position: Int, label: String)
        fun onAvatarClick(label: String)
    }

    fun setOnItemClickListener(listener: Adapter_Events.OnItemClickListener) {
        mOnItemClickListener = listener
    }

    fun setData(dealData: List<EventIndexData>?) {
        if (dealData == null || dealData.isEmpty()) {
            return
        }
        dataList.clear()
        dataList.addAll(dealData)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return holderType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val cell :View = when(viewType){
            TYPE_FULL_IMG -> LayoutInflater.from(mContext).inflate(R.layout.item_hot_event, parent, false)
            TYPE_NORMAL,TYPE_GRADUAL -> LayoutInflater.from(mContext).inflate(R.layout.item_event_normal, parent, false)
            TYPE_SEE_MORE -> LayoutInflater.from(mContext).inflate(R.layout.item_event_see_more, parent, false)
            else -> LayoutInflater.from(mContext).inflate(R.layout.item_hot_event, parent, false)
        }
        val viewHolder = ViewHolder(cell)
        viewHolder.ivEvent = cell.findViewById(R.id.iv_event)
        viewHolder.ivEventBg = cell.findViewById(R.id.iv_event_bg)
        viewHolder.tvEventTitle = cell.findViewById(R.id.tv_event_title)
        viewHolder.tvEventLocation = cell.findViewById(R.id.tv_event_location)
        viewHolder.tvEventTime = cell.findViewById(R.id.tv_event_time)
        viewHolder.tvEventDescription = cell.findViewById(R.id.tv_event_description)
        viewHolder.tvOwner = cell.findViewById(R.id.tv_owner)
        viewHolder.ivOwner = cell.findViewById(R.id.iv_owner)

        return viewHolder
    }

    override fun getItemCount(): Int {
        return if(isMainPage){
            if(dataList.size <= 10){
                dataList.size
            } else {
                10
            }
        } else {
            dataList.size
        }
    }

    @SuppressLint("WrongConstant")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if(dataList.size == 0){return}
        val data = dataList[position]
        setBackground((holder as ViewHolder).ivEventBg, null)
        ImgHelper.loadNormalImg(mContext, BuildConfig.IMAGE_URL+data.image, holder.ivEvent)


        holder.tvEventTitle.text = data.title
        holder.tvEventDescription.text = data.description
        holder.tvEventLocation.text = data.location_title
        holder.tvEventTime.text = customDateFormat.format(sdf.parse(data.start_time))//data.start_time
        holder.tvOwner.text = data.author.nickname
        ImgHelper.loadNormalImg(mContext, BuildConfig.IMAGE_URL+data.author.image, holder.ivOwner)

        if(getItemViewType(position) != TYPE_FULL_IMG){
            holder.tvEventTime.setTextColor(mContext.resources.getColor(R.color.colorPinkOrange))
        }
        if(getItemViewType(position) == TYPE_GRADUAL){
            holder.tvEventDescription.setTextColor(mContext.resources.getColor(R.color.colorWhite))
        }

        holder.itemView.setOnClickListener {
            mOnItemClickListener?.onItemClick(holder.itemView, position, data.label)
        }
        holder.ivOwner.setOnClickListener {
            mOnItemClickListener?.onAvatarClick(data.author.label)
        }
        holder.tvOwner.setOnClickListener {
            mOnItemClickListener?.onAvatarClick(data.author.label)
        }
        if(TextUtils.isEmpty(data.image_color)){
        Glide.with(mContext)
            .asBitmap()
            .load(BuildConfig.IMAGE_URL+data.image)
            .into(object : CustomTarget<Bitmap?>() {
                override fun onLoadCleared(placeholder: Drawable?) {
                    Log.e("Peter","onLoadCleared  ")
                }

                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap?>?) {
                    Palette.from(resource).generate {
                        val vibrantSwatch = it?.vibrantSwatch
                        val mutedSwatch = it?.mutedSwatch
                        var swatch: Swatch? = null

                        setBackgroundBitmap(holder.ivEvent, resource)

                        if (vibrantSwatch != null) {
                            swatch = vibrantSwatch
                        } else if (mutedSwatch != null) {
                            swatch = mutedSwatch
                        }
                        if (swatch != null) {
                            val mainColor = (swatch.rgb)
                            val gd = GradientDrawable()

                            gd.colors = intArrayOf(Color.TRANSPARENT, Color.TRANSPARENT, mainColor)
                            gd.useLevel = false
                            gd.gradientType = GradientDrawable.LINEAR_GRADIENT
                            if(getItemViewType(position) != TYPE_NORMAL){
                                setBackground(holder.ivEventBg, gd)
                            }
                        }
                    }
                }
            })
        } else {
            try {

                val gd = GradientDrawable()
                //#FF485050
//                Log.e("Peter","TESTCOLOR   ${Color.parseColor("##FF485050")}")
                gd.colors = intArrayOf(Color.TRANSPARENT, Color.TRANSPARENT, Color.parseColor("#${data.image_color}"))
//                gd.colors = intArrayOf(Color.TRANSPARENT, Color.TRANSPARENT, Color.parseColor("#FF485050"))

                gd.useLevel = false
                gd.gradientType = GradientDrawable.LINEAR_GRADIENT
                if(getItemViewType(position) != TYPE_NORMAL){
                    setBackground(holder.ivEventBg, gd)
                }

            } catch (e:Exception){
                Log.e("Peter","TESTCOLOR   CONODIODA")

            }

        }
    }

    private fun setBackground(ivEvent: ImageView, gd: GradientDrawable?) {
        ivEvent.background = gd
    }

    private fun setBackgroundBitmap(ivEvent: ImageView, bitmap: Bitmap?) {
        ivEvent.setImageBitmap(bitmap)

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        lateinit var ivEvent: ImageView
        lateinit var ivEventBg: ImageView
        lateinit var tvEventTitle: TextView
        lateinit var tvEventLocation: TextView
        lateinit var tvEventTime: TextView
        lateinit var tvEventDescription: TextView
        lateinit var tvOwner: TextView
        lateinit var ivOwner: ImageView
    }

}