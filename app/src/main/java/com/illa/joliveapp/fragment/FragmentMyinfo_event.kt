package com.illa.joliveapp.fragment

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.illa.joliveapp.R
import com.illa.joliveapp.activity.MyInfoActivity
import com.illa.joliveapp.adapter.Adapter_My_Events
import com.illa.joliveapp.tools.IntentHelper
import com.illa.joliveapp.tools.PrefHelper
import com.illa.joliveapp.viewmodle.ProfileActivityVM
import kotlinx.android.synthetic.main.fragment_my_events_history.*


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "CAST_NEVER_SUCCEEDS", "DEPRECATION")
class FragmentMyinfo_event : BaseFragment() {

    private val profileActivityVM: ProfileActivityVM by activityViewModels()
    private lateinit var myEventAdapter: Adapter_My_Events
    private lateinit var historyEventAdapter: Adapter_My_Events

    override fun getLayoutId(): Int {
        return R.layout.fragment_my_events_history

    }

    @SuppressLint("NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.e("Peter","isNavigationViewInit ")

        if(!isNavigationViewInit){
            Log.e("Peter","isNavigationViewInit  ININ ")
            super.onViewCreated(view, savedInstanceState)
            init()
            initObserve()
            callApis()
            isNavigationViewInit = true
        }
    }

    override fun onResume() {
        super.onResume()
//        setTitle("Hello.")
    }


    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    private fun init(){
        myEventAdapter = Adapter_My_Events(getMContext().get(), 1)
        historyEventAdapter= Adapter_My_Events(getMContext().get(), 2)

        myEventAdapter.setOnItemClickListener(object : Adapter_My_Events.OnItemClickListener{
            override fun onItemClick(view: View?, position: Int, label: String) {
                getMContext().get()?.let {
                    IntentHelper.gotoEventDetailActivity(it, label, false)
                }
            }
        })

        historyEventAdapter.setOnItemClickListener(object : Adapter_My_Events.OnItemClickListener{
            override fun onItemClick(view: View?, position: Int, label: String) {
                getMContext().get()?.let {
                    IntentHelper.gotoEventDetailActivity(it, label, false)
                }
            }
        })

        val layoutManager =  LinearLayoutManager(getMContext().get(), LinearLayoutManager.VERTICAL, false)
        rv_events_going.layoutManager = layoutManager
        rv_events_going.adapter = myEventAdapter

        val layoutManager2 =  LinearLayoutManager(getMContext().get(), LinearLayoutManager.VERTICAL, false)

        rv_events_history.layoutManager = layoutManager2
        rv_events_history.adapter = historyEventAdapter
    }

    private val onItemSelectedListener= object : AdapterView.OnItemSelectedListener{
        override fun onNothingSelected(p0: AdapterView<*>?) {
            Log.e("peter","sp_payment_method   onNothingSelected ")
        }

        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n", "ShowToast")
    private val onClick = View.OnClickListener {
        when (it.id) {

        }
    }

//    private fun updateLabel() {
//        val myFormat = "yyyy-MM-dd"
//        val yearFormat = "yyyy"
//        val sdf = SimpleDateFormat(myFormat, Locale.US)
//        val sdfYear = SimpleDateFormat(yearFormat, Locale.US)
//        val currentTime = Calendar.getInstance().time
//        val currentYear = sdfYear.format(currentTime).toInt()
//        val chooseYear = sdfYear.format(myCalendar.time).toInt()
//        var age = abs(currentYear - chooseYear)
//
//
//        if (currentTime.month - myCalendar[Calendar.MONTH] < 0){
//            age--
//        } else if (currentTime.month - myCalendar[Calendar.MONTH] == 0 && currentTime.date - myCalendar[Calendar.DAY_OF_MONTH] < 0){
//            age--
//        }
//
//        ed_user_birth.setText(sdf.format(myCalendar.time))
//    }

    private fun initObserve(){
        profileActivityVM.getMyEventsData().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            myEventAdapter.setData(it.data.processing)
            historyEventAdapter.setData(it.data.history)
        })
    }

    private fun callApis(){
//        profileActivityVM.getMyInfo()
//        profileActivityVM.getEventsApi(PrefHelper.chatLable, null)

        if((getMContext().get() as MyInfoActivity).userLabel == PrefHelper.chatLable){
            profileActivityVM.getMyEvents()

        } else {
            profileActivityVM.getUserEvents((getMContext().get() as MyInfoActivity).userLabel)

        }

    }

}
