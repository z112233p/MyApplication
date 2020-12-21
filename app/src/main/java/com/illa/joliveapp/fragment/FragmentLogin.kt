package com.illa.joliveapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.activityViewModels
import com.illa.joliveapp.R
import com.illa.joliveapp.tools.PrefHelper
import com.illa.joliveapp.viewmodle.MainActivityVM
import kotlin.properties.Delegates

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FragmentLogin : BaseFragment() {

    val mainActVM: MainActivityVM by activityViewModels()
    private var clickAble by Delegates.notNull<Boolean>()
    private var nowPicPos = 0
    private val imgRes = intArrayOf(R.mipmap.ph_tokyo, R.mipmap.ph_taipei)


    override fun getLayoutId(): Int {
        return R.layout.fragment_login_main
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clickAble = true
//        init()
//        initObserve()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun checkSession(): Boolean  {
        return PrefHelper.apiHeader != ""
    }

//
//    private fun init(){
//        btn_send.setOnClickListener(onClick)
//        btn_resend.setOnClickListener(onClick)
//        btn_register.setOnClickListener(onClick)
//    }
//
//    @SuppressLint("WrongConstant", "ShowToast")
//    private var onClick :View.OnClickListener ?= View.OnClickListener {
//        Tools.hideSoftKeyboard(getMContext().get() as Activity)
//        when (it.id){
//            R.id.btn_send -> {
//                if(clickAble){
//                    mainActVM.login(loginData = LoginData(et_phone.text.toString().toInt(), et_verification.text.toString()))
//                    clickAble = false
//                }
//            }
//            R.id.btn_resend -> mainActVM.resendSMS(resendSMS = ResendSMS(et_phone.text.toString().toInt()))
//            R.id.btn_register -> findNavController().navigate(R.id.action_FirstFragment_to_FragmentRegister)
//        }
//        btn_send.isClickable = false
//
//    }
//
//    @SuppressLint("ShowToast")
//    private fun initObserve(){
////        if(checkSession()){
//////            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
////            return
////        }
//        mainActVM.getLoginResponse().observe(viewLifecycleOwner, Observer {
//            clickAble = true
//            btn_send.isClickable = true
//
//            if(it.code != 0){
//                Tools.toast(getMContext().get(), "登錄失敗，請註冊帳號或重新發送驗證碼")
//                return@Observer
//            }
//            Tools.toast(getMContext().get(), "登錄成功")
//
//            PrefHelper.setApiHeader(it.data.user_token)
//            PrefHelper.setUserID(it.data.user_id.toString())
//            PrefHelper.setChatToken(it.data.chat_auth_token)
//            PrefHelper.setChatId(it.data.chat_user_id)
//            PrefHelper.setChatLable(it.data.label)
//            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
//        })
//
//        mainActVM.getResendSMSCheck().observe(viewLifecycleOwner, Observer {
//            btn_send.isClickable = true
//
//            if(it){
//                Tools.toast(getMContext().get(), "驗證碼發送成功")
//            } else {
//                Tools.toast(getMContext().get(), "驗證碼發送失敗")
//            }
//        })
//    }

}