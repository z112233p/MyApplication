package com.illa.joliveapp.fragment

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.illa.joliveapp.R
import com.illa.joliveapp.activity.CreateEventActivity
import com.illa.joliveapp.tools.Tools
import com.illa.joliveapp.viewmodle.CreateEventsActivityVM
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.android.synthetic.main.fragment_create_event_step_3.*
import kotlinx.android.synthetic.main.fragment_create_event_step_3.tv_next_step
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "CAST_NEVER_SUCCEEDS",
    "EqualsBetweenInconvertibleTypes"
)
class FragmentCreateEvent_step3 : BaseFragment() {

    private val createEventsActVM: CreateEventsActivityVM  by activityViewModels()

    private var nowTime =  Date()
    private var dateSdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
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
//            initObserve()
            checkIntentData()
            isNavigationViewInit = true
        }
    }

    override fun onResume() {
        super.onResume()
        initObserve()
        setTitle("建立活動")
        act.stepThree()
        act.showPreview()
        act.hidePost()
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
        act.showPreview()
        act.setPreviewOnclickListener(View.OnClickListener {
            act.dataBody.description = ed_event_contain.text.toString()
            act.dataBody.currency_id = "1"
            act.dataBody.payment_method_id = "1"
            findNavController().navigate(R.id.action_FragmentCreateEvent_step3_to_FragmentPreviewEvent)
        })
        act.setPreviewUnClick()

        tv_next_step.setOnClickListener(onClick)

        val startTime = dateSdf.parse(act.dataBody.start_time)
        val endTime = dateSdf.parse(act.dataBody.end_time)
        val diffTime = endTime.time - startTime.time

        val diffDay: Int = (diffTime / (1000 * 60 * 60 * 24)).toInt();
        val diffHour: Int = ((diffTime - diffDay * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60)).toInt()
        val diffMinute: Int = ((diffTime - diffDay * (1000 * 60 * 60 * 24) - diffHour * (1000 * 60 * 60)) / (1000 * 60)).toInt()

        var diffTimeString = "${diffDay}天 ${diffHour}小時 ${diffMinute}分"

        if(diffDay != 0 && diffHour != 0 && diffMinute != 0){
            diffTimeString = "${diffDay}天 ${diffHour}小時 ${diffMinute}分"
        } else if(diffDay != 0 && diffHour != 0 && diffMinute == 0){
            diffTimeString = "${diffDay}天 ${diffHour}小時"

        } else if(diffDay != 0 && diffHour == 0 && diffMinute == 0){
            diffTimeString = "${diffDay}天"

        }  else if(diffDay == 0 && diffHour != 0 && diffMinute != 0){
            diffTimeString = "${diffHour}小時 ${diffMinute}分"

        }  else if(diffDay == 0 && diffHour != 0 && diffMinute == 0){
            diffTimeString = "${diffHour}小時"

        }  else if(diffDay == 0 && diffHour == 0 && diffMinute != 0){
            diffTimeString = "${diffMinute}分"

        }


        var strFormat = String.format(getMContext().get()?.resources?.getString(R.string.pre_create_event_content)!!,diffTimeString)
//        ed_event_contain.setText(getMContext().get()?.resources?.getString(R.string.pre_create_event_content))
        ed_event_contain.setText(strFormat)
        ed_event_contain.addTextChangedListener {
            if(it!!.isNotEmpty()){
                tv_next_step.isClickable = true
                tv_next_step.background = getMContext().get()!!.resources.getDrawable(R.drawable.bg_clickable_btn)
                tv_next_step.setTextColor(getMContext().get()!!.resources.getColor(R.color.colorWhite))

                act.setPreviewClick()
            } else {
                act.setPreviewUnClick()

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

                val dataclassAsMap: MutableMap<String, String> =
                    ObjectMapper().convertValue<Map<String, String>>((getMContext().get() as CreateEventActivity).dataBody, object:
                        TypeReference<Map<String, String>>() {}).toMutableMap()
                dataclassAsMap["is_need_approved"] = act.dataBody.is_need_approved
//                dataclassAsMap["1"] = act.dataBody.is_need_approved
//                dataclassAsMap["2"] = act.dataBody.is_need_approved
//                dataclassAsMap["4"] = act.dataBody.is_need_approved
//                dataclassAsMap["image_color5"] = act.dataBody.is_need_approved


                val ketSet = dataclassAsMap.keys.toTypedArray()

                Log.e("Peter","dataclassAsMap:   $dataclassAsMap")
                Log.e("Peter","dataclassAsMap:   ${dataclassAsMap.keys}")
                Log.e("Peter","dataclassAsMap:   ${ketSet[4]}")

                if(TextUtils.isEmpty(act.eventLabel)){
                    Log.e("Peter","CreateEvent :   createEvent")

                    createEventsActVM.createEvent(dataclassAsMap, (getMContext().get() as CreateEventActivity).file)
                } else {
                    Log.e("Peter","CreateEvent :   updateEvent")

                    createEventsActVM.updateEvent(dataclassAsMap, act.file,
                        act.intentDataBody.data.id.toString()
                    )
                }

            }
        }
    }
    private fun initObserve(){
        createEventsActVM.getCreateEventResponse().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            Log.e("Peter","CreateEvent :   getCreateEventResponse   $it")

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
