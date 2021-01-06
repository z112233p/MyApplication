package com.illa.joliveapp.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.illa.joliveapp.MyApp
import com.illa.joliveapp.R

class Adapter_Choose_Location() :RecyclerView.Adapter<Adapter_Choose_Location.ViewHolder>() {
    private var dataList: MutableList<String> = ArrayList()
    private lateinit var mContext: Context
    private var mOnItemClickListener: OnItemClickListener? = null
    private var currentPosition: Int = 0
    private var oldHighLight: Int = 0


    constructor(context: Context?) : this(){
        if (context != null) {
            mContext = context
        }
    }

    interface OnItemClickListener {
        fun onItemClick(view: View?, position: Int)
    }

    fun setOnItemClickListener(listener: Adapter_Choose_Location.OnItemClickListener) {
        mOnItemClickListener = listener
    }

    fun setData(dealData: List<String>?) {
        Log.e("Peter","Adapter_Choose_Location0")

        if (dealData == null || dealData.isEmpty()) {
            return
        }
        dataList.clear()
        dataList.addAll(dealData)
        Log.e("Peter","Adapter_Choose_Location   ${dataList.size}")

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val cell = LayoutInflater.from(mContext).inflate(R.layout.item_location, parent, false)
        val viewHolder = ViewHolder(cell)
        viewHolder.ivLocation = cell.findViewById(R.id.iv_location)
        viewHolder.tvLocation = cell.findViewById(R.id.tv_location)
        viewHolder.llMain = cell.findViewById(R.id.ll_main)
        viewHolder.parent = cell

        return viewHolder
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(dataList.size == 0){return}
        val data = dataList[position]
        holder.tvLocation.text = data
        MyApp.get()?.location?.get(data)?.let { holder.ivLocation.setImageResource(it) }

        if(position == currentPosition){

            holder.llMain.background = mContext.resources.getDrawable(R.color.colorAccent)


        } else {
            holder.llMain.background = mContext.resources.getDrawable(R.color.transparent)

        }


//        holder.parent.setOnClickListener {
//            oldHighLight = currentPosition
//            currentPosition = position
//            notifyItemChanged(oldHighLight)
//            notifyItemChanged(currentPosition)
//
//        }
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        lateinit var ivLocation: ImageView
        lateinit var tvLocation: TextView
        lateinit var llMain: LinearLayout
        lateinit var parent: View

    }

}