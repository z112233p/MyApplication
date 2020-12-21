package com.illa.joliveapp.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.illa.joliveapp.R
import com.illa.joliveapp.tools.ImgHelper

class CircleViewPager(private var mContext: Context,
                      private var imageList: List<String>): PagerAdapter() {

    private lateinit var callback: CircleViewPagerInterface
    private var imageResourceList = ArrayList<Int>()

    interface CircleViewPagerInterface{
        fun itemOnClick(pos: Int)
        fun itemOnLongClick(pos: Int)
    }

    fun setImageList(imageResourceList: ArrayList<Int>) {
        if(imageResourceList.size == 0){return}
        this.imageResourceList.clear()
        imageResourceList.forEach {
            this.imageResourceList.add(it)
        }
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
        Log.e("Peter", "CircleViewPager    ${imageList}")
        if(imageResourceList.size == 0){
            ImgHelper.loadNormalImg(mContext, imageList[pos], ivBanner)
        } else {
            ivBanner.setImageResource(imageResourceList[pos])
        }
//        ivBanner.setImageResource(imageResourceList[pos])
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as ImageView)
    }
}