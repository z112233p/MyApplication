package com.illa.joliveapp.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.illa.joliveapp.R
import com.illa.joliveapp.activity.EventDetailActivity
import com.illa.joliveapp.activity.EventReviewActivity
import com.illa.joliveapp.adapter.Adapter_Event_Review
import com.illa.joliveapp.datamodle.event.review.User
import com.illa.joliveapp.datamodle.event.review_member.ReviewMember
import com.illa.joliveapp.viewmodle.EventDetailActivityVM
import kotlinx.android.synthetic.main.fragment_event_review.*


class FragmentEventReviewV2 : BaseFragment(){

    private val eventDetailActVM: EventDetailActivityVM by activityViewModels()
    private lateinit var adapter: Adapter_Event_Review
    private lateinit var adapterNotCheck: Adapter_Event_Review
    private lateinit var act: EventReviewActivity

    private var eventID: String = ""
    private var memberHasChecked: ArrayList<User> = ArrayList()
    private var memberNotCheck: ArrayList<User> = ArrayList()

    override fun getLayoutId(): Int {
        return R.layout.fragment_event_review
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initObserve()
        eventDetailActVM.getReviewList(eventID)
    }

    private fun init() {
        act = getMContext().get() as EventReviewActivity

//        act.hideEventStatusLayout()
//        act.hideMenu()

        setTitle("參加者名單")
//        eventID = arguments?.get("eventID").toString()
        eventID = act.eventID
        adapter = Adapter_Event_Review(getMContext().get())
        rv_event_review.layoutManager = LinearLayoutManager(getMContext().get(), RecyclerView.VERTICAL, false)

        adapterNotCheck = Adapter_Event_Review(getMContext().get())
        rv_event_review_not_check.layoutManager = LinearLayoutManager(getMContext().get(), RecyclerView.VERTICAL, false)

        rv_event_review.adapter = adapter
        rv_event_review_not_check.adapter = adapterNotCheck
        adapterNotCheck.setOnItemClickListener(object : Adapter_Event_Review.OnItemClickListener{
            override fun onItemClick(view: View?, status: Int, userId: Int) {
                val dataBody = ReviewMember(eventID.toInt(), userId)

                val builder: AlertDialog.Builder = AlertDialog.Builder(getMContext().get())
                builder.setMessage("確定要審核？")
                builder.setPositiveButton("確定") {
                        p0, p1 -> eventDetailActVM.postEventReview(dataBody)
                }
                builder.setNegativeButton("取消") {
                        p0, p1 -> Log.e("Peter","dialog cancel")
                }
                val dialog = builder.create()
                dialog.show()
//                when(status){
//                    0 -> eventDetailActVM.postEventReview(dataBody)
//                    1 -> eventDetailActVM.postEventRollCall(dataBody)
//                }
            }
        })
    }

    private fun initObserve(){
        eventDetailActVM.getEventReview().observe(viewLifecycleOwner, Observer {
            Log.e("Peter","FragmentEventReview getEventReview   22c  ${it.data.users}")
            memberHasChecked.clear()
            memberNotCheck.clear()
            it.data.users.forEach {
                if(it.status == 0 ){
                    memberNotCheck.add(it)
                } else if(it.status != 9){
                    memberHasChecked.add(it)
                }
            }

            adapter.setData(memberHasChecked)
            adapterNotCheck.setData(memberNotCheck)
        })

    }
}