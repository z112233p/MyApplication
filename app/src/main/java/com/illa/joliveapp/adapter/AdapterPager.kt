package com.illa.joliveapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class AdapterPager(fm: FragmentManager, private val tabsTitle: List<String>, private val fragmentList: ArrayList<Fragment>) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return tabsTitle.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return tabsTitle[position]
    }
}