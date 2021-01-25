package com.illa.joliveapp.fragment

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.illa.joliveapp.R
import com.illa.joliveapp.adapter.Adapter_Event_Type_Main_Page
import com.illa.joliveapp.adapter.Adapter_Events
import com.illa.joliveapp.adapter.Adapter_Jomie
import com.illa.joliveapp.adapter.CircleViewPager
import com.illa.joliveapp.controller.BannerController
import com.illa.joliveapp.dialog.DialogChooseCountry
import com.illa.joliveapp.tools.IntentHelper
import com.illa.joliveapp.viewmodle.EventsActivityVM
import kotlinx.android.synthetic.main.activity_events.*
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_main.iv_choose_country
import kotlinx.android.synthetic.main.fragment_main.iv_create_event
import kotlinx.android.synthetic.main.layout_input_rv.view.*
import java.util.*
import kotlin.collections.ArrayList

@Suppress("CAST_NEVER_SUCCEEDS")
class FragmentMain : BaseFragment() {

    private val eventsActivityVM : EventsActivityVM by activityViewModels()

    private lateinit var bannerController: BannerController
    private lateinit var imageList: ArrayList<String>
    private lateinit var imageResourceList: ArrayList<Int>

    private lateinit var hotEventAdapter: Adapter_Events
    private lateinit var comingEventAdapter: Adapter_Events
    private lateinit var mayLikeEventAdapter: Adapter_Events

    private lateinit var eventTypeAdapter: Adapter_Event_Type_Main_Page
    private lateinit var eventCategoryList: ArrayList<String>

    private lateinit var jomieAdapter: Adapter_Jomie

    private lateinit var ivChooseCountry: ImageView
    private lateinit var ivCreateEvent: ImageView
    private lateinit var tvChoseCountry: TextView
    private lateinit var tvCreateEvent: TextView

    override fun getLayoutId(): Int {
        return R.layout.fragment_main
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ivChooseCountry = requireActivity().findViewById(R.id.iv_choose_country)
        ivCreateEvent = requireActivity().findViewById(R.id.iv_create_event)
        tvChoseCountry = requireActivity().findViewById(R.id.tv_chose_country)
        tvCreateEvent = requireActivity().findViewById(R.id.tv_create_event)

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        expandToolbar(true)
//        hideToolBar()
        showToolBar()
        clearToolBarFlag()
        hideTitle()
        init()
        initObserve()
        initHotEvents()
        initComingEvents()
        initMayLikeEvents()

        iv_choose_country.setOnClickListener {
            val dialog = getMContext().get()?.let { it1 -> DialogChooseCountry(it1) }
            dialog?.show()
        }
        ivChooseCountry.setOnClickListener {
            val dialog = getMContext().get()?.let { it1 -> DialogChooseCountry(it1) }
            dialog?.show()
        }
        tvChoseCountry.setOnClickListener {
            val dialog = getMContext().get()?.let { it1 -> DialogChooseCountry(it1) }
            dialog?.show()
        }


        ivCreateEvent.setOnClickListener {
            getMContext().get()?.let { it1 -> IntentHelper.gotoCreateEventActivity(it1) }
        }
        iv_create_event.setOnClickListener {
            getMContext().get()?.let { it1 -> IntentHelper.gotoCreateEventActivity(it1) }
        }
        tvCreateEvent.setOnClickListener {
            getMContext().get()?.let { it1 -> IntentHelper.gotoCreateEventActivity(it1) }
        }
    }

    override fun onResume() {
        super.onResume()
        callApis()

    }

    private val onSwipeToRefresh = SwipeRefreshLayout.OnRefreshListener {
        callApis()
        ll_hot_events.getRecycleView().scrollToPosition(0)
        ll_coming_events.getRecycleView().scrollToPosition(0)
        ll_may_like_events.getRecycleView().scrollToPosition(0)

        Handler().postDelayed({
            if(home_refresh.isRefreshing){
                home_refresh.isRefreshing = false
            }

        }, 1000)
    }

    private fun callApis(){
        eventsActivityVM.getEventsApi(null, null)
        eventsActivityVM.getEventCategory()
        eventsActivityVM.getEventIndex()
        eventsActivityVM.getJomie()
    }

    fun init(){
        home_refresh.setOnRefreshListener(onSwipeToRefresh)
        home_refresh.setColorSchemeColors(getMContext().get()?.resources!!.getColor(R.color.colorAccent))

        eventCategoryList = ArrayList()
        imageList = ArrayList()
        imageList.add("https://dev.illa.me/images/f67b208659af9a031e9f6c6091e07557.jpg")
        imageList.add("https://dev.illa.me/images/39f2a851b051e1b6a7c5feb9258cc8b4.png")
        imageList.add("https://dev.illa.me/images/f67b208659af9a031e9f6c6091e07557.jpg")
        imageList.add("https://dev.illa.me/images/39f2a851b051e1b6a7c5feb9258cc8b4.png")
        imageResourceList = ArrayList()
        imageResourceList.add(R.mipmap.ph_banner01)
        imageResourceList.add(R.mipmap.ph_banner02)
        imageResourceList.add(R.mipmap.ph_banner03)
        imageResourceList.add(R.mipmap.ph_banner04)

        bannerController = BannerController(getMContext().get()!!, imageList.size, vp_banner_view_pager, ll_dot_layout)
        bannerController.setImageList(imageResourceList)

        val adapter = CircleViewPager(getMContext().get()!! , imageList)
        adapter.setImageList(imageResourceList)

        adapter.setCircleViewPagerInterface(object : CircleViewPager.CircleViewPagerInterface{
            override fun itemOnClick(pos: Int) {
                Log.e("peter","itemOnClick")
            }

            override fun itemOnLongClick(pos: Int) {
                Log.e("peter","itemOnLongClick")
            }

        })
        vp_banner_view_pager.adapter = adapter
//        vp_banner_view_pager.setPageTransformer(true, object : )
        vp_banner_view_pager.setController(bannerController)

        eventTypeAdapter = Adapter_Event_Type_Main_Page(getMContext().get())
        comingEventAdapter = Adapter_Events(getMContext().get(), 1, true)
        mayLikeEventAdapter = Adapter_Events(getMContext().get(), 2, true)
        hotEventAdapter = Adapter_Events(getMContext().get(), 2, true)
        jomieAdapter = Adapter_Jomie(getMContext().get())

        eventTypeAdapter.setOnItemClickListener(object :Adapter_Event_Type_Main_Page.OnItemClickListener{
            override fun onItemClick(Id: String, name: String) {
                getMContext().get()?.let { it1 ->
                    IntentHelper.gotoSeeMoreActivity(it1, "", Id, name) }
            }

        })

        jomieAdapter.setOnItemClickListener(object : Adapter_Jomie.OnItemClickListener{
            override fun onItemClick(label: String) {
                getMContext().get()?.let { IntentHelper.gotoMyInfoActivity(it,label) }
            }

        })


        val layoutManager =  GridLayoutManager(getMContext().get(), 2)//, LinearLayoutManager.HORIZONTAL, false)
        rv_event_type.layoutManager = layoutManager
        rv_event_type.adapter = eventTypeAdapter

        val layoutManagerNew =  LinearLayoutManager(getMContext().get(), LinearLayoutManager.HORIZONTAL, false)
        rv_new_jomie.layoutManager = layoutManagerNew
        rv_new_jomie.adapter = jomieAdapter
    }

