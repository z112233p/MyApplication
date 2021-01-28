package com.illa.joliveapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.illa.joliveapp.BuildConfig
import com.illa.joliveapp.R
import com.illa.joliveapp.datamodle.event.review.User
import com.illa.joliveapp.datamodle.follows.Fan
import com.illa.joliveapp.datamodle.follows.Follows
import com.illa.joliveapp.datamodle.wallet.HistoryX
import com.illa.joliveapp.tools.ImgHelper
import com.illa.joliveapp.tools.PrefHelper

class Adapter_Wallet() :RecyclerView.Adapter<Adapter_Wallet.ViewHolder>() {
    private var dataList: MutableList<HistoryX> = ArrayList()
    private lateinit var mContext: Context
    private var mOnItemClickListener: OnItemClickListener? = null


    constructor(context: Context?) : this(){
        if (context != null) {
            mContext = context
        }
    }

    interface OnItemClickListener {
        fun onItemClick(view: View?, status: Int, userId: Int)
        fun omAvatarClick(label: String)
    }

    fun setOnItemClickListener(listener: Adapter_Wallet.OnItemClickListener) {
        mOnItemClickListener = listener
    }

    fun setData(dealData: List<HistoryX>?) {
        dataList.clear()
        dealData?.let { dataList.addAll(it) }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val cell = LayoutInflater.from(mContext).inflate(R.layout.item_wallet, parent, false)
        val viewHolder = ViewHolder(cell)
        viewHolder.tvWalletType = cell.findViewById(R.id.tv_wallet_type)
        viewHolder.tvAmount = cell.findViewById(R.id.tv_amount)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(dataList.size == 0){return}
        val data = dataList[position]
        holder.tvWalletType.text = data.comment
        holder.tvAmount.text = data.count.toString()

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        lateinit var tvWalletType: TextView
        lateinit var tvAmount: TextView
    }

}