package com.example.myapplication.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.BuildConfig
import com.example.myapplication.R
import com.example.myapplication.datamodle.event.index.EventIndexData
import com.example.myapplication.tools.ImgHelper
import com.example.myapplication.tools.PrefHelper


@Suppress("UNREACHABLE_CODE")
class Adapter_My_Events() :RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val TYPE_GOING = 1
    val TYPE_HISTORY = 2

    private lateinit var dataList: MutableList<EventIndexData>
    private lateinit var mContext: Context
    private var holderType: Int = TYPE_GOING
    private var mOnItemClickListener: OnItemClickListener? = null


    constructor(context: Context?, holderType: Int) : this(){
        dataList = ArrayList()
        this.holderType = holderType
        if (context != null) {
            mContext = context
        }
    }

    interface OnItemClickListener {
        fun onItemClick(view: View?, position: Int, label: String)
    }

    fun setOnItemClickListener(listener: Adapter_My_Events.OnItemClickListener) {
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
            TYPE_GOING -> LayoutInflater.from(mContext).inflate(R.layout.item_my_event, parent, false)
            TYPE_HISTORY -> LayoutInflater.from(mContext).inflate(R.layout.item_event_history, parent, false)
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
        return dataList.size

    }

    @SuppressLint("WrongConstant")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if(dataList.size == 0){return}
        val data = dataList[position]
        setBackground((holder as ViewHolder).ivEventBg, null)

        if(getItemViewType(position) == TYPE_GOING){
            ImgHelper.loadNormalImg(mContext, BuildConfig.IMAGE_URL+data.image, holder.ivEvent)
        } else {
            BuildConfig.CHATROOM_IMAGE_URL+"dating/"+PrefHelper.chatLable +".jpg"
            ImgHelper.loadNormalImgNoCache(mContext,BuildConfig.CHATROOM_IMAGE_URL+"dating/"+PrefHelper.chatLable +".jpg", holder.ivOwner)
            holder.tvOwner.text = PrefHelper.chatName
        }

        holder.tvEventTitle.text = data.title
        holder.tvEventDescription.text = data.description
        holder.tvEventLocation.text = data.location_title
        holder.tvEventTime.text = data.start_time


        holder.itemView.setOnClickListener {
            mOnItemClickListener?.onItemClick(holder.itemView, position, data.label)
        }

        holder.itemView.setLayerType(View.LAYER_TYPE_HARDWARE, null)

    }

    private fun setBackground(ivEvent: ImageView, gd: GradientDrawable?) {
        ivEvent.background = gd
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