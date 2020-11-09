package com.example.myapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.example.myapplication.R
import com.example.myapplication.tools.ImgHelper

class CircleViewPager(private var mContext: Context,
                      private var imageList: List<String>): PagerAdapter() {

    private lateinit var callback: CircleViewPagerInterface


    interface CircleViewPagerInterface{
        fun itemOnClick(pos: Int)
        fun itemOnLongClick(pos: Int)
    }

    fun setCircleViewPagerInterface(callback: CircleViewPagerInterface){
        this.callback = callback
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as ImageView
    }

    override fun getCount(): Int {
        return Int.MAX_VALUE
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val pos: Int = position % imageList.size
        val layoutInflater: LayoutInflater = this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = layoutInflater.inflate(R.layout.item_banner, container, false)

        view.setOnClickListener {
            callback.itemOnClick(pos)
        }
        view.setOnLongClickListener {
            callback.itemOnLongClick(pos)
            true
        }
        val ivBanner = view.findViewById<ImageView>(R.id.iv_banner_img)
        ImgHelper.loadNormalImg(mContext, imageList[pos], ivBanner)
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as ImageView)
    }
}