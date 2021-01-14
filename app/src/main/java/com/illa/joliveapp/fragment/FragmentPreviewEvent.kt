package com.illa.joliveapp.fragment

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.illa.joliveapp.R
import com.illa.joliveapp.activity.CreateEventActivity
import com.illa.joliveapp.tools.Tools
import com.illa.joliveapp.viewmodle.CreateEventsActivityVM
import kotlinx.android.synthetic.main.fragment_create_event_step_3.*
import kotlinx.android.synthetic.main.fragment_event_main_v2.*
import org.json.JSONObject


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "CAST_NEVER_SUCCEEDS",
    "UNREACHABLE_CODE"
)
class FragmentPreviewEvent : BaseFragment() {
    private val createEventsActVM: CreateEventsActivityVM by activityViewModels()

    private lateinit var act: CreateEventActivity

    override fun getLayoutId(): Int {
        return R.layout.fragment_event_main_v2
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
            setHasOptionsMenu(true)
        }
    }

    override fun onResume() {
        Log.e("Peter","FragmentEventDetailV2 onResume")
        super.onResume()
        act.hidePreview()
        v_divider_line.visibility = View.INVISIBLE
        tv_you_may_like.visibility = View.INVISIBLE
        rv_events.visibility = View.INVISIBLE

    }


    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    private fun init(){
        act = getMContext().get() as CreateEventActivity
        act.setPostOnclickListener(View.OnClickListener {


            Log.e("Peter","CreateEvent dataBody :   $(getMContext().get() as CreateEventActivity).dataBody")

            val dataclassAsMap =
                ObjectMapper().convertValue<Map<String, String>>((getMContext().get() as CreateEventActivity).dataBody, object:
                    TypeReference<Map<String, String>>() {})
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
        })
        tv_event_start_time.text = act.dataBody.start_time
        tv_event_location.text = act.dataBody.location_title
        tv_location_detail.text = act.dataBody.location_address
        tv_event_title.text = act.dataBody.title
        tv_event_budget.text = act.dataBody.budget
        tv_event_users_limit.text = act.dataBody.users_limit+ "人"
        tv_event_reward.text = act.dataBody.award_count
        tv_event_content.text = act.dataBody.description

        val myBitmap = BitmapFactory.decodeFile(act.file.getAbsolutePath())


        iv_event_photo.setImageBitmap(myBitmap)

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

        createEventsActVM.getErrorMsg().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            Tools.toast(getMContext().get(),it)
        })
    }

}
