package com.illa.joliveapp.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.illa.joliveapp.BuildConfig
import com.illa.joliveapp.R
import com.illa.joliveapp.activity.EventDetailActivity
import com.illa.joliveapp.adapter.Adapter_Events
import com.illa.joliveapp.custom_view.ItemJoins
import com.illa.joliveapp.datamodle.event.detailv2.Data
import com.illa.joliveapp.dialog.DialogEventDetailOption
import com.illa.joliveapp.tools.ImgHelper
import com.illa.joliveapp.tools.IntentHelper
import com.illa.joliveapp.tools.PrefHelper
import com.illa.joliveapp.tools.Tools
import com.illa.joliveapp.viewmodle.EventDetailActivityVM
import kotlinx.android.synthetic.main.fragment_event_main_v2.*
import kotlinx.android.synthetic.main.fragment_event_main_v2.iv_event_photo
import kotlinx.android.synthetic.main.fragment_event_main_v2.tv_event_budget
import kotlinx.android.synthetic.main.fragment_event_main_v2.tv_event_location
import kotlinx.android.synthetic.main.fragment_event_main_v2.tv_event_start_time
import kotlinx.android.synthetic.main.fragment_event_main_v2.tv_event_title
import kotlinx.android.synthetic.main.fragment_event_main_v2.tv_event_users_limit
import java.text.SimpleDateFormat
import java.util.*


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "CAST_NEVER_SUCCEEDS",
    "UNREACHABLE_CODE"
)
class FragmentEventDetailV2 : BaseFragment() {

    private val eventDetailActVM: EventDetailActivityVM by activityViewModels()

    private lateinit var eventDetailData: Data
    private lateinit var act: EventDetailActivity
    private lateinit var mayLikeEventAdapter: Adapter_Events

    private var locationLatitude = ""
    private var locationLongitude = ""
    private var ChatId = ""
    private var dateSdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    private var daySdf = SimpleDateFormat("MM/dd/yyyy(E)")
    private var timeSdf = SimpleDateFormat("hh:mm a")

    private var nowTime =  Date()
    private var joinType:Any = 0.0


    override fun getLayoutId(): Int {
        return R.layout.fragment_event_main_v2
    }

    @SuppressLint("NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        Log.e("Peter","isNavigationViewInit ")

        if(!isNavigationViewInit){
            Log.e("Peter","isNavigationViewInit  ININ ")
            super.onViewCreated(view, savedInstanceState)
            init()
//            initObserve()
            isNavigationViewInit = true
            setHasOptionsMenu(true)
        }
    }

    override fun onResume() {
        Log.e("Peter","FragmentEventDetailV2 onResume")
        setTitle("")
        initObserve()
        callApis()
        act.showEventOwnerLayout()
        super.onResume()

    }


    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    private fun init(){
        act = getMContext().get() as EventDetailActivity
        tv_event_location.setOnClickListener(onClick)
        tv_location_detail.setOnClickListener(onClick)

        mayLikeEventAdapter = Adapter_Events(getMContext().get(), 2, true)
        mayLikeEventAdapter.setOnItemClickListener(object : Adapter_Events.OnItemClickListener{
            override fun onItemClick(view: View?, position: Int, label: String) {
                getMContext().get()?.let {
                    IntentHelper.gotoEventDetailActivity(it, label, false)
                }
            }

            override fun onAvatarClick(label: String) {
                Log.e("Peter","onAvatarClick")
                getMContext().get()?.let {
                    IntentHelper.gotoMyInfoActivity(it, label)
                }
            }
        })

        val layoutManager =  LinearLayoutManager(getMContext().get(), LinearLayoutManager.HORIZONTAL, false)
        rv_events.layoutManager = layoutManager
        rv_events.adapter = mayLikeEventAdapter
    }


    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n", "ShowToast")
    private val onClick = View.OnClickListener {
        when (it.id) {
            R.id.tv_event_location, R.id.tv_location_detail->{
                val myIntent = Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.google.com/maps/search/?api=1&query="+locationLatitude+","+locationLongitude))
                this.startActivity(myIntent)
            }
        }
    }

