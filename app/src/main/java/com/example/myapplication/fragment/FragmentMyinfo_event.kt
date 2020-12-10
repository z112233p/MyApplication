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
import com.example.myapplication.adapter.Adapter_My_Events
import com.example.myapplication.custom_view.ItemJobView
import com.example.myapplication.tools.ImgHelper
import com.example.myapplication.tools.IntentHelper
import com.example.myapplication.tools.PrefHelper
import com.example.myapplication.tools.Tools
import com.example.myapplication.viewmodle.ProfileActivityVM
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.fragment_my_events_history.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap
import kotlin.math.abs


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


        profileActivityVM.getEvents().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            myEventAdapter.setData(it.data.event)
            historyEventAdapter.setData(it.data.event)

        })
    }

    private fun callApis(){
//        profileActivityVM.getMyInfo()
        profileActivityVM.getEventsApi(PrefHelper.chatLable)
    }

}
