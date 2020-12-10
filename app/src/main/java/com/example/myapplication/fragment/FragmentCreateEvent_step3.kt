package com.example.myapplication.fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.activity.CreateEventActivity
import com.example.myapplication.activity.MapsActivity
import com.example.myapplication.adapter.Adapter_Event_Type
import com.example.myapplication.datamodle.event.create.CreateEvent
import com.example.myapplication.tools.ImgHelper
import com.example.myapplication.tools.Tools
import com.example.myapplication.viewmodle.CreateEventsActivityVM
import com.example.myapplication.viewmodle.EventsActivityVM
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.fragment_create_event_step_2.*
import kotlinx.android.synthetic.main.fragment_create_event_step_3.*
import kotlinx.android.synthetic.main.fragment_create_event_step_3.tv_next_step
import okhttp3.internal.format
import org.json.JSONObject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "CAST_NEVER_SUCCEEDS")
class FragmentCreateEvent_step3 : BaseFragment() {

    private val createEventsActVM: CreateEventsActivityVM  by activityViewModels()
    private lateinit var act: CreateEventActivity



    override fun getLayoutId(): Int {
        return R.layout.fragment_create_event_step_3
    }

    @SuppressLint("NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        Log.e("Peter","isNavigationViewInit ")

        if(!isNavigationViewInit){//初始化过视图则不再进行view和data初始化
            Log.e("Peter","isNavigationViewInit  ININ ")

            super.onViewCreated(view, savedInstanceState)
            init()
            initObserve()
            checkIntentData()
            isNavigationViewInit = true
        }
    }

    override fun onResume() {
        super.onResume()
        setTitle("建立活動")

    }

    private fun checkIntentData(){
        if(TextUtils.isEmpty(act.eventLabel)){
            return
        } else {
         ed_event_contain.setText(act.intentDataBody.data.description)
            tv_next_step.isClickable = true
            tv_next_step.background = getMContext().get()!!.resources.getDrawable(R.drawable.bg_clickable_btn)
            tv_next_step.setTextColor(getMContext().get()!!.resources.getColor(R.color.colorWhite))
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    private fun init(){
        act = getMContext().get() as CreateEventActivity
        act.stepThree()

        tv_next_step.setOnClickListener(onClick)
        ed_event_contain.addTextChangedListener {
            if(it!!.isNotEmpty()){
                tv_next_step.isClickable = true
                tv_next_step.background = getMContext().get()!!.resources.getDrawable(R.drawable.bg_clickable_btn)
                tv_next_step.setTextColor(getMContext().get()!!.resources.getColor(R.color.colorWhite))
            }
        }

    }


    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n", "ShowToast")
    private val onClick = View.OnClickListener {
        when (it.id) {
            R.id.tv_next_step ->{
                (getMContext().get() as CreateEventActivity).dataBody.description = ed_event_contain.text.toString()
                (getMContext().get() as CreateEventActivity).dataBody.currency_id = "1"
                (getMContext().get() as CreateEventActivity).dataBody.payment_method_id = "1"

                Log.e("Peter","CreateEvent dataBody :   $(getMContext().get() as CreateEventActivity).dataBody")

                val dataclassAsMap =
                    ObjectMapper().convertValue<Map<String, String>>((getMContext().get() as CreateEventActivity).dataBody, object:
                    TypeReference<Map<String, String>>() {})
                val ketSet = dataclassAsMap.keys.toTypedArray()

                Log.e("Peter","dataclassAsMap:   $dataclassAsMap")
                Log.e("Peter","dataclassAsMap:   ${dataclassAsMap.keys}")
                Log.e("Peter","dataclassAsMap:   ${ketSet[4]}")

                if(TextUtils.isEmpty(act.eventLabel)){
                    createEventsActVM.createEvent(dataclassAsMap, (getMContext().get() as CreateEventActivity).file)
                } else {
                    createEventsActVM.updateEvent(dataclassAsMap, act.file,
                        act.intentDataBody.data.id.toString()
                    )
                }

            }
        }
    }
    private fun initObserve(){
        createEventsActVM.getCreateEventResponse().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if(!TextUtils.isEmpty(it)){
                val jsonData = JSONObject(it)
                if (jsonData.get("code") == 0){
                    Tools.toast(getMContext().get(), "開團成功")
                    act.finish()


                } else {
                    Tools.toast(getMContext().get(), jsonData.get("data").toString())

                }
            }
        })

        createEventsActVM.getUpdateEventResponse().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if(!TextUtils.isEmpty(it)){
                Tools.toast(getMContext().get(), "修改成功")
                act.finish()
            }
        })

        createEventsActVM.getErrorMsg().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            Tools.toast(getMContext().get(),it)
        })
    }

}
