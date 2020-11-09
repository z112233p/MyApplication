package com.example.myapplication.fragment

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf

import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.adapter.Adapter_Choose_Location
import com.example.myapplication.adapter.Adapter_Events
import com.example.myapplication.tools.Config
import kotlinx.android.synthetic.main.fragment_choose_location.*


class FragmentChooseLocation : BaseFragment(){

    private lateinit var adapter: Adapter_Choose_Location

    override fun getLayoutId(): Int {
        return R.layout.fragment_choose_location
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initObserve()
    }

    private fun init() {
        adapter = Adapter_Choose_Location(getMContext().get())
        rv_country.layoutManager = GridLayoutManager(getMContext().get(), 2, RecyclerView.VERTICAL, false)
        rv_country.adapter = adapter
        val dataList = ArrayList<String>()
        dataList.add("taipei")
        dataList.add("taichung")
        dataList.add("kaohsiung")
        dataList.add("san_srancisco")
        dataList.add("osaka")
        dataList.add("tokyo")
        dataList.add("hong_kong")
        adapter.setData(dataList)

//        adapter.setOnItemClickListener(object : Adapter_Events.OnItemClickListener{
//            override fun onItemClick(view: View?, position: Int, label: String) {
//                val bundle = bundleOf(Config.EVENT_LABEL to label)
//                findNavController().navigate(R.id.action_FragmentMyEvents_to_FragmentEventDetail, bundle)
//            }
//        })
    }

    private fun initObserve(){

    }
}