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
import com.example.myapplication.viewmodle.MainActivityVM

import kotlinx.android.synthetic.main.fragment_notice.*
import me.illa.jolive.adapter.Adapter_Notice


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "CAST_NEVER_SUCCEEDS")
class FragmentNotice : BaseFragment() {

    val mainActVM: MainActivityVM by activityViewModels()

    private lateinit var adapter: Adapter_Notice


    override fun getLayoutId(): Int {
        return R.layout.fragment_notice

    }

    @SuppressLint("NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


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
        setTitle("Notice")

    }


    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    private fun init(){
        adapter = Adapter_Notice(getMContext().get())
        rv_notice.layoutManager = LinearLayoutManager(getMContext().get(), RecyclerView.VERTICAL, false)
        rv_notice.adapter = adapter
    }



    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n", "ShowToast")
    private val onClick = View.OnClickListener {
        when (it.id) {

        }
    }


    private fun callApis(){
        mainActVM.getNotice()
        mainActVM.getNoticeTemplate()
    }

    private fun initObserve(){
        mainActVM.getNoticeData().observe(viewLifecycleOwner, Observer {
            adapter.setData(it.data)
        })

        mainActVM.getNoticeTemplateData().observe(viewLifecycleOwner, Observer {
            adapter.setTemplate(it.data)
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
