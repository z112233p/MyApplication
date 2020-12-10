package com.example.myapplication.fragment

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
import com.example.myapplication.BuildConfig
import com.example.myapplication.R
import com.example.myapplication.activity.CreateEventActivity
import com.example.myapplication.activity.MapsActivity
import com.example.myapplication.adapter.Adapter_Event_Type
import com.example.myapplication.tools.ImgHelper
import com.example.myapplication.tools.Tools
import com.example.myapplication.viewmodle.CreateEventsActivityVM
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
            override fun onItemClick(Id: String) {
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
                DatePickerDialog(getMContext().get()!!, DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
                    val chooseDate :Date = dateFormat.parse("$year-${month+1}-$day")
                    val chooseDate2 : String? = dateFormat.format(chooseDate)

                    tv_event_start_date.text = chooseDate2
                    hasStartDay = true
                    checkNextStep()
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
                        tv_event_start_time.text = "$hourString:$minuteString:00"
                        hasStartTime = true
                        checkNextStep()
                    }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show()

                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
            }

            R.id.ll_choose_end_time ->{
                DatePickerDialog(getMContext().get()!!, DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
                    val chooseDate :Date = dateFormat.parse("$year-${month+1}-$day")
                    val chooseDate2 : String? = dateFormat.format(chooseDate)

                    tv_event_end_date.text = chooseDate2
                    hasEndDay = true
                    checkNextStep()
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
                        tv_event_end_time.text = "$hourString:$minuteString:00"
                        hasEndTime = true
                        checkNextStep()
                    }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show()

                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
            }

            R.id.ll_choose_restriction_time ->{
                DatePickerDialog(getMContext().get()!!, DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
                    val chooseDate :Date = dateFormat.parse("$year-${month+1}-$day")
                    val chooseDate2 : String? = dateFormat.format(chooseDate)

                    tv_event_restriction_date.text = chooseDate2
                    hasRestrictionDay = true
                    checkNextStep()
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
                        tv_event_restriction_time.text = "$hourString:$minuteString:00"
                        hasRestrictionTime = true
                        checkNextStep()
                    }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show()

                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
            }


            R.id.iv_upload_photo -> {//iv_upload_photo//iv_event_photo
                getMContext().get()?.let { ctx ->
                    CropImage.activity().start(ctx,this@FragmentCreateEvent_v2)
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