    private fun initObserve(){
        eventDetailActVM.getJoinEventResponse().observe(viewLifecycleOwner, Observer {
            Log.e("Peter","getJoinEventResponse  FR $it")
            if(!TextUtils.isEmpty(it)){
                Log.e("Peter","getJoinEventResponse FR IN  $it")
                callApis()
            }
        })


        eventDetailActVM.getCancelJoinEventResponse().observe(viewLifecycleOwner, Observer {
            Log.e("Peter","getCancelJoinEventResponse   $it")

            if(!TextUtils.isEmpty(it)){
                Log.e("Peter","getCancelJoinEventResponse IN  $it")
                callApis()

            }
        })


        eventDetailActVM.getEventDetailV2().observe(viewLifecycleOwner, Observer {
            eventDetailData = it.data
            ChatId = eventDetailData.chat_rid
            Log.e("Peter","getEventDetailV2 eventDetailData.join_type    ${eventDetailData.join_type} ")

            act.eventID = eventDetailData.id
            locationLatitude = eventDetailData.location_gps_latitude
            locationLongitude = eventDetailData.location_gps_longitude

            tv_event_location.text = eventDetailData.location_title
            tv_location_detail.text = eventDetailData.location_address
            tv_event_title.text = eventDetailData.title
            tv_event_budget.text = eventDetailData.budget.toString()
            tv_event_users_limit.text = eventDetailData.users_limit.toString()+ "人"
            tv_event_reward.text = eventDetailData.award_count.toString()
            tv_event_content.text = eventDetailData.description

            val startDay = daySdf.format(dateSdf.parse(eventDetailData.start_time))
            val endDay = daySdf.format(dateSdf.parse(eventDetailData.end_time))
            val startTime = timeSdf.format(dateSdf.parse(eventDetailData.start_time))
            val endTime = timeSdf.format(dateSdf.parse(eventDetailData.end_time))

            if(startDay == endDay){
                val startSting = "$startDay $startTime - $endTime"
                tv_event_start_time.text = startSting

            } else {
                val startSting = "$startDay $startTime to \n$endDay $endTime"
                tv_event_start_time.text = startSting
            }

            val restrictionTime = dateSdf.parse(eventDetailData.review_time)
            val newDate = restrictionTime.time - nowTime.time

            val diffDay = newDate / (1000 * 60 * 60 * 24);
            val diffHour =(newDate - diffDay * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60)
            val diffMinute = (newDate - diffDay * (1000 * 60 * 60 * 24) - diffHour * (1000 * 60 * 60)) / (1000 * 60)

            Log.e("Peter"," getEventDetailV2  diffTime ?  ${eventDetailData.review_time}    $nowTime  ")

            Log.e("Peter"," getEventDetailV2  diffTime !  $diffDay    $diffHour   $diffMinute")

            act.dealEventStatus(eventDetailData.join_type, newDate)
            joinType = eventDetailData.join_type

            ImgHelper.loadNormalImg(getMContext().get(), BuildConfig.IMAGE_URL+eventDetailData.image, iv_event_photo)

            act.setOwnerData(eventDetailData.author)
            act.userLabel = eventDetailData.author.label
            Log.e("Peter"," getEventDetailV2  eventDetailData.joins up")

            eventDetailData.joins.forEach {
                val joins = ItemJoins(getMContext().get())
                joins.setJoinsPhoto(BuildConfig.CHATROOM_IMAGE_URL+"dating/"+ it.label +".jpg")
                Log.e("Peter"," getEventDetailV2  eventDetailData.joins up INLOOP")

                ll_participant.addView(joins)

            }
            if(eventDetailData.joins.isEmpty()){
                ll_participant.visibility = View.GONE
            }
        })

        eventDetailActVM.getCloseEventResponse().observe(viewLifecycleOwner, Observer {
            act.onBackPressed()
        })
        eventDetailActVM.getEvents().observe(viewLifecycleOwner, Observer {
            mayLikeEventAdapter.setData(it.data.event)
        })

    }


    private fun callApis(){
        Log.e("Peter","FragmentEventDetailV2 callApis")

        ll_participant.removeAllViews()
        eventDetailActVM.getEventDetailV2(act.eventLabel)
        eventDetailActVM.getEventsApi("popular", null)

    }

    private fun checkIntentData(){
        if(act.gotoReview){
            val bundle = bundleOf("eventID" to act.eventID)
            findNavController().navigate(R.id.action_FragmentEventDetailV2_to_FragmentEventReview, bundle)

        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.e("Peter","onOptionsItemSelected FRA")
        return when (item.itemId) {
            R.id.action_option -> {
                val dialog = getMContext().get()?.let {
                    DialogEventDetailOption(it, joinType, act.userLabel == PrefHelper.chatLable)
                }
                dialog?.setSeeDetailOnclick(View.OnClickListener {

                    val bundle = bundleOf("eventID" to act.eventID)
                    findNavController().navigate(R.id.action_FragmentEventDetailV2_to_FragmentEventReview, bundle)
                    act.hideEventOwnerLayout()
                    dialog.dismiss()
                })

                dialog?.setGoChatOnclick(View.OnClickListener {
                    if(ChatId == null){
                        Tools.toast(getMContext().get(),"沒有參與活動，無法加入聊天室")
                        return@OnClickListener
                    }
                    Log.e("Peter","ChatId   $ChatId")
                    getMContext().get()?.let { it1 -> IntentHelper.gotoChatRoom(it1,ChatId,
                        act.eventID.toString()
                    ) }
                    dialog.dismiss()

                })
                dialog?.setReportOnclick(View.OnClickListener {
                    val builder: AlertDialog.Builder = AlertDialog.Builder(getMContext().get())
                    builder.setMessage("確定要檢舉？")
                    builder.setPositiveButton("確定") {
                            p0, p1 -> Log.e("Peter","dialog ok")
                    }
                    builder.setNegativeButton("取消") {
                            p0, p1 -> Log.e("Peter","dialog cancel")
                    }
                    val dialog = builder.create()

                    dialog.show()
                })

                dialog?.setCloseEventClick(View.OnClickListener {
                    val builder: AlertDialog.Builder = AlertDialog.Builder(getMContext().get())
                    builder.setMessage("確定要刪除？")
                    builder.setPositiveButton("確定") {
                            p0, p1 -> eventDetailActVM.closeEvent(eventId = act.eventID.toString())
                    }
                    builder.setNegativeButton("取消") {
                            p0, p1 -> Log.e("Peter","dialog cancel")
                    }
                    val dialog = builder.create()

                    dialog.show()


                })

                dialog?.show()

                true
            }

            else -> super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)

    }

}
