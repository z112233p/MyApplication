package com.illa.joliveapp.fragment

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
import com.illa.joliveapp.R
import com.illa.joliveapp.activity.NoticeActivity
import com.illa.joliveapp.tools.IntentHelper
import com.illa.joliveapp.tools.ProgressDialogController
import com.illa.joliveapp.viewmodle.MainActivityVM

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
            setHasOptionsMenu(true)
            isNavigationViewInit = true
        }
    }

    override fun onResume() {
        super.onResume()
        getMContext().get()?.let { ProgressDialogController.setContext(it) }

        initObserve()
        callApis()
        setTitle("通知中心")

    }


    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    private fun init(){
        adapter = Adapter_Notice(getMContext().get())
        adapter.setOnItemClickListener(object : Adapter_Notice.OnItemClickListener{
            override fun onItemClick(
                view: View?,
                position: Int,
                noticeId: String,
                template: String,
                link: String
            ) {
                mainActVM.setNoticeRead(noticeId)
                when(template){
                    "EVENT_TO_START",
                    "EVENT_CLOSE",
                    "EVENT_UPDATE",
                    "YOUR_EVENT_APPROVED",
                    "YOUR_FOLLOW_CREATE_NEW_EVENT",
                    "EVENT_CHANGE_NAME" -> {
                        getMContext().get()?.let { IntentHelper.gotoEventDetailActivity(it, link, false)}
                    }
                    "SOMEBODY_PEEK_YOU",
                    "SOMEBODY_CANCEL_EVENT",
                    "SOMEBODY_FOLLOW_YOU",
                    "SOMEBODY_CHANGE_NAME" -> {
                        getMContext().get()?.let { IntentHelper.gotoMyInfoActivity(it, link) }
                    }
                    "REVIEW_END_EXPIRED",
                    "SOMEBODY_WANT_TO_JOIN" -> {
                        getMContext().get()?.let { IntentHelper.gotoEventReviewActivity(it,link, link) }
                    }

                }
            }

        })
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
            it.data.forEach {
                if(!it.is_read){
                    (getMContext().get() as NoticeActivity).setActionReadClickable()
                    return@forEach
                }
            }
            adapter.setData(it.data)
        })

        mainActVM.getNoticeTemplateData().observe(viewLifecycleOwner, Observer {
            adapter.setTemplate(it.data)
        })

        mainActVM.getNoticeRead().observe(viewLifecycleOwner, Observer {
            mainActVM.getNotice()
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