    private fun initHotEvents(){
        hotEventAdapter.setOnItemClickListener(object : Adapter_Events.OnItemClickListener{
            override fun onItemClick(view: View?, position: Int, label: String) {
//                val bundle = bundleOf(Config.EVENT_LABEL to label)
//                findNavController().navigate(R.id.action_FragmentMain_to_FragmentEventDetail, bundle)

                getMContext().get()?.let {
                    IntentHelper.gotoEventDetailActivity(it, label, false)
                }
            }

            override fun onAvatarClick(label: String) {
                getMContext().get()?.let {
                    IntentHelper.gotoMyInfoActivity(it, label)
                }
                Log.e("Peter","onAvatarClick")
            }
        })

        ll_hot_events.run {
            getRecycleView().adapter = hotEventAdapter
            setTitle("熱門推薦")
            tv_show_more.setOnClickListener {
                getMContext().get()?.let { it1 ->
                    IntentHelper.gotoSeeMoreActivity(it1, "popular", "", "熱門推薦") }
            }
        }
    }

    private fun initComingEvents(){
        comingEventAdapter.setOnItemClickListener(object : Adapter_Events.OnItemClickListener{
            override fun onItemClick(view: View?, position: Int, label: String) {
//                val bundle = bundleOf(Config.EVENT_LABEL to label)
//                findNavController().navigate(R.id.action_FragmentMain_to_FragmentEventDetail, bundle)
                getMContext().get()?.let {
                    IntentHelper.gotoEventDetailActivity(it, label, false)
                }
            }

            override fun onAvatarClick(label: String) {
                getMContext().get()?.let {
                    IntentHelper.gotoMyInfoActivity(it, label)
                }
                Log.e("Peter","onAvatarClick")
            }
        })

        ll_coming_events.run {
            getRecycleView().adapter = comingEventAdapter
            setTitle("即將開始")
            tv_show_more.setOnClickListener {
                getMContext().get()?.let { it1 -> IntentHelper.gotoSeeMoreActivity(it1, "incoming", "", "即將開始") }
            }
        }
    }

    private fun initMayLikeEvents(){
        mayLikeEventAdapter.setOnItemClickListener(object : Adapter_Events.OnItemClickListener{
            override fun onItemClick(view: View?, position: Int, label: String) {
//                val bundle = bundleOf(Config.EVENT_LABEL to label)
//                findNavController().navigate(R.id.action_FragmentMain_to_FragmentEventDetail, bundle)
                getMContext().get()?.let {
                    IntentHelper.gotoEventDetailActivity(it, label, false)
                }
            }

            override fun onAvatarClick(label: String) {
                getMContext().get()?.let {
                    IntentHelper.gotoMyInfoActivity(it, label)
                }
                Log.e("Peter","onAvatarClick")
            }
        })

        ll_may_like_events.run {
            getRecycleView().adapter = mayLikeEventAdapter
            setTitle("最新開團")
            tv_show_more.setOnClickListener {
                getMContext().get()?.let { it1 -> IntentHelper.gotoSeeMoreActivity(it1, "latest", "", "最新開團") }
            }
        }
    }


    private fun initObserve(){
        eventsActivityVM.getEventIndexData().observe(viewLifecycleOwner, Observer {
            Log.e("Peter","initObserve   getEventIndexData")
            Collections.shuffle(it.data.popular)
            hotEventAdapter.setData(it.data.popular)
            comingEventAdapter.setData(it.data.incoming )
            mayLikeEventAdapter.setData(it.data.latest)
            eventTypeAdapter.setData(it.data.categorys)
        })

        eventsActivityVM.getEventCategoryData().observe(viewLifecycleOwner, Observer {
            Log.e("Peter","initObserve   getEventCategoryData")

            eventCategoryList.clear()
            it.data.forEach {
                Log.e("Peter","getEventCategory22  eventCategoryList    $eventCategoryList")

                eventCategoryList.add(it.i18n)
            }
//            eventTypeAdapter.setData(it.data)
        })

        eventsActivityVM.getJomieData().observe(viewLifecycleOwner, Observer {
            Log.e("Peter","initObserve   getJomieData")

            jomieAdapter.setData(it.data)
        })
    }

    override fun onDetach() {
        expandToolbar(false)
        super.onDetach()
    }
}
