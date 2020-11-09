package com.example.myapplication.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.datamodle.authorization.LoginData
import com.example.myapplication.datamodle.authorization.ResendSMS
import com.example.myapplication.tools.Config
import com.example.myapplication.tools.PrefHelper
import com.example.myapplication.tools.Tools
import com.example.myapplication.viewmodle.MainActivityVM
import kotlinx.android.synthetic.main.fragment_input_verify.*
import kotlin.properties.Delegates


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FragmentInputVerify : BaseFragment() {

    val mainActVM: MainActivityVM by activityViewModels()
    private var clickAble by Delegates.notNull<Boolean>()
    private lateinit var phone: String

    override fun getLayoutId(): Int {
        return R.layout.fragment_input_verify
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getPhoneNumber()
        showToolBar()
        setToolbarTitle("請輸入驗證碼")
        clickAble = true
        init()
        initObserve()

    }

    private fun getPhoneNumber(){
        phone = arguments?.get(Config.INPUT_NUMBER).toString()
        Log.e("Peter","getPhoneNumber   $phone")

    }

    private fun checkSession(): Boolean  {
        return PrefHelper.apiHeader != ""
    }

    private fun init(){
        cl_verify_images.setOnClickListener(onClick)
        tv_shelter.setOnClickListener(onClick)
        tv_resend_verify.setOnClickListener(onClick)
        ll_send_code_btn.setOnClickListener(onClick)


        ed_input_verify.addTextChangedListener {
            Log.e("Peter","ed_input_verify.addTextChangedListener   ${it?.length}")

            tv_verify_1.text = ""
            iv_verify_1.visibility = View.VISIBLE
            tv_verify_2.text = ""
            iv_verify_2.visibility = View.VISIBLE
            tv_verify_3.text = ""
            iv_verify_3.visibility = View.VISIBLE
            tv_verify_4.text = ""
            iv_verify_4.visibility = View.VISIBLE
            tv_verify_5.text = ""
            iv_verify_5.visibility = View.VISIBLE
            tv_verify_6.text = ""
            iv_verify_6.visibility = View.VISIBLE


            if (it != null) {
                for(i in 1.. it.length){
                    when(i){
                        1 -> {
                            tv_verify_1.text = it[i-1].toString()
                            iv_verify_1.visibility = View.INVISIBLE
                        }
                        2 -> {
                            tv_verify_2.text = it[i-1].toString()
                            iv_verify_2.visibility = View.INVISIBLE
                        }
                        3 -> {
                            tv_verify_3.text = it[i-1].toString()
                            iv_verify_3.visibility = View.INVISIBLE
                        }
                        4 -> {
                            tv_verify_4.text = it[i-1].toString()
                            iv_verify_4.visibility = View.INVISIBLE
                        }
                        5 -> {
                            tv_verify_5.text = it[i-1].toString()
                            iv_verify_5.visibility = View.INVISIBLE
                        }
                        6 -> {
                            tv_verify_6.text = it[i-1].toString()
                            iv_verify_6.visibility = View.INVISIBLE
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("WrongConstant", "ShowToast")
    private var onClick :View.OnClickListener ?= View.OnClickListener {
        Tools.hideSoftKeyboard(getMContext().get() as Activity)
        when (it.id){
            R.id.cl_verify_images -> {
                ed_input_verify.requestFocus()
                ed_input_verify.isFocusableInTouchMode = true
                ed_input_verify.isClickable = false
                val imm: InputMethodManager? = (getMContext().get() as Activity).getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager?
                imm?.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
            }
            R.id.tv_shelter -> {

            }
            R.id.tv_resend_verify -> mainActVM.resendSMS(resendSMS = ResendSMS(phone.toInt()))
            R.id.ll_send_code_btn -> mainActVM.login(loginData = LoginData(phone.toInt(), ed_input_verify.text.toString()))

        }
    }
    @SuppressLint("ShowToast")
    private fun initObserve(){
//        if(checkSession()){
////            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
//            return
//        }
        mainActVM.getLoginResponse().observe(viewLifecycleOwner, Observer {
            clickAble = true

            if(it.code != 0){
                Tools.toast(getMContext().get(), "登錄失敗，請註冊帳號或重新發送驗證碼")
                return@Observer
            }
            Tools.toast(getMContext().get(), "登錄成功")

            PrefHelper.setApiHeader(it.data.user_token)
            PrefHelper.setUserID(it.data.user_id.toString())
            PrefHelper.setChatToken(it.data.chat_auth_token)
            PrefHelper.setChatId(it.data.chat_user_id)
            PrefHelper.setChatLable(it.data.label)
            findNavController().navigate(R.id.action_FragmentInputVerify_to_SecondFragment)
        })

        mainActVM.getResendSMSCheck().observe(viewLifecycleOwner, Observer {

            if(it){
                Tools.toast(getMContext().get(), "驗證碼發送成功")
            } else {
                Tools.toast(getMContext().get(), "驗證碼發送失敗")
            }
        })
    }

}