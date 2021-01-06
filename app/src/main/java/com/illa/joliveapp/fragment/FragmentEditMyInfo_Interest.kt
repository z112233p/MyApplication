package com.illa.joliveapp.fragment

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.view.get
import androidx.fragment.app.activityViewModels
import com.illa.joliveapp.R
import com.illa.joliveapp.activity.EditMyInfoActivity
import com.illa.joliveapp.custom_view.ItemInterestView
import com.illa.joliveapp.tools.Tools
import com.illa.joliveapp.viewmodle.ProfileActivityVM
import kotlinx.android.synthetic.main.fragment_creat_profile_step_1.fl_tag_main
import kotlinx.android.synthetic.main.fragment_creat_profile_step_1.tv_next_step


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "CAST_NEVER_SUCCEEDS")
class FragmentEditMyInfo_Interest : BaseFragment() {

    private val profileActivityVM: ProfileActivityVM by activityViewModels()
    private var nextStepCheck = 0
    private var selectedPosition = 0
    private lateinit var act: EditMyInfoActivity


    override fun getLayoutId(): Int {
        return R.layout.fragment_edit_myinfo_interest
    }

    @SuppressLint("NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        Log.e("Peter","isNavigationViewInit ")

        if(!isNavigationViewInit){
            Log.e("Peter","isNavigationViewInit  ININ ")
            super.onViewCreated(view, savedInstanceState)
            init()
            initButtons()
            initObserve()
            callApis()
            isNavigationViewInit = true
        }
    }

    override fun onResume() {
        super.onResume()
        setTitle("請選擇您近期喜歡或參與的興趣")

//        act.setSubTitle("請選擇您近期喜歡或參與的興趣")
//        (getMContext().get() as ProfileActivity).setStepOne()
//        act.dataBody.keys.forEach {
//            if(it.contains("interests[")){
//                act.dataBody.remove(it)
//            }
//        }
    }


    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    private fun init(){
        tv_next_step.isClickable = false
        act = getMContext().get() as EditMyInfoActivity
        act.hideActionItem()

        tv_next_step.setOnClickListener(onClick)
        tv_next_step.isClickable = false

    }


    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n", "ShowToast")
    private val onClick = View.OnClickListener {
        when (it.id) {

            R.id.tv_next_step -> {
                act.interestArrayList.clear()
                for(i in 0 until fl_tag_main.childCount){
                    val itemView = fl_tag_main[i] as ItemInterestView
                    if(itemView.getIsSelected()!!){
//                        act.dataBody["interests[$selectedPosition]"] = itemView.getItemId().toString()
                        Log.e("Peter","select interest   ${itemView.getItemId()}")
                        itemView.getItemId()?.let { it1 -> act.interestArrayList.add(it1) }

                        selectedPosition++
                    }
                }
                act.onBackPressed()
//                findNavController().navigate(R.id.action_FragmentCreateProfile_step1_to_FragmentCreateProfile_step2)
            }
        }
    }
    private fun initButtons(){
        Log.e("peter","FragmentEditMyInfo_Interest  initButtons   ${act.interestArrayList}")

        act.interestArrayList.forEach {
            for(i in 0 until fl_tag_main.childCount){
                val itemView = fl_tag_main[i] as ItemInterestView
                Log.e("peter","FragmentEditMyInfo_Interest  id   $it    itemView.getItemId()    ${itemView.getItemId()} ")

                if(it == itemView.getItemId()){
                    itemView.setIsSelected(true)
                }
            }
        }
        tv_next_step.isClickable = true
        tv_next_step.background = getMContext().get()!!.resources.getDrawable(R.drawable.bg_clickable_btn)
        tv_next_step.setTextColor(getMContext().get()!!.resources.getColor(R.color.colorWhite))
        act.hideActionItem()
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

                            if(nextStepCheck == 0){
                                nextStepCheck++
                            } else if(nextStepCheck == 1 && textView.getIsSelected() == true){
                                nextStepCheck = 0

                            }

                            if(nextStepCheck >= 8 ){
                                textView.setClickAble(false)
                                Tools.toast(getMContext().get(),"最多選擇8個興趣")

                            } else {
                                textView.setClickAble(true)
                            }

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
                initButtons()

            })
    }

    private fun callApis(){
        profileActivityVM.getInterestList()
    }

}
