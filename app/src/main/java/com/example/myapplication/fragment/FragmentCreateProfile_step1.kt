package com.example.myapplication.fragment

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.view.get
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.activity.ProfileActivity
import com.example.myapplication.custom_view.ItemInterestView
import com.example.myapplication.custom_view.ItemJobView
import com.example.myapplication.viewmodle.ProfileActivityVM
import kotlinx.android.synthetic.main.fragment_creat_profile_step_1.*
import kotlinx.android.synthetic.main.fragment_creat_profile_step_1.fl_tag_main
import kotlinx.android.synthetic.main.fragment_creat_profile_step_1.tv_next_step
import kotlinx.android.synthetic.main.fragment_creat_profile_step_2.*


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "CAST_NEVER_SUCCEEDS")
class FragmentCreateProfile_step1 : BaseFragment() {

    private val profileActivityVM: ProfileActivityVM by activityViewModels()
    private var nextStepCheck = 0
    private var selectedPosition = 0
    private lateinit var act: ProfileActivity


    override fun getLayoutId(): Int {
        return R.layout.fragment_creat_profile_step_1
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
        setTitle("Hello.")
        (getMContext().get() as ProfileActivity).setStepOne()
        act.dataBody.keys.forEach {
            if(it.contains("interests[")){
                act.dataBody.remove(it)
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    private fun init(){
        tv_next_step.isClickable = false
        act = getMContext().get() as ProfileActivity
        tv_next_step.setOnClickListener(onClick)
        tv_next_step.isClickable = false

    }


    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n", "ShowToast")
    private val onClick = View.OnClickListener {
        when (it.id) {

            R.id.tv_next_step -> {
                for(i in 0 until fl_tag_main.childCount){
                    val itemView = fl_tag_main[i] as ItemInterestView
                    if(itemView.getIsSelected()!!){
                        act.dataBody["interests[$selectedPosition]"] = itemView.getItemId().toString()
                        selectedPosition++
                    }
                }

                findNavController().navigate(R.id.action_FragmentCreateProfile_step1_to_FragmentCreateProfile_step2)
            }
        }
    }

    private fun initObserve() {
        profileActivityVM.getInterestListData()
            .observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                Log.e("Peter", "getInterestList  onNext    ${it.data[0].color}")
                fl_tag_main.removeAllViews()

                it.data.forEach {
                    val textView = ItemInterestView(getMContext().get())
                    textView.setItemName(it.i18n)
                    textView.setSelectedColor(it.color)
                    textView.setItemId(it.id)
                    textView.setItemOnclick(object :ItemInterestView.onItemClick{
                        override fun onClick() {
                            nextStepCheck = 0
                            for(i in 0 until fl_tag_main.childCount){
                                val itemView = fl_tag_main[i] as ItemInterestView
                                if(itemView.getIsSelected()!!){
                                   nextStepCheck++
                                }
                            }
                            Log.e("Peter", "nextStepCheck $nextStepCheck  ")

                            if(nextStepCheck > 0){
                                tv_next_step.isClickable = true
                                tv_next_step.background = getMContext().get()!!.resources.getDrawable(R.drawable.bg_clickable_btn)
                                tv_next_step.setTextColor(getMContext().get()!!.resources.getColor(R.color.colorWhite))
                            } else {
                                tv_next_step.isClickable = false
                                tv_next_step.background = getMContext().get()!!.resources.getDrawable(R.drawable.bg_unclickable_btn)
                                tv_next_step.setTextColor(getMContext().get()!!.resources.getColor(R.color.colorGray70))



                            }
                        }

                    })
                    fl_tag_main.addView(textView)
                }
            })
    }

    private fun callApis(){
        profileActivityVM.getInterestList()
    }

}
