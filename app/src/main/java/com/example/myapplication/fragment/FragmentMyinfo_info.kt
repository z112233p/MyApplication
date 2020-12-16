package com.example.myapplication.fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.myapplication.BuildConfig
import com.example.myapplication.MyApp
import com.example.myapplication.R
import com.example.myapplication.activity.CreateEventActivity
import com.example.myapplication.activity.ProfileActivity
import com.example.myapplication.adapter.Adapter_Events
import com.example.myapplication.custom_view.ItemJobView
import com.example.myapplication.dialog.DialogIGLogin
import com.example.myapplication.tools.ImgHelper
import com.example.myapplication.tools.IntentHelper
import com.example.myapplication.tools.PrefHelper
import com.example.myapplication.tools.Tools
import com.example.myapplication.viewmodle.ProfileActivityVM
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_event_detail.*
import kotlinx.android.synthetic.main.fragment_creat_profile_step_2.*
import kotlinx.android.synthetic.main.fragment_creat_profile_step_3.*
import kotlinx.android.synthetic.main.fragment_creat_profile_step_3.tv_next_step
import kotlinx.android.synthetic.main.fragment_my_info_detail.*
import kotlinx.android.synthetic.main.item_custom_incoming_message.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap
import kotlin.math.abs


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "CAST_NEVER_SUCCEEDS", "DEPRECATION")
class FragmentMyinfo_info : BaseFragment() {

    private val profileActivityVM: ProfileActivityVM by activityViewModels()
    private lateinit var myEventAdapter: Adapter_Events

    override fun getLayoutId(): Int {
        return R.layout.fragment_my_info_detail

    }

    @SuppressLint("NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.e("Peter","isNavigationViewInit ")

        if(!isNavigationViewInit){
            Log.e("Peter","isNavigationViewInit  ININ ")
            super.onViewCreated(view, savedInstanceState)
            init()
            initObserve()
            isNavigationViewInit = true
        }
    }

    override fun onResume() {
        super.onResume()
        callApis()

//        setTitle("Hello.")
    }


    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    private fun init(){
        tv_follow_btn.setOnClickListener(onClick)


        myEventAdapter = Adapter_Events(getMContext().get(), 3, false)
        val layoutManager =  LinearLayoutManager(getMContext().get(), LinearLayoutManager.HORIZONTAL, false)
        rv_my_events.layoutManager = layoutManager
        rv_my_events.adapter = myEventAdapter

        myEventAdapter.setOnItemClickListener(object : Adapter_Events.OnItemClickListener{
            override fun onItemClick(view: View?, position: Int, label: String) {
                getMContext().get()?.let {
                    IntentHelper.gotoEventDetailActivity(it, label)
                }
            }
        })
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
            R.id.tv_follow_btn -> {
                val dialog = getMContext().get()?.let { it1 -> DialogIGLogin(it1) }
                dialog?.show()
            }

        }
    }


    private fun initObserve(){
        profileActivityVM.getMyInfoData().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            ImgHelper.loadNormalImgNoCache(getMContext().get(),BuildConfig.CHATROOM_IMAGE_URL+"dating/"+ it.user.label +".jpg", iv_my_photo)
            tv_name.text = it.user.nickname
            val birth = "Taipei City, "+it.user.age+", "+Tools.getConstellation(it.user.birthday.split("-")[1],it.user.birthday.split("-")[2])
            tv_birth.text = birth

            var interests = ""
            it.user.interest_map?.forEach { id ->
                if(id == it.user.interest_map!!.last()){
                    interests += MyApp.get()!!.getInterest(id)

                } else {
                    interests += MyApp.get()!!.getInterest(id)+", "

                }
            }
            tv_my_interest.text = interests
            tv_my_job.text = MyApp.get()!!.getJob(it.user.job_id.toInt())
            if(it.user.gender == 0){
                iv_gender.setImageDrawable(getMContext().get()?.resources?.getDrawable(R.mipmap.ic_gender_woman))
            } else {
                iv_gender.setImageDrawable(getMContext().get()?.resources?.getDrawable(R.mipmap.ic_gender_man))

            }
            tv_about_me_data.text = it.user.about
        })

        profileActivityVM.getMyEventsData().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            myEventAdapter.setData(it.data.processing)
        })
    }

    private fun callApis(){
        profileActivityVM.getMyInfo()
        profileActivityVM.getEventsApi(PrefHelper.chatLable, null)
        profileActivityVM.getMyEvents()
    }

}
