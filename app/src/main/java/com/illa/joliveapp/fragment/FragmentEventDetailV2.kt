package com.illa.joliveapp.fragment

import android.annotation.SuppressLint
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
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.illa.joliveapp.BuildConfig
import com.illa.joliveapp.R
import com.illa.joliveapp.activity.EventDetailActivity
import com.illa.joliveapp.custom_view.ItemJoins
import com.illa.joliveapp.datamodle.event.detailv2.Data
import com.illa.joliveapp.dialog.DialogEventDetailOption
import com.illa.joliveapp.tools.ImgHelper
import com.illa.joliveapp.tools.IntentHelper
import com.illa.joliveapp.tools.Tools
import com.illa.joliveapp.viewmodle.EventDetailActivityVM
import kotlinx.android.synthetic.main.fragment_event_main_v2.*
import kotlinx.android.synthetic.main.fragment_event_main_v2.iv_event_photo
import kotlinx.android.synthetic.main.fragment_event_main_v2.tv_event_budget
import kotlinx.android.synthetic.main.fragment_event_main_v2.tv_event_location
import kotlinx.android.synthetic.main.fragment_event_main_v2.tv_event_start_time
import kotlinx.android.synthetic.main.fragment_event_main_v2.tv_event_title
import kotlinx.android.synthetic.main.fragment_event_main_v2.tv_event_users_limit


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "CAST_NEVER_SUCCEEDS",
    "UNREACHABLE_CODE"
)
class FragmentEventDetailV2 : BaseFragment() {

    private val eventDetailActVM: EventDetailActivityVM by activityViewModels()
    private lateinit var eventDetailData: Data
    private lateinit var act: EventDetailActivity
    private var locationLatitude = ""
    private var locationLongitude = ""
    private var ChatId = ""

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

            act.dealEventStatus(eventDetailData.join_type)
            act.eventID = eventDetailData.id
            locationLatitude = eventDetailData.location_gps_latitude
            locationLongitude = eventDetailData.location_gps_longitude

            tv_event_start_time.text = eventDetailData.start_time
            tv_event_location.text = eventDetailData.location_title
            tv_location_detail.text = eventDetailData.location_address
            tv_event_title.text = eventDetailData.title
            tv_event_budget.text = eventDetailData.budget.toString()
            tv_event_users_limit.text = eventDetailData.users_limit.toString()
            tv_event_reward.text = eventDetailData.award_count.toString()
            tv_event_content.text = eventDetailData.description

            ImgHelper.loadNormalImg(getMContext().get(), BuildConfig.IMAGE_URL+eventDetailData.image, iv_event_photo)

            act.setOwnerData(eventDetailData.author)
            Log.e("Peter"," getEventDetailV2  eventDetailData.joins up")

            eventDetailData.joins.forEach {
                val joins = ItemJoins(getMContext().get())
                joins.setJoinsPhoto(BuildConfig.CHATROOM_IMAGE_URL+"dating/"+ it.label +".jpg")
                Log.e("Peter"," getEventDetailV2  eventDetailData.joins up INLOOP")

                ll_participant.addView(joins)

            }
        })
    }


    private fun callApis(){
        Log.e("Peter","FragmentEventDetailV2 callApis")

        ll_participant.removeAllViews()
        eventDetailActVM.getEventDetailV2(act.eventLabel)
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
                val dialog = getMContext().get()?.let { DialogEventDetailOption(it) }
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

                dialog?.show()

                true
            }

            else -> super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)

    }

}
