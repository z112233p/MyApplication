package com.illa.joliveapp.fragment

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import com.illa.joliveapp.R
import com.illa.joliveapp.activity.CreateEventActivity
import kotlinx.android.synthetic.main.fragment_event_main_v2.*


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "CAST_NEVER_SUCCEEDS",
    "UNREACHABLE_CODE"
)
class FragmentPreviewEvent : BaseFragment() {

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
//            initObserve()
            isNavigationViewInit = true
            setHasOptionsMenu(true)
        }
    }

    override fun onResume() {
        Log.e("Peter","FragmentEventDetailV2 onResume")
        super.onResume()
        act.hidePreview()


    }


    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    private fun init(){
        act = getMContext().get() as CreateEventActivity

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



}
