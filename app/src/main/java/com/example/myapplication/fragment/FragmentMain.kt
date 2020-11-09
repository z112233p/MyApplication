package com.example.myapplication.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.adapter.Adapter_Event_Type
import com.example.myapplication.adapter.Adapter_Events
import com.example.myapplication.adapter.CircleViewPager
import com.example.myapplication.controller.BannerController
import com.example.myapplication.dialog.DialogChooseCountry
import com.example.myapplication.tools.Config
import com.example.myapplication.viewmodle.EventsActivityVM
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.layout_input_rv.view.*

class FragmentMain : BaseFragment() {

    private val eventsActivityVM : EventsActivityVM by activityViewModels()

    private lateinit var bannerController: BannerController
    private lateinit var imageList: ArrayList<String>
    private lateinit var hotEventAdapter: Adapter_Events
    private lateinit var comingEventAdapter: Adapter_Events
    private lateinit var mayLikeEventAdapter: Adapter_Events

    private lateinit var eventTypeAdapter: Adapter_Event_Type
    private lateinit var eventCategoryList: ArrayList<String>


    override fun getLayoutId(): Int {
        return R.layout.fragment_main
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        expandToolbar(true)
        hideToolBar()
        hideTitle()
        callApis()
        init()
        initObserve()
        initHotEvents()
        initComingEvents()
        initMayLikeEvents()
        iv_choose_country.setOnClickListener {
//            findNavController().navigate(R.id.action_FragmentMain_to_FragmentChooseLocation)
            val dialog = getMContext().get()?.let { it1 -> DialogChooseCountry(it1) }
            dialog?.show()
        }
    }

    private fun callApis(){
        eventsActivityVM.getEventsApi(null)
        eventsActivityVM.getEventCategory()

    }

    fun init(){
        eventCategoryList = ArrayList()
        imageList = ArrayList()
        imageList.add("https://dev.illa.me/images/f67b208659af9a031e9f6c6091e07557.jpg")
        imageList.add("https://dev.illa.me/images/39f2a851b051e1b6a7c5feb9258cc8b4.png")
        imageList.add("https://dev.illa.me/images/f67b208659af9a031e9f6c6091e07557.jpg")
        imageList.add("https://dev.illa.me/images/39f2a851b051e1b6a7c5feb9258cc8b4.png")
        bannerController = BannerController(getMContext().get()!!, imageList.size, vp_banner_view_pager, ll_dot_layout)
        val adapter = CircleViewPager(getMContext().get()!! , imageList)
        adapter.setCircleViewPagerInterface(object : CircleViewPager.CircleViewPagerInterface{
            override fun itemOnClick(pos: Int) {
                Log.e("peter","itemOnClick")
            }

            override fun itemOnLongClick(pos: Int) {
                Log.e("peter","itemOnLongClick")
            }

        })
        vp_banner_view_pager.adapter = adapter
        vp_banner_view_pager.setController(bannerController)

        eventTypeAdapter = Adapter_Event_Type(getMContext().get())
        comingEventAdapter = Adapter_Events(getMContext().get(), 1)
        mayLikeEventAdapter = Adapter_Events(getMContext().get(), 2)
        this.hotEventAdapter = Adapter_Events(getMContext().get(), 3)


        val layoutManager =  GridLayoutManager(getMContext().get(), 2, LinearLayoutManager.HORIZONTAL, false)
        rv_event_type.layoutManager = layoutManager
        rv_event_type.adapter = eventTypeAdapter

    }

    private fun initHotEvents(){
        hotEventAdapter.setOnItemClickListener(object : Adapter_Events.OnItemClickListener{
            override fun onItemClick(view: View?, position: Int, label: String) {
                val bundle = bundleOf(Config.EVENT_LABEL to label)
                findNavController().navigate(R.id.action_FragmentMain_to_FragmentEventDetail, bundle)
            }
        })

        ll_hot_events.run {
            getRecycleView().adapter = hotEventAdapter
            setTitle("熱門推薦")
        }
    }

    private fun initComingEvents(){
        comingEventAdapter.setOnItemClickListener(object : Adapter_Events.OnItemClickListener{
            override fun onItemClick(view: View?, position: Int, label: String) {
                val bundle = bundleOf(Config.EVENT_LABEL to label)
                findNavController().navigate(R.id.action_FragmentMain_to_FragmentEventDetail, bundle)
            }
        })

        ll_coming_events.run {
            getRecycleView().adapter = comingEventAdapter
            setTitle("即將開始")
        }
    }

    private fun initMayLikeEvents(){
        mayLikeEventAdapter.setOnItemClickListener(object : Adapter_Events.OnItemClickListener{
            override fun onItemClick(view: View?, position: Int, label: String) {
                val bundle = bundleOf(Config.EVENT_LABEL to label)
                findNavController().navigate(R.id.action_FragmentMain_to_FragmentEventDetail, bundle)
            }
        })

        ll_may_like_events.run {
            getRecycleView().adapter = mayLikeEventAdapter
            setTitle("猜你喜歡")
        }
    }


    private fun initObserve(){
        eventsActivityVM.getEvents().observe(viewLifecycleOwner, Observer {
            hotEventAdapter.setData(it.data.event)
            comingEventAdapter.setData(it.data.event)
            mayLikeEventAdapter.setData(it.data.event)

        })

        eventsActivityVM.getEventCategoryData().observe(viewLifecycleOwner, Observer {
            it.data.forEach {
                eventCategoryList.add(it.i18n)
            }
            eventTypeAdapter.setData(eventCategoryList)
        })
    }

    override fun onDetach() {
        expandToolbar(false)
        super.onDetach()
    }
}
