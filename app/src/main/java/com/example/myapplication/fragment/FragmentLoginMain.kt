package com.example.myapplication.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.tools.PrefHelper
import com.example.myapplication.datamodle.authorization.LoginData
import com.example.myapplication.datamodle.authorization.ResendSMS
import com.example.myapplication.tools.Tools
import com.example.myapplication.viewmodle.MainActivityVM
import kotlinx.android.synthetic.main.fragment_login_main.*
import kotlinx.android.synthetic.main.item_login_button.view.*
import kotlin.properties.Delegates

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FragmentLoginMain : BaseFragment() {

    val mainActVM: MainActivityVM by activityViewModels()

    override fun getLayoutId(): Int {
        return R.layout.fragment_login_main
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        setTitle("請輸入手機號碼")
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun checkSession(): Boolean  {
        return PrefHelper.apiHeader != ""
    }

    private fun init(){
        hideToolBar()
        hideTitle()
        tv_login.setOnClickListener(onClick)
        btn_login_phone.run {
            setOnClickListener(onClick)
            tv_btn_text.text = "手機號碼註冊"
            iv_btn_img.setImageDrawable(getMContext().get()?.resources?.getDrawable(R.mipmap.ic_phone))
        }
        btn_login_facebook.run {
            setOnClickListener(onClick)
            tv_btn_text.text = "Facebook登入"
            tv_btn_text.setTextColor(getMContext().get()?.resources?.getColor(R.color.colorWhite)!!)
            iv_btn_img.setImageDrawable(getMContext().get()?.resources?.getDrawable(R.mipmap.ic_facebook_))
            fl_button_layout.background = getMContext().get()?.resources?.getDrawable(R.drawable.bg_login_button_blue)
        }
        btn_login_apple.run {
            setOnClickListener(onClick)
            tv_btn_text.text = "Apple登入"
            iv_btn_img.setImageDrawable(getMContext().get()?.resources?.getDrawable(R.mipmap.ic_apple))
        }

    }

    @SuppressLint("WrongConstant", "ShowToast")
    private var onClick :View.OnClickListener ?= View.OnClickListener {
        Tools.hideSoftKeyboard(getMContext().get() as Activity)
        when (it.id){
            R.id.btn_login_phone,R.id.tv_login -> findNavController().navigate(R.id.action_FragmentLoginMain_to_FragmentInputPhone)

        }
    }

}