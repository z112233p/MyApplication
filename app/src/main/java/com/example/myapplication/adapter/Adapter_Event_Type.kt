package com.example.myapplication.adapter

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.BuildConfig
import com.example.myapplication.R
import com.example.myapplication.datamodle.event.Event
import com.example.myapplication.tools.ImgHelper

class Adapter_Event_Type() :RecyclerView.Adapter<Adapter_Event_Type.ViewHolder>() {
    private lateinit var dataList: ArrayList<String>
    private lateinit var mContext: Context
    private var mOnItemClickListener: OnItemClickListener? = null


    constructor(context: Context?) : this(){
        dataList = ArrayList()
        if (context != null) {
            mContext = context
        }
    }

    interface OnItemClickListener {
        fun onItemClick()
    }

    fun setOnItemClickListener(listener: Adapter_Event_Type.OnItemClickListener) {
        mOnItemClickListener = listener
    }

    fun setData(dealData: List<String>?) {
        if (dealData == null || dealData.isEmpty()) {
            return
        }
        dataList.clear()
        dataList.addAll(dealData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val cell = LayoutInflater.from(mContext).inflate(R.layout.item_event_type, parent, false)
        val viewHolder = ViewHolder(cell)
        viewHolder.tvEventType = cell.findViewById(R.id.iv_event_type)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(dataList.size == 0){return}
        val data = dataList[position]
        holder.tvEventType.text = data
        holder.itemView.setOnClickListener {
            mOnItemClickListener?.onItemClick()
        }
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        lateinit var tvEventType: TextView

    }

}