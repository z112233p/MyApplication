package com.example.myapplication.fragment

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.activity.SeeMoreActivity
import com.example.myapplication.adapter.Adapter_Events
import com.example.myapplication.tools.IntentHelper
import com.example.myapplication.viewmodle.MainActivityVM
import com.example.myapplication.viewmodle.ProfileActivityVM

import kotlinx.android.synthetic.main.fragment_see_more_main.*


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "CAST_NEVER_SUCCEEDS")
class FragmentSeeMoreMain : BaseFragment() {

    private val profileActivityVM: ProfileActivityVM by activityViewModels()
    private lateinit var adapter: Adapter_Events
    private lateinit var act: SeeMoreActivity
    override fun getLayoutId(): Int {
        return R.layout.fragment_see_more_main

    }

    @SuppressLint("NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        act = getMContext().get() as SeeMoreActivity
        Log.e("Peter","isNavigationViewInit ")

        if(!isNavigationViewInit){
            Log.e("Peter","isNavigationViewInit  ININ ")
            super.onViewCreated(view, savedInstanceState)
            init()
            initObserve()
            setHasOptionsMenu(true)
            isNavigationViewInit = true
        }
    }

    override fun onResume() {
        super.onResume()
        callApis()
        setTitle(act.barTitle)
    }


    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    private fun init(){
        adapter = Adapter_Events(getMContext().get(), 4, false)
        adapter.setOnItemClickListener(object : Adapter_Events.OnItemClickListener{
            override fun onItemClick(view: View?, position: Int, label: String) {
                getMContext().get()?.let {
                    IntentHelper.gotoEventDetailActivity(it, label)
                }
            }
        })

        rv_events.layoutManager = LinearLayoutManager(getMContext().get(), RecyclerView.VERTICAL, false)
        rv_events.adapter = adapter
    }



    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n", "ShowToast")
    private val onClick = View.OnClickListener {
        when (it.id) {

        }
    }


    private fun callApis(){
        profileActivityVM.getEventsApi(act.sortType, act.eventsCategorysId)
    }

    private fun initObserve(){


        profileActivityVM.getEvents().observe(viewLifecycleOwner, Observer {
            adapter.setData(it.data.event)
        })

    }
//
//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        super.onCreateOptionsMenu(menu, inflater)
//    }
//
//    @RequiresApi(Build.VERSION_CODES.N)
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        Log.e("Peter","FragmentEditMyInfoV2 onOptionsItemSelected")
//        return when(item.itemId){
//            R.id.action_store ->{
//
//
//                true
//            }
//            else -> true
//
//        }
//        return super.onOptionsItemSelected(item)
//
//    }

}
