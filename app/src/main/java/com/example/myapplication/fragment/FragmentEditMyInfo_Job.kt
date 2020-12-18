package com.example.myapplication.fragment

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.view.get
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.activity.EditMyInfoActivity
import com.example.myapplication.activity.ProfileActivity
import com.example.myapplication.custom_view.ItemJobView
import com.example.myapplication.viewmodle.ProfileActivityVM
import kotlinx.android.synthetic.main.fragment_creat_profile_step_2.*


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "CAST_NEVER_SUCCEEDS")
class FragmentEditMyInfo_Job : BaseFragment() {

    private val profileActivityVM: ProfileActivityVM by activityViewModels()
    private var currentId = 0
    private lateinit var act: EditMyInfoActivity



    override fun getLayoutId(): Int {
        return R.layout.fragment_edit_myinfo_job
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
        setTitle("")
    }


    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    private fun init(){
        act = getMContext().get() as EditMyInfoActivity

        tv_next_step.setOnClickListener(onClick)
        tv_next_step.isClickable = false

    }


    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n", "ShowToast")
    private val onClick = View.OnClickListener {
        when (it.id) {
            R.id.tv_next_step -> {
//                act.dataBody["job_id"] = currentId.toString()
                act.jobId = currentId
                act.onBackPressed()
            }
        }
    }

    private fun initObserve(){
        profileActivityVM.getJbListData().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            Log.e("Peter","getJbList  onNext    ${it.data.size}")
            fl_tag_main.removeAllViews()
            it.data.forEach {
                val textView = ItemJobView(getMContext().get())
                textView.setItemName(it.i18n)
                textView.setOnClickListener(itemOnclick)
                textView.setItemId(it.id)
                fl_tag_main.addView(textView)
            }
        })
    }

    private val itemOnclick= View.OnClickListener {
      val itemView = it as ItemJobView
        itemView.selected()
        currentId = itemView.getItemId()!!
        refreshItem()
        tv_next_step.isClickable = true
        tv_next_step.background = getMContext().get()!!.resources.getDrawable(R.drawable.bg_clickable_btn)
        tv_next_step.setTextColor(getMContext().get()!!.resources.getColor(R.color.colorWhite))
    }

    private fun refreshItem(){
        for(i in 0 until fl_tag_main.childCount){
            val itemView = fl_tag_main[i] as ItemJobView
            if(itemView.getItemId() == currentId){
                itemView.selected()
            } else {
                itemView.unselected()

            }
        }
    }

    private fun callApis(){
        profileActivityVM.getJbList()
    }

}
