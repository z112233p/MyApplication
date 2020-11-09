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
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.adapter.Adapter_Event_Review
import com.example.myapplication.adapter.Adapter_Events
import com.example.myapplication.datamodle.event.review_member.ReviewMember
import com.example.myapplication.datamodle.profile.delete_photo.DeleteMyPhoto
import com.example.myapplication.tools.Config
import com.example.myapplication.tools.PrefHelper
import com.example.myapplication.viewmodle.EventsActivityVM
import kotlinx.android.synthetic.main.fragment_event_review.*


class FragmentEventReview : BaseFragment(){

    private val eventsActivityVM : EventsActivityVM by activityViewModels()
    private lateinit var adapter: Adapter_Event_Review
    private var eventID: String = ""

    override fun getLayoutId(): Int {
        return R.layout.fragment_event_review
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initObserve()
        eventsActivityVM.getReviewList(eventID)
    }

    private fun init() {
        eventID = arguments?.get("eventID").toString()

        adapter = Adapter_Event_Review(getMContext().get())
        rv_event_review.layoutManager = LinearLayoutManager(getMContext().get(), RecyclerView.VERTICAL, false)

        rv_event_review.adapter = adapter
        adapter.setOnItemClickListener(object : Adapter_Event_Review.OnItemClickListener{
            override fun onItemClick(view: View?, status: Int, userId: Int) {
                val dataBody = ReviewMember(eventID.toInt(), userId)

                when(status){
                    0 -> eventsActivityVM.postEventReview(dataBody)
                    1 -> eventsActivityVM.postEventRollCall(dataBody)
                }
            }
        })
    }

    private fun initObserve(){
        eventsActivityVM.getEventReview().observe(viewLifecycleOwner, Observer {
            Log.e("Peter","FragmentEventReview getEventReview     $it")

            adapter.setData(it.data.users)
        })

    }
}