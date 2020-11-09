package com.example.myapplication.fragment

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.adapter.Adapter_Events
import com.example.myapplication.tools.Config
import com.example.myapplication.tools.PrefHelper
import com.example.myapplication.viewmodle.EventsActivityVM
import kotlinx.android.synthetic.main.fragment_my_events.*


class FragmentMyEvents : BaseFragment(){

    private val eventsActivityVM : EventsActivityVM by activityViewModels()
    private lateinit var adapter: Adapter_Events

    override fun getLayoutId(): Int {
        return R.layout.fragment_my_events
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initObserve()
        eventsActivityVM.getEventsApi(PrefHelper.chatLable)
    }

    private fun init() {
        adapter = Adapter_Events(getMContext().get(), 3)
        rv_events.layoutManager = GridLayoutManager(getMContext().get(), 2, RecyclerView.VERTICAL, false)
        rv_events.adapter = adapter
        adapter.setOnItemClickListener(object : Adapter_Events.OnItemClickListener{
            override fun onItemClick(view: View?, position: Int, label: String) {
                val bundle = bundleOf(Config.EVENT_LABEL to label)
                findNavController().navigate(R.id.action_FragmentMyEvents_to_FragmentEventDetail, bundle)
            }
        })
    }

    private fun initObserve(){
        eventsActivityVM.getEvents().observe(viewLifecycleOwner, Observer {
            adapter.setData(it.data.event)
        })
    }
}