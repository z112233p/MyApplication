package com.illa.joliveapp.fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.illa.joliveapp.BuildConfig
import com.illa.joliveapp.R
import com.illa.joliveapp.activity.CreateEventActivity
import com.illa.joliveapp.activity.MapsActivity
import com.illa.joliveapp.adapter.Adapter_Event_Type
import com.illa.joliveapp.custom_view.RangeTimePickerDialog
import com.illa.joliveapp.tools.ImgHelper
import com.illa.joliveapp.tools.Tools
import com.illa.joliveapp.viewmodle.CreateEventsActivityVM
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.fragment_create_event_v2.*
import kotlinx.android.synthetic.main.fragment_create_event_v2.tv_event_end_date
import kotlinx.android.synthetic.main.fragment_create_event_v2.tv_event_end_time
import kotlinx.android.synthetic.main.fragment_create_event_v2.tv_event_restriction_date
import kotlinx.android.synthetic.main.fragment_create_event_v2.tv_event_restriction_time
import kotlinx.android.synthetic.main.fragment_create_event_v2.tv_event_start_date
import kotlinx.android.synthetic.main.fragment_create_event_v2.tv_event_start_time
import kotlinx.android.synthetic.main.fragment_create_event_v2.tv_next_step
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "CAST_NEVER_SUCCEEDS")
class FragmentCreateEvent_v2 : BaseFragment() {

    private val createEventsActVM: CreateEventsActivityVM  by activityViewModels()

    private lateinit var calendar : GregorianCalendar
    private lateinit var paymentList: ArrayList<String>
    private lateinit var currencyList: ArrayList<String>
    private lateinit var eventCategoryList: ArrayList<String>
    private lateinit var eventTypeAdapter: Adapter_Event_Type
    private lateinit var act: CreateEventActivity

    private var dateSdf = SimpleDateFormat("yyyy-MM-dd")
    private var timeSdf = SimpleDateFormat("HH:mm:ss")
    private var hourSdf = SimpleDateFormat("HH")
    private var minuteSdf = SimpleDateFormat("mm")
    private var yearSdf = SimpleDateFormat("yyyy")
    private var monthSdf = SimpleDateFormat("MM")
    private var daySdf = SimpleDateFormat("dd")

    private var nowTime =  Date()

    private val MAP_ACTIVITY_RESULT_OK = 999
    private var eventType = "1"

    private var hasPhoto = false
    private var hasType = false
    private var hasStartDay = false
    private var hasEndDay = false
    private var hasRestrictionDay = false

    private var hasStartTime = false
    private var hasEndTime = false
    private var hasRestrictionTime = false

    @SuppressLint("SimpleDateFormat")
    var dateFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")


    override fun getLayoutId(): Int {
        return R.layout.fragment_create_event_v2
    }

