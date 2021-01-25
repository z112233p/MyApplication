package com.illa.joliveapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.illa.joliveapp.R
import com.illa.joliveapp.datamodle.event.index.Category
import com.illa.joliveapp.datamodle.event.list.Data

class Adapter_Event_Type_Main_Page() :RecyclerView.Adapter<Adapter_Event_Type_Main_Page.ViewHolder>() {
    private lateinit var dataList: ArrayList<Category>
    private lateinit var mContext: Context
    private var mOnItemClickListener: OnItemClickListener? = null
    private var currentPosition: Int = -1
    private var oldHighLight: Int = 0

    constructor(context: Context?) : this(){
        dataList = ArrayList()
        if (context != null) {
            mContext = context
        }
    }

    interface OnItemClickListener {
        fun onItemClick(Id: String, name: String)
    }

    fun setType(typeId: Int){
        currentPosition = typeId
        notifyDataSetChanged()

    }

    fun setOnItemClickListener(listener: Adapter_Event_Type_Main_Page.OnItemClickListener) {
        mOnItemClickListener = listener

    }

    fun setData(dealData: List<Category>) {
        if (dealData == null || dealData.isEmpty()) {
            return
        }
        dataList.clear()
        dataList.addAll(dealData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val cell = LayoutInflater.from(mContext).inflate(R.layout.item_event_type_main_page, parent, false)
        val viewHolder = ViewHolder(cell)
        viewHolder.tvEventType = cell.findViewById(R.id.iv_event_type)
        viewHolder.tvEventCount = cell.findViewById(R.id.tv_event_count)
//        viewHolder.clMain = cell.findViewById(R.id.cl_main)

        return viewHolder
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(dataList.size == 0){return}
        val data = dataList[position]
        holder.tvEventType.text = data.i18n
        holder.tvEventCount.text = data.count.toString()
//        if(position == currentPosition){
//            holder.tvEventType.background = mContext.resources.getDrawable(R.drawable.bg_event_type_btn_selected)
////            holder.tvEventType.setTextColor(mContext.resources.getColor(R.color))
//        } else {
//            holder.tvEventType.background = mContext.resources.getDrawable(R.drawable.bg_event_type_btn)
//
//        }


            holder.itemView.setOnClickListener {
            mOnItemClickListener?.onItemClick(data.id.toString(), data.i18n)
            oldHighLight = currentPosition
            currentPosition = position
            notifyItemChanged(oldHighLight)
            notifyItemChanged(currentPosition)
        }


//        holder.clMain.layoutParams =   ConstraintLayout.LayoutParams(
//            ViewGroup.LayoutParams.WRAP_CONTENT,
//            ViewGroup.LayoutParams.WRAP_CONTENT
//        )
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        lateinit var tvEventType: TextView
        lateinit var tvEventCount: TextView

//        lateinit var clMain: ConstraintLayout

    }

}