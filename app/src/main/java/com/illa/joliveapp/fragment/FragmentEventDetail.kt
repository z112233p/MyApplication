package com.illa.joliveapp.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.illa.joliveapp.BuildConfig
import com.illa.joliveapp.R
import com.illa.joliveapp.datamodle.event.detailv2.Data
import com.illa.joliveapp.tools.Config
import com.illa.joliveapp.tools.ImgHelper
import com.illa.joliveapp.tools.Tools
import com.illa.joliveapp.viewmodle.EventsActivityVM
import kotlinx.android.synthetic.main.fragment_event_detial.*

class FragmentEventDetail : BaseFragment(), View.OnClickListener {
    private val eventsActivityVM : EventsActivityVM by activityViewModels()
    private var eventLabel: String = ""
    private var eventID: String = ""
    private lateinit var eventDetailData: Data

    override fun getLayoutId(): Int {
        return R.layout.fragment_event_detial
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserve()
        init()
        callAPIs()
    }

    private fun callAPIs(){
        eventsActivityVM.getEventDetail(eventLabel)
    }

    private fun init(){
        eventLabel = arguments?.getString(Config.EVENT_LABEL).toString()
        btn_sign_up_cancel.setOnClickListener(this)
        btn_sign_up.setOnClickListener(this)
        btn_check.setOnClickListener(this)
        btn_edit.setOnClickListener(this)
    }

    private fun initObserve(){
        eventsActivityVM.getEventDetail().observe(viewLifecycleOwner, Observer {
            eventDetailData = it.data
            eventID = eventDetailData.id.toString()

            ImgHelper.loadNormalImg(getMContext().get(),BuildConfig.IMAGE_URL+eventDetailData.image, iv_event_photo)

            tv_event_title.text = eventDetailData.title
            tv_event_description.text = eventDetailData.description
            tv_event_restriction_time.text = eventDetailData.review_time
            tv_event_start_time.text = eventDetailData.start_time
            tv_event_end_time.text = eventDetailData.end_time
            tv_event_type.text = eventDetailData.events_categorys_id.toString()
            tv_payment_method.text = eventDetailData.payment_method_id.toString()
            tv_currency_type.text = eventDetailData.currency_id.toString()
            tv_event_users_limit.text = eventDetailData.users_limit.toString()
            tv_event_budget.text = eventDetailData.budget.toString()
            tv_event_location.text = eventDetailData.location_title

            Log.e("Peter","join_type:  ${eventDetailData.join_type}")
            if(eventDetailData.join_type == null){
                btn_sign_up.visibility = View.VISIBLE
                return@Observer
            }

            when(eventDetailData.join_type){
                "1" -> btn_sign_up_cancel.visibility = View.VISIBLE
                "9" ->{
                    btn_check.visibility = View.VISIBLE
                    btn_edit.visibility = View.VISIBLE
                }
//                else ->btn_sign_up.visibility = View.VISIBLE
            }
        })

        eventsActivityVM.getErrorMsg().observe(viewLifecycleOwner, Observer {
            Tools.toast(getMContext().get(), it)
        })
    }

    override fun onClick(p0: View) {
        when(p0.id){
            R.id.btn_sign_up_cancel -> eventsActivityVM.cancelJoinEvent(eventID)
            R.id.btn_sign_up -> eventsActivityVM.joinEvent(eventID)
            R.id.btn_check ->{
                val bundle = bundleOf("eventID" to eventID)
                findNavController().navigate(R.id.action_FragmentEventDetail_to_FragmentEventReview, bundle)
            }
            R.id.btn_edit ->{
                val bundle = bundleOf("eventDetailData" to eventDetailData)
                findNavController().navigate(R.id.action_FragmentEventDetail_to_FragmentEventUpdate, bundle)
            }
        }
    }

}