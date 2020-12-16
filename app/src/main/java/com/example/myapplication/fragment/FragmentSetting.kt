package com.example.myapplication.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import com.example.myapplication.R
import com.example.myapplication.tools.IntentHelper
import com.example.myapplication.tools.PrefHelper
import com.example.myapplication.tools.Tools
import com.example.myapplication.viewmodle.MainActivityVM


import kotlinx.android.synthetic.main.fragment_setting.*
import kotlinx.android.synthetic.main.item_setting.view.*


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "CAST_NEVER_SUCCEEDS")
class FragmentSetting : BaseFragment() {

    val mainActVM: MainActivityVM by activityViewModels()


    override fun getLayoutId(): Int {
        return R.layout.fragment_setting

    }

    @SuppressLint("NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        Log.e("Peter","isNavigationViewInit ")

        if(!isNavigationViewInit){
            Log.e("Peter","isNavigationViewInit  ININ ")
            super.onViewCreated(view, savedInstanceState)
            init()
            initObserve()
            setHasOptionsMenu(true)
            isNavigationViewInit = true
        }
    }

    override fun onResume() {
        super.onResume()
        callApis()
        setTitle("Settings")

    }


    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    private fun init(){
        initMenuButtons()
        tv_user_phone.text = PrefHelper.userPhone
        ll_logout.setOnClickListener(onClick)

    }

    private fun initMenuButtons(){
        cl_push.iv_title.setImageDrawable(getMContext().get()?.resources?.getDrawable(R.mipmap.ic_setting_push))
        cl_push.tv_title.text = getString(R.string.notice_push)
        cl_push.setting_switch.visibility = View.VISIBLE

        cl_ban.iv_title.setImageDrawable(getMContext().get()?.resources?.getDrawable(R.mipmap.ic_ban))
        cl_ban.tv_title.text = getString(R.string.ban_list)
        cl_ban.tv_value.text = "10人"

        cl_hide_age.iv_title.setImageDrawable(getMContext().get()?.resources?.getDrawable(R.mipmap.ic_hide_age))
        cl_hide_age.tv_title.text = getString(R.string.hide_age)
        cl_hide_age.setting_switch.visibility = View.VISIBLE

        cl_change_language.iv_title.setImageDrawable(getMContext().get()?.resources?.getDrawable(R.mipmap.ic_change_language))
        cl_change_language.tv_title.text = getString(R.string.change_language)
        cl_change_language.v_divider_line.visibility = View.INVISIBLE
        cl_change_language.tv_value.text = "繁體中文"

        cl_service_provision.iv_title.setImageDrawable(getMContext().get()?.resources?.getDrawable(R.mipmap.ic_service_provision))
        cl_service_provision.tv_title.text = getString(R.string.service_provision)

        cl_privacy_policy.iv_title.setImageDrawable(getMContext().get()?.resources?.getDrawable(R.mipmap.ic_privacy_policy))
        cl_privacy_policy.tv_title.text = getString(R.string.privacy_policy)

        cl_official_website.iv_title.setImageDrawable(getMContext().get()?.resources?.getDrawable(R.mipmap.ic_official_website))
        cl_official_website.tv_title.text = getString(R.string.official_website)

        cl_facebook_website.iv_title.setImageDrawable(getMContext().get()?.resources?.getDrawable(R.mipmap.ic_facebook_website))
        cl_facebook_website.tv_title.text = getString(R.string.facebook_website)
        cl_facebook_website.v_divider_line.visibility = View.INVISIBLE
    }


    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n", "ShowToast")
    private val onClick = View.OnClickListener {
        when (it.id) {
            R.id.ll_logout ->{
                val builder: AlertDialog.Builder = AlertDialog.Builder(getMContext().get())
                builder.setMessage("確定要登出？")
                builder.setPositiveButton("確定") {
                        p0, p1 ->  Tools.logout()
                    (getMContext().get() as Activity).finish()
                    getMContext().get()?.let { it1 -> IntentHelper.gotoPersonalActivity(it1) }

                }
                builder.setNegativeButton("取消") {
                        p0, p1 -> Log.e("Peter","dialog cancel")
                }
                val dialog = builder.create()
                dialog.show()
            }
        }
    }


    private fun callApis(){

    }

    private fun initObserve(){

    }
//
//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        super.onCreateOptionsMenu(menu, inflater)
//    }
//
//    @RequiresApi(Build.VERSION_CODES.N)
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        Log.e("Peter","FragmentEditMyInfoV2 onOptionsItemSelected")
//        return when(item.itemId){
//            R.id.action_store ->{
//
//
//                true
//            }
//            else -> true
//
//        }
//        return super.onOptionsItemSelected(item)
//
//    }

}