    @SuppressLint("NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if(isNavigationViewInit){
           return
        }
            super.onViewCreated(view, savedInstanceState)
        init()
        initObserve()
        callApis()
//        checkIntentData()
    }

    private fun checkIntentData(){

        if(TextUtils.isEmpty(act.eventLabel)){

            return
        } else {
            tv_event_restriction_date.setText(act.intentDataBody.data.review_time.split(" ")[0])
            tv_event_restriction_time.setText(act.intentDataBody.data.review_time.split(" ")[1])
            tv_event_start_date.setText(act.intentDataBody.data.start_time.split(" ")[0])
            tv_event_start_time.setText(act.intentDataBody.data.start_time.split(" ")[1])
            tv_event_end_date.setText(act.intentDataBody.data.end_time.split(" ")[0])
            tv_event_end_time.setText(act.intentDataBody.data.end_time.split(" ")[1])

            ImgHelper.loadNormalImg(getMContext().get(), BuildConfig.IMAGE_URL+act.intentDataBody.data.image, iv_upload_photo)
            eventTypeAdapter.setType(act.intentDataBody.data.events_categorys_id)

            tv_next_step.isClickable = true
            tv_next_step.background = getMContext().get()!!.resources.getDrawable(R.drawable.bg_clickable_btn)
            tv_next_step.setTextColor(getMContext().get()!!.resources.getColor(R.color.colorWhite))
        }
    }

    override fun onResume() {
        super.onResume()
        setTitle("建立活動")
        (getMContext().get() as CreateEventActivity).stepOne()


    }

    override fun onStart() {
        super.onStart()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    private fun init(){
        act = getMContext().get() as CreateEventActivity

        paymentList = ArrayList()
        currencyList = ArrayList()
        eventCategoryList = ArrayList()
        calendar = GregorianCalendar()

        eventTypeAdapter = Adapter_Event_Type(getMContext().get())
        eventTypeAdapter.setOnItemClickListener(object :Adapter_Event_Type.OnItemClickListener{
            override fun onItemClick(Id: String, name: String) {
                eventType = Id
                hasType = true
                checkNextStep()
            }


        })
        val layoutManager =  GridLayoutManager(getMContext().get(), 1, LinearLayoutManager.HORIZONTAL, false)
        rv_event_classify.layoutManager = layoutManager
        rv_event_classify.adapter = eventTypeAdapter

        ll_choose_start_time.setOnClickListener(onClick)
        ll_choose_end_time.setOnClickListener(onClick)
        ll_choose_restriction_time.setOnClickListener(onClick)
        iv_upload_photo.setOnClickListener(onClick)
        tv_next_step.setOnClickListener(onClick)
        tv_next_step.isClickable = false
    }


    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n", "ShowToast")
    private val onClick = View.OnClickListener {
        when(it.id){
            R.id.ll_choose_start_time ->{
                val datePicker = DatePickerDialog(getMContext().get()!!, DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
                    val chooseDate :Date = dateFormat.parse("$year-${month+1}-$day")
                    val chooseDate2 : String? = dateFormat.format(chooseDate)

                    tv_event_start_date.text = chooseDate2
                    hasStartDay = true
                    checkNextStep()
                    val timePicker = RangeTimePickerDialog(getMContext().get()!!, TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
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
                        tv_event_start_time.text = "$hourString:$minuteString:00"
                        if(tv_event_start_date.text == tv_event_end_date.text){
                            tv_event_end_date.text = ""
                            tv_event_end_time.text = ""


                        } else if(tv_event_start_date.text == tv_event_restriction_date.text){
                            tv_event_restriction_date.text = ""
                            tv_event_restriction_time.text = ""
                        }

                        hasStartTime = true
                        checkNextStep()
                    }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false)

                    if(!TextUtils.isEmpty(tv_event_start_time.text)){
                        val hour = tv_event_start_time.text.split(":")[0].toInt()
                        val minute = tv_event_start_time.text.split(":")[1].toInt()

                        timePicker.updateTime(hour,minute)
                    } else {
                        val hour = hourSdf.format(nowTime).toInt()+2
                        val minute = minuteSdf.format(nowTime).toInt()
                        timePicker.updateTime(hour,minute)

                    }

                    if(!TextUtils.isEmpty(tv_event_restriction_time.text) && tv_event_restriction_date.text == tv_event_start_date.text){

                        val minTime = timeSdf.parse(tv_event_restriction_time.text.toString())
                        Log.e("Peter","STARTTIME?? 333    ${tv_event_restriction_time.text}")
                        Log.e("Peter","STARTTIME?? 333    ${tv_event_restriction_time.text.split(":")}")

                        val hour = tv_event_restriction_time.text.split(":")[0].toInt()
                        val minute = tv_event_restriction_time.text.split(":")[1].toInt()

                        timePicker.setMin(hour, minute)
                    } else {
                        val nowDate = dateSdf.format(nowTime)
                        val choosedDate = dateSdf.format(dateSdf.parse(tv_event_start_date.text.toString()))
                        Log.e("Peter","tv_event_restriction_time  minuteSdf  $nowDate   $choosedDate")
                        if(nowDate == chooseDate2){
                            val hour = hourSdf.format(nowTime).toInt()+2
                            val minute = minuteSdf.format(nowTime).toInt()
                            Log.e("Peter","tv_event_restriction_time  minuteSdf  $minute   $hour")

                            timePicker.setMin(hour, minute)
                        }
                    }

                    timePicker.show()

                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
                //tv_event_restriction_date

                if(!TextUtils.isEmpty(tv_event_start_date.text)){
                    val year = tv_event_start_date.text.split("-")[0].toInt()
                    val month = tv_event_start_date.text.split("-")[1].toInt() - 1
                    val day = tv_event_start_date.text.split("-")[2].toInt()
                    datePicker.updateDate(year, month, day)

                } else {
//                    val year = yearSdf.format(nowTime).toInt()+1
//                    val month = monthSdf.format(nowTime).toInt()
//                    val day = daySdf.format(nowTime).toInt()
//                    datePicker.updateDate(year, month, day)

                }

                if(!TextUtils.isEmpty(tv_event_restriction_date.text)){

                    val minDate = dateSdf.parse(tv_event_restriction_date.text.toString())
                    datePicker.datePicker.minDate = minDate.time
                    Log.e("Peter","STARTTIME?? 1 ")

                } else {
                    datePicker.datePicker.minDate = Date().time
                    Log.e("Peter","STARTTIME?? 2   ${Date().time}")

                }
                datePicker.show()
            }

            R.id.ll_choose_end_time ->{
                val datePicker = DatePickerDialog(getMContext().get()!!, DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
                    val chooseDate :Date = dateFormat.parse("$year-${month+1}-$day")
                    val chooseDate2 : String? = dateFormat.format(chooseDate)

                    tv_event_end_date.text = chooseDate2
                    hasEndDay = true
                    checkNextStep()
                    val timePicker = RangeTimePickerDialog(getMContext().get()!!, TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
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
                        tv_event_end_time.text = "$hourString:$minuteString:00"
                        hasEndTime = true
                        checkNextStep()
                    }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false)
                    if(!TextUtils.isEmpty(tv_event_end_time.text)){
                        val hour = tv_event_end_time.text.split(":")[0].toInt()
                        val minute = tv_event_end_time.text.split(":")[1].toInt()

                        timePicker.updateTime(hour,minute)
                    }
                    if(!TextUtils.isEmpty(tv_event_start_time.text) && tv_event_start_date.text == tv_event_end_date.text){
                        val minTime = timeSdf.parse(tv_event_start_time.text.toString())
                        val hour = tv_event_start_time.text.split(":")[0].toInt()
                        val minute = tv_event_start_time.text.split(":")[1].toInt()
                        timePicker.updateTime(hour,minute)

                        timePicker.setMin(hour, minute)
                    }

                    timePicker.show()
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))

                if(!TextUtils.isEmpty(tv_event_end_date.text)){
                    val year = tv_event_end_date.text.split("-")[0].toInt()
                    val month = tv_event_end_date.text.split("-")[1].toInt() - 1
                    val day = tv_event_end_date.text.split("-")[2].toInt()
                    Log.e("Peter","ll_choose_end_time update $year   $month   $day")
                    datePicker.updateDate(year, month, day)

                }
                if(!TextUtils.isEmpty(tv_event_start_date.text)){

                    val minDate = dateSdf.parse(tv_event_start_date.text.toString())
                    datePicker.datePicker.minDate = minDate.time
                }
                //sdf.parse(data.start_time)
                datePicker.show()
            }

            R.id.ll_choose_restriction_time ->{
                val datePicker = DatePickerDialog(getMContext().get()!!, DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
                    val chooseDate :Date = dateFormat.parse("$year-${month+1}-$day")
                    val chooseDate2 : String? = dateFormat.format(chooseDate)

                    tv_event_restriction_date.text = chooseDate2
                    hasRestrictionDay = true
                    checkNextStep()
                    val timePicker = RangeTimePickerDialog(getMContext().get()!!, TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
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
                        tv_event_restriction_time.text = "$hourString:$minuteString:00"
                        hasRestrictionTime = true
                        checkNextStep()
                    }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false)
                    if(!TextUtils.isEmpty(tv_event_restriction_time.text)){
                        val hour = tv_event_restriction_time.text.split(":")[0].toInt()
                        val minute = tv_event_restriction_time.text.split(":")[1].toInt()

                        timePicker.updateTime(hour,minute)
                    }
                    if(!TextUtils.isEmpty(tv_event_restriction_date.text)){

                        val nowDate = dateSdf.format(nowTime)
                        val choosedDate = dateSdf.format(dateSdf.parse(tv_event_restriction_date.text.toString()))
                        Log.e("Peter","tv_event_restriction_time  minuteSdf  $nowDate   $choosedDate")
                        if(nowDate == chooseDate2){
                            val hour = hourSdf.format(nowTime).toInt()+1
                            val minute = minuteSdf.format(nowTime).toInt()
                            Log.e("Peter","tv_event_restriction_time  minuteSdf  $minute   $hour")

                        timePicker.setMin(hour, minute)
                        }

                    }
                    if(!TextUtils.isEmpty(tv_event_start_time.text)){
                        val nowDate = dateSdf.format(dateSdf.parse(tv_event_start_date.text.toString()))
                        val choosedDate = dateSdf.format(dateSdf.parse(tv_event_restriction_date.text.toString()))
                        Log.e("Peter","tv_event_restriction_time  minuteSdf  $nowDate   $choosedDate")
                        if(nowDate == chooseDate2){
//                            val maxHour = hourSdf.format(hourSdf.parse(tv_event_start_time.text.toString())).toInt()
//                            val maxMinute = minuteSdf.format(minuteSdf.parse(tv_event_start_time.text.toString())).toInt()
                            val hour = tv_event_start_time.text.split(":")[0].toInt()
                            val minute = tv_event_start_time.text.split(":")[1].toInt()
                            Log.e("Peter","tv_event_restriction_time  maxHour  $hour   $minute")

                            timePicker.setMax(hour-1, minute)
                            timePicker.updateTime(hour-1, minute)
                        }


                    }

                    timePicker.show()

                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))

                if(!TextUtils.isEmpty(tv_event_restriction_date.text)){
                    val year = tv_event_restriction_date.text.split("-")[0].toInt()
                    val month = tv_event_restriction_date.text.split("-")[1].toInt() - 1
                    val day = tv_event_restriction_date.text.split("-")[2].toInt()
                    Log.e("Peter","ll_choose_end_time update $year   $month   $day")
                    datePicker.updateDate(year, month, day)

                }
                if(!TextUtils.isEmpty(tv_event_start_date.text)){
                    val maxDate = dateSdf.parse(tv_event_start_date.text.toString())
                    datePicker.datePicker.maxDate = maxDate.time
                }
                datePicker.datePicker.minDate = Date().time
                datePicker.show()
            }


            R.id.iv_upload_photo -> {//iv_upload_photo//iv_event_photo
                getMContext().get()?.let { ctx ->
                    CropImage.activity().setAspectRatio(345,224).start(ctx,this@FragmentCreateEvent_v2)
                }
            }
            R.id.btn_goto_map_activity -> getMContext().get()?.let { it1 ->
                val intent = Intent(getMContext().get(), MapsActivity::class.java)
                startActivityForResult(intent, MAP_ACTIVITY_RESULT_OK)
            }

            R.id.tv_next_step -> {
                findNavController().navigate(R.id.action_FragmentCreateEvent_v2_to_FragmentCreateEvent_step2)
                (getMContext().get() as CreateEventActivity).dataBody.start_time = tv_event_start_date.text.toString()+" "+tv_event_start_time.text.toString()
                (getMContext().get() as CreateEventActivity).dataBody.end_time = tv_event_end_date.text.toString()+" "+tv_event_end_time.text.toString()
                (getMContext().get() as CreateEventActivity).dataBody.review_time = tv_event_restriction_date.text.toString()+" "+tv_event_restriction_time.text.toString()
                (getMContext().get() as CreateEventActivity).dataBody.events_categorys_id = eventType

            }
        }
    }

    private fun initObserve(){
        createEventsActVM.getEventCategoryData().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it.data.forEach {
                eventCategoryList.add(it.id.toString())
            }
            eventTypeAdapter.setData(it.data)

        })


        createEventsActVM.getEventDetail().observe(viewLifecycleOwner,  androidx.lifecycle.Observer {
            Log.e("Peter","CreateEventActivity getEventDetail $it")

            act.intentDataBody = it
            checkIntentData()

        })

    }

    private fun callApis(){
        Log.e("Peter","CreateEventActivity getIntentData V@  ${act.eventLabel}")

        createEventsActVM.getEventDetail(act.eventLabel)
        createEventsActVM.getPaymentMethod()
        createEventsActVM.getCurrencyType()
        createEventsActVM.getEventCategory()
    }



    private fun checkNextStep(){
        if(hasPhoto && hasType && hasStartDay && hasEndDay &&
            hasRestrictionDay && hasStartTime && hasEndTime && hasRestrictionTime ){
            tv_next_step.isClickable = true
            tv_next_step.background = getMContext().get()!!.resources.getDrawable(R.drawable.bg_clickable_btn)
            tv_next_step.setTextColor(getMContext().get()!!.resources.getColor(R.color.colorWhite))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == AppCompatActivity.RESULT_OK){
                val result = CropImage.getActivityResult(data)
                Tools.saveCropImage(result.uri)
                (getMContext().get() as CreateEventActivity).file = Tools.dealCropImage()
                Tools.deleteCropImage()
                ImgHelper.loadNormalImg(getMContext().get(), result.uri.toString(), iv_upload_photo)
                iv_upload_photo_icon.visibility = View.GONE
                hasPhoto = true
                checkNextStep()
            }
        }
    }
}
