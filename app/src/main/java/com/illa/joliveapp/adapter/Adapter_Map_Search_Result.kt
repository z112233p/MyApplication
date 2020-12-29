package com.illa.joliveapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.illa.joliveapp.R

class Adapter_Map_Search_Result(): RecyclerView.Adapter<Adapter_Map_Search_Result.ViewHolder>(){

    private lateinit var dataList: MutableList<String>
    private lateinit var placeIdList: MutableList<String>
    private lateinit var mContext: Context
    private var mOnItemClickListener: OnItemClickListener? = null


    constructor(context: Context) : this(){
        dataList = ArrayList()
        mContext = context
    }

    interface OnItemClickListener {
        fun onItemClick(view: View?, position: Int, title: String, placeid: String)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mOnItemClickListener = listener
    }

    fun setData(dealData: List<String>) {
        dataList.clear()
        dataList.addAll(dealData)
        notifyDataSetChanged()
    }

    fun setIdList(placeIdList: ArrayList<String>) {
        this.placeIdList = placeIdList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val cell = LayoutInflater.from(mContext).inflate(R.layout.item_map_search_result, parent, false)
        val viewHolder = ViewHolder(cell)
        viewHolder.tvLocationTitle = cell.findViewById(R.id.tv_location_title)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(dataList.size == 0){return}
        holder.tvLocationTitle.text = dataList[position]
        holder.itemView.setOnClickListener {
            mOnItemClickListener?.onItemClick(it, position, dataList[position], placeIdList[position])
        }

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var tvLocationTitle: TextView
    }
}
