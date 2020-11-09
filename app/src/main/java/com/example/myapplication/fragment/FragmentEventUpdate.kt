package com.example.myapplication.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.BuildConfig
import com.example.myapplication.R
import com.example.myapplication.activity.MapsActivity
import com.example.myapplication.datamodle.event.create.CreateEvent
import com.example.myapplication.datamodle.event.detail.Data
import com.example.myapplication.tools.Config
import com.example.myapplication.tools.ImgHelper
import com.example.myapplication.tools.Tools
import com.example.myapplication.viewmodle.EventsActivityVM
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.fragment_create_event.*
import kotlinx.android.synthetic.main.fragment_create_event.btn_choose_event_end_date
import kotlinx.android.synthetic.main.fragment_create_event.btn_choose_event_end_time
import kotlinx.android.synthetic.main.fragment_create_event.btn_choose_event_restriction_date
import kotlinx.android.synthetic.main.fragment_create_event.btn_choose_event_restriction_time
import kotlinx.android.synthetic.main.fragment_create_event.btn_choose_event_start_date
import kotlinx.android.synthetic.main.fragment_create_event.btn_choose_event_start_time
import kotlinx.android.synthetic.main.fragment_create_event.btn_goto_map_activity
import kotlinx.android.synthetic.main.fragment_create_event.iv_event_photo
import kotlinx.android.synthetic.main.fragment_create_event.tv_event_description
import kotlinx.android.synthetic.main.fragment_create_event.tv_event_end_time
import kotlinx.android.synthetic.main.fragment_create_event.tv_event_location
import kotlinx.android.synthetic.main.fragment_create_event.tv_event_restriction_time
import kotlinx.android.synthetic.main.fragment_create_event.tv_event_start_time
import kotlinx.android.synthetic.main.fragment_create_event.tv_event_title
import kotlinx.android.synthetic.main.fragment_event_detial.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class FragmentEventUpdate : BaseFragment(){

    private val eventsActivityVM : EventsActivityVM by activityViewModels()

    private lateinit var calendar : GregorianCalendar
    private lateinit var paymentList: ArrayList<String>
    private lateinit var currencyList: ArrayList<String>
    private lateinit var eventCategoryList: ArrayList<String>
    private lateinit var eventDetailData: Data

    private var file : File? = File("")
    private val MAP_ACTIVITY_RESULT_OK = 999
    private var getLatitude = ""
    private var getLongitude = ""
    private var eventType = 0
    private var currencyType = 0
    private var paymentMethod = 0
    private var eventID: String = ""


    @SuppressLint("SimpleDateFormat")
    var dateFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")


    override fun getLayoutId(): Int {
        return R.layout.fragment_create_event
    }

    @SuppressLint("NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initSpinners()
        initObserve()
        callApis()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    private fun init(){
        paymentList = ArrayList()
        currencyList = ArrayList()
        eventCategoryList = ArrayList()
        calendar = GregorianCalendar()

        btn_choose_event_restriction_date.setOnClickListener(onClick)
        btn_choose_event_restriction_time.setOnClickListener(onClick)
        btn_choose_event_start_date.setOnClickListener(onClick)
        btn_choose_event_start_time.setOnClickListener(onClick)
        btn_choose_event_end_date.setOnClickListener(onClick)
        btn_choose_event_end_time.setOnClickListener(onClick)
        btn_confirm.setOnClickListener(onClick)

        iv_event_photo.setOnClickListener(onClick)
        btn_goto_map_activity.setOnClickListener(onClick)

        initUI()
    }

    private fun initUI(){
        eventDetailData = arguments?.getParcelable("eventDetailData")!!
        eventID = eventDetailData.id.toString()

        ImgHelper.loadNormalImg(getMContext().get(), BuildConfig.IMAGE_URL+eventDetailData.image, iv_event_photo)
        ed_event_title.setText(eventDetailData.title)
        ed_event_description.setText(eventDetailData.description)

        ed_event_restriction_date.setText(eventDetailData.review_time.split(" ")[0])
        ed_event_restriction_time.setText(eventDetailData.review_time.split(" ")[1])
        ed_event_start_date.setText(eventDetailData.start_time.split(" ")[0])
        ed_event_start_time.setText(eventDetailData.start_time.split(" ")[1])
        ed_event_end_date.setText(eventDetailData.end_time.split(" ")[0])
        ed_event_end_time.setText(eventDetailData.end_time.split(" ")[1])

        sp_event_type.setSelection(eventDetailData.events_categorys_id)
        sp_payment_method.setSelection(eventDetailData.payment_method_id)
        sp_currency_type.setSelection(eventDetailData.currency_id)

        ed_event_users_limit.setText(eventDetailData.users_limit.toString())
        ed_event_budget.setText(eventDetailData.budget.toString())
        ed_event_location.setText(eventDetailData.location_title)

        getLatitude = eventDetailData.location_gps_latitude
        getLongitude = eventDetailData.location_gps_longitude
    }




    private fun initSpinners(){
        sp_currency_type.onItemSelectedListener = onItemSelectedListener
        sp_event_type.onItemSelectedListener = onItemSelectedListener
        sp_payment_method.onItemSelectedListener = onItemSelectedListener

    }

    private val onItemSelectedListener= object : AdapterView.OnItemSelectedListener{
        override fun onNothingSelected(p0: AdapterView<*>?) {
            Log.e("peter","sp_payment_method   onNothingSelected ")
        }

        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            when(p0?.id){
                R.id.sp_currency_type -> currencyType = p2
                R.id.sp_event_type -> eventType = p2
                R.id.sp_payment_method -> paymentMethod = p2
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n", "ShowToast")
    private val onClick = View.OnClickListener {
        when(it.id){
            R.id.btn_choose_event_restriction_date ->{
                DatePickerDialog(getMContext().get()!!, DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
                    val chooseDate : Date = dateFormat.parse("$year-${month+1}-$day")
                    val chooseDate2 : String? = dateFormat.format(chooseDate)

                    ed_event_restriction_date.setText(chooseDate2)
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
            }
            R.id.btn_choose_event_start_date ->{
                DatePickerDialog(getMContext().get()!!, DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
                    val chooseDate : Date = dateFormat.parse("$year-${month+1}-$day")
                    val chooseDate2 : String? = dateFormat.format(chooseDate)

                    ed_event_start_date.setText(chooseDate2)
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
            }
            R.id.btn_choose_event_end_date ->{
                DatePickerDialog(getMContext().get()!!, DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
                    val chooseDate : Date = dateFormat.parse("$year-${month+1}-$day")
                    val chooseDate2 : String? = dateFormat.format(chooseDate)

                    ed_event_end_date.setText(chooseDate2)
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
            }
            R.id.btn_choose_event_restriction_time ->{
                TimePickerDialog(getMContext().get()!!, TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                    val hourString = if(hour.toString().length < 2){
                        "0$hour"
                    } else {
                        hour.toString()
                    }
                    val minuteString = if(minute.toString().length < 2){
                        "0$minute"
                    } else {
                        minute.toString()
                    }
                    ed_event_restriction_time.setText("$hourString:$minuteString:00")
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show()
            }
            R.id.btn_choose_event_start_time ->{
                TimePickerDialog(getMContext().get()!!, TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                    val hourString = if(hour.toString().length < 2){
                        "0$hour"
                    } else {
                        hour.toString()
                    }
                    val minuteString = if(minute.toString().length < 2){
                        "0$minute"
                    } else {
                        minute.toString()
                    }
                    ed_event_start_time.setText("$hourString:$minuteString:00")
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show()
            }
            R.id.btn_choose_event_end_time ->{
                TimePickerDialog(getMContext().get()!!, TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                    val hourString = if(hour.toString().length < 2){
                        "0$hour"
                    } else {
                        hour.toString()
                    }
                    val minuteString = if(minute.toString().length < 2){
                        "0$minute"
                    } else {
                        minute.toString()
                    }
                    ed_event_end_time.setText("$hourString:$minuteString:00")
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show()
            }
            R.id.iv_event_photo -> {
                getMContext().get()?.let { ctx ->
                    CropImage.activity().start(ctx,this@FragmentEventUpdate)
                }
            }
            R.id.btn_goto_map_activity -> getMContext().get()?.let { it1 ->
                val intent = Intent(getMContext().get(), MapsActivity::class.java)
                startActivityForResult(intent, MAP_ACTIVITY_RESULT_OK)
            }
            R.id.btn_confirm ->{
                val dataBody = CreateEvent()
                dataBody.title = ed_event_title.text.toString()
                dataBody.description = ed_event_description.text.toString()
                dataBody.events_categorys_id = eventType.toString()
                dataBody.payment_method_id = paymentMethod.toString()
                dataBody.location_title = ed_event_location.text.toString()
                dataBody.location_gps_latitude = getLatitude
                dataBody.location_gps_longitude = getLongitude
                dataBody.meeting_title = ed_event_location.text.toString()
                dataBody.metting_gps_latitude = getLatitude
                dataBody.metting_gps_longitude = getLongitude
                dataBody.users_limit = ed_event_users_limit.text.toString()
                dataBody.start_time = ed_event_start_date.text.toString()+" "+ed_event_start_time.text.toString()
                dataBody.end_time = ed_event_end_date.text.toString()+" "+ed_event_end_time.text.toString()
                dataBody.budget = ed_event_budget.text.toString()
                dataBody.currency_id = currencyType.toString()
                dataBody.review_time = ed_event_restriction_date.text.toString()+" "+ed_event_restriction_time.text.toString()
                dataBody.award_count = "30"

                Log.e("Peter","CreateEvent dataBody :   $dataBody")

                val dataclassAsMap = ObjectMapper().convertValue<Map<String, String>>(dataBody, object:
                    TypeReference<Map<String, String>>() {})
                val ketSet = dataclassAsMap.keys.toTypedArray()

                Log.e("Peter","dataclassAsMap:   $dataclassAsMap")
                Log.e("Peter","dataclassAsMap:   ${dataclassAsMap.keys}")
                Log.e("Peter","dataclassAsMap:   ${ketSet[4]}")
                Log.e("Peter","file check   ${file?.exists()}")

                eventsActivityVM.updateEvent(dataclassAsMap, file,eventID)
            }
        }
    }

    private fun initObserve(){
        eventsActivityVM.getPaymentMethodData().observe(viewLifecycleOwner, androidx.lifecycle.Observer { it ->
            paymentList.clear()
            it.data.forEach {
                paymentList.add(it.i18n)
            }
            val listAdapter = ArrayAdapter<String>(getMContext().get()!!, android.R.layout.simple_spinner_item, paymentList)
            sp_payment_method.adapter = listAdapter
            sp_payment_method.setSelection(eventDetailData.payment_method_id)

        })
        eventsActivityVM.getCurrencyTypeData().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            currencyList.clear()
            it.data.forEach {
                currencyList.add(it.i18n)
            }
            val listAdapter = ArrayAdapter<String>(getMContext().get()!!, android.R.layout.simple_spinner_item, currencyList)
            sp_currency_type.adapter = listAdapter
            sp_currency_type.setSelection(eventDetailData.currency_id)

        })
        eventsActivityVM.getEventCategoryData().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            eventCategoryList.clear()
            it.data.forEach {
                eventCategoryList.add(it.i18n)
            }
            val listAdapter = ArrayAdapter<String>(getMContext().get()!!, android.R.layout.simple_spinner_item, eventCategoryList)
            sp_event_type.adapter = listAdapter
            sp_event_type.setSelection(eventDetailData.events_categorys_id)

        })
        eventsActivityVM.getErrorMsg().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if(it == "FINISH"){
                Tools.toast(getMContext().get(), "修改成功")
                val bundle = bundleOf(Config.EVENT_LABEL to eventDetailData.label)
                (getMContext().get() as Activity).onBackPressed()
            } else {
                Tools.toast(getMContext().get(), it)
            }
        })
    }

    private fun callApis(){
        eventsActivityVM.getPaymentMethod()
        eventsActivityVM.getCurrencyType()
        eventsActivityVM.getEventCategory()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == AppCompatActivity.RESULT_OK){
                val result = CropImage.getActivityResult(data)
                Tools.saveCropImage(result.uri)
                file = Tools.dealCropImage()//File(result.uri.path)
                Tools.deleteCropImage()
                ImgHelper.loadNormalImg(getMContext().get(), result.uri.toString(), iv_event_photo)
            }
        }
        if (requestCode == MAP_ACTIVITY_RESULT_OK) {
            if (resultCode == AppCompatActivity.RESULT_OK){
                ed_event_location.setText(data?.getStringExtra("location"))
                getLatitude = data?.getStringExtra("latitude")!!
                getLongitude = data.getStringExtra("longitude")!!

            }
        }
    }
}