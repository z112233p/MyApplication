package com.illa.joliveapp.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.illa.joliveapp.R
import com.illa.joliveapp.activity.CreateEventActivity
import com.illa.joliveapp.activity.MapsActivity
import com.illa.joliveapp.tools.Tools
import com.illa.joliveapp.viewmodle.CreateEventsActivityVM
import kotlinx.android.synthetic.main.fragment_create_event_step_2.*
import kotlinx.android.synthetic.main.fragment_create_event_step_2.ed_event_location
import kotlinx.android.synthetic.main.fragment_create_event_step_2.ed_event_title
import kotlinx.android.synthetic.main.fragment_create_event_step_2.tv_next_step


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "CAST_NEVER_SUCCEEDS")
class FragmentCreateEvent_step2 : BaseFragment() {

    private val createEventsActVM: CreateEventsActivityVM  by activityViewModels()
    private val MAP_ACTIVITY_RESULT_OK = 999
    private var edEventLocationClickable = true
    private var getLatitude = ""
    private var getLongitude = ""
    private var getLocation = ""
    private var getLocationAddress = ""

    private var hasName = false
    private var hasLocation = false
    private var hasPeople = false
    private var hasCost = false
    private var hasReward = true

    private lateinit var act: CreateEventActivity


    override fun getLayoutId(): Int {
        return R.layout.fragment_create_event_step_2
    }

    @SuppressLint("NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        Log.e("Peter","isNavigationViewInit ")

        if(!isNavigationViewInit){//初始化过视图则不再进行view和data初始化
            Log.e("Peter","isNavigationViewInit  ININ ")

            super.onViewCreated(view, savedInstanceState)
            init()
            initObserve()
            callApis()
            checkIntentData()
            isNavigationViewInit = true
        }
    }

    override fun onResume() {
        super.onResume()
        setTitle("建立活動")
        (getMContext().get() as CreateEventActivity).stepTwo()
        act.hidePreview()
        act.hidePost()
    }

    private fun checkIntentData(){
        if(TextUtils.isEmpty(act.eventLabel)){
            return
        } else {
            ed_event_title.setText(act.intentDataBody.data.title)
            ed_event_location.setText(act.intentDataBody.data.location_title)
            ed_event_location_address.setText(act.intentDataBody.data.location_address)
            getLatitude = act.intentDataBody.data.location_gps_latitude
            getLongitude = act.intentDataBody.data.location_gps_longitude
            getLocation = act.intentDataBody.data.location_title
            Log.e("Peter","FragmentCreateEvent_step2   ${act.intentDataBody.data.users_limit}")
            ed_event_users_limit.setText(act.intentDataBody.data.users_limit.toString())
            ed_event_budget.setText(act.intentDataBody.data.budget.toString())
            ed_award_count.setText(act.intentDataBody.data.award_count.toString())

            tv_next_step.isClickable = true
            tv_next_step.background = getMContext().get()!!.resources.getDrawable(R.drawable.bg_clickable_btn)
            tv_next_step.setTextColor(getMContext().get()!!.resources.getColor(R.color.colorWhite))
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    private fun init(){
        act = getMContext().get() as CreateEventActivity

        ed_event_location.setOnClickListener(onClick)
        tv_fix_location.setOnClickListener(onClick)
        tv_next_step.setOnClickListener(onClick)
        tv_next_step.isClickable = false

        ed_event_title.addTextChangedListener {
            if(it!!.isNotEmpty()){
               hasName = true
                checkNextStep()
            }
        }
        ed_event_budget.addTextChangedListener {
            if(it!!.isNotEmpty()){
                hasCost = true
                checkNextStep()
            }
        }
        ed_event_users_limit.addTextChangedListener {
            if(it!!.isNotEmpty()){
                hasPeople = true
                checkNextStep()
            }
        }
        ed_award_count.addTextChangedListener {
            if(it!!.isNotEmpty()){
                hasReward = true
                checkNextStep()
            }
        }


    }


    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n", "ShowToast")
    private val onClick = View.OnClickListener {
        when (it.id) {

            R.id.tv_next_step ->{
                findNavController().navigate(R.id.action_FragmentCreateEvent_step2_to_FragmentCreateEvent_step3)
                (getMContext().get() as CreateEventActivity).dataBody.title = ed_event_title.text.toString()
                (getMContext().get() as CreateEventActivity).dataBody.location_title = ed_event_location.text.toString()
                (getMContext().get() as CreateEventActivity).dataBody.location_address = ed_event_location_address.text.toString()
                (getMContext().get() as CreateEventActivity).dataBody.location_gps_latitude = getLatitude
                (getMContext().get() as CreateEventActivity).dataBody.location_gps_longitude = getLongitude
                (getMContext().get() as CreateEventActivity).dataBody.users_limit = ed_event_users_limit.text.toString()
                (getMContext().get() as CreateEventActivity).dataBody.budget = ed_event_budget.text.toString()
//                (getMContext().get() as CreateEventActivity).dataBody.award_count = ed_award_count.text.toString()
                (getMContext().get() as CreateEventActivity).dataBody.award_count = "1"
            }

            R.id.tv_fix_location -> getMContext().get()?.let { it1 ->
                Log.e("Peter","ISOPGPS   ${Tools.isGPSEnabled(getMContext().get()!!)}")
                if(Tools.isGPSEnabled(getMContext().get()!!)){
                    Log.e("Peter","ISOPGPS   TRUE")

//                    return@OnClickListener

                } else {
                    Log.e("Peter","ISOPGPS   FALSE")

                    Tools.checkGPS(getMContext().get()!!)
                    return@OnClickListener
                }
                val intent = Intent(getMContext().get(), MapsActivity::class.java)

                val b = Bundle()
                b.putString("Latitude", getLatitude)
                b.putString("Longitude", getLongitude)
                b.putString("Location", getLocation)
                intent.putExtras(b)

                startActivityForResult(intent, MAP_ACTIVITY_RESULT_OK)
            }
            R.id.ed_event_location -> getMContext().get()?.let { it1 ->
                if(edEventLocationClickable){
                    if(Tools.isGPSEnabled(getMContext().get()!!)){
                        Log.e("Peter","ISOPGPS   TRUE")

//                    return@OnClickListener

                    } else {
                        Log.e("Peter","ISOPGPS   FALSE")

                        Tools.checkGPS(getMContext().get()!!)
                        return@OnClickListener
                    }


                    val intent = Intent(getMContext().get(), MapsActivity::class.java)

                    val b = Bundle()
                    b.putString("Latitude", getLatitude)
                    b.putString("Longitude", getLongitude)
                    b.putString("Location", getLocation)
                    intent.putExtras(b)


                    startActivityForResult(intent, MAP_ACTIVITY_RESULT_OK)
                    ed_event_location.isClickable = false
                }
            }
        }
    }
    private fun initObserve(){

    }

    private fun callApis(){
//        createEventsActVM.getPaymentMethod()
//        createEventsActVM.getCurrencyType()
//        createEventsActVM.getEventCategory()
    }

    private fun checkNextStep(){
        if(hasCost&& hasLocation && hasName &&
        hasPeople && hasReward ){
            tv_next_step.isClickable = true
            tv_next_step.background = getMContext().get()!!.resources.getDrawable(R.drawable.bg_clickable_btn)
            tv_next_step.setTextColor(getMContext().get()!!.resources.getColor(R.color.colorWhite))
        }
    }

    @SuppressLint("WrongConstant")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == MAP_ACTIVITY_RESULT_OK) {
            if (resultCode == AppCompatActivity.RESULT_OK){
                ed_event_location.setText(data?.getStringExtra("location"))
                ed_event_location_address.setText(data?.getStringExtra("locationAddress"))
                edEventLocationClickable = false
                ed_event_location.isFocusableInTouchMode = true
                ed_event_location.isFocusable = true;

                getLatitude = data?.getStringExtra("latitude")!!
                getLongitude = data.getStringExtra("longitude")!!
                getLocation = data.getStringExtra("location")!!
                getLocationAddress = data.getStringExtra("locationAddress")!!
                hasLocation = true
                checkNextStep()
            }
        }
    }
}
