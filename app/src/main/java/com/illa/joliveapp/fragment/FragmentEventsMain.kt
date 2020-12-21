package com.illa.joliveapp.fragment

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.illa.joliveapp.R
import com.illa.joliveapp.adapter.Adapter_Events
import com.illa.joliveapp.tools.Config
import com.illa.joliveapp.viewmodle.EventsActivityVM
import kotlinx.android.synthetic.main.fragment_events_main.*


class FragmentEventsMain : BaseFragment(), View.OnClickListener{

    private val eventsActivityVM : EventsActivityVM by activityViewModels()
    private lateinit var adapter: Adapter_Events

    override fun getLayoutId(): Int {
        return R.layout.fragment_events_main
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (getMContext().get() as Activity).window.addFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH)

        init()
        initObserve()
        eventsActivityVM.getEventsApi(null, null)
    }

    private fun init() {
        adapter = Adapter_Events(getMContext().get(), 3, true)
        rv_events.layoutManager = GridLayoutManager(getMContext().get(), 2, RecyclerView.VERTICAL, false)
        rv_events.adapter = adapter
        adapter.setOnItemClickListener(object : Adapter_Events.OnItemClickListener{
            override fun onItemClick(view: View?, position: Int, label: String) {
                val bundle = bundleOf(Config.EVENT_LABEL to label)
                findNavController().navigate(R.id.action_FragmentEventsMain_to_FragmentEventDetail, bundle)
            }
        })

        btn_create_event.setOnClickListener(this)
        btn_my_created_event_event.setOnClickListener(this)
    }

    private fun initObserve(){
        eventsActivityVM.getEvents().observe(viewLifecycleOwner, Observer {
//            adapter.setData(it.data.event)
        })
    }

    override fun onClick(p0: View) {
        when(p0.id){
            R.id.btn_create_event -> findNavController().navigate(R.id.action_FragmentEventsMain_to_FragmentCreateEvent)
            R.id.btn_my_created_event_event -> findNavController().navigate(R.id.action_FragmentEventsMain_to_FragmentMyEvents)
        }
    }
}