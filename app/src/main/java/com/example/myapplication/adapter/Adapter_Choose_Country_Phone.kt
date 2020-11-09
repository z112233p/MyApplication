package com.example.myapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.jay.widget.StickyHeaders


class Adapter_Choose_Country_Phone () : RecyclerView.Adapter<RecyclerView.ViewHolder>(), StickyHeaders {
    private val HEADER_ITEM = 123

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == HEADER_ITEM) {
            val inflate: View = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event_type, parent, false)
            MyViewHolder(inflate)
        } else {
            val inflate: View = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event_type, parent, false)
            MyViewHolder(inflate)
        }
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun isStickyHeader(p0: Int): Boolean {
        TODO("Not yet implemented")
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    }

}