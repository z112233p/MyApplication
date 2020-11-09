package com.example.myapplication.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.tools.PrefHelper
import com.example.myapplication.datamodle.authorization.register.Register
import com.example.myapplication.viewmodle.MainActivityVM
import kotlinx.android.synthetic.main.fragment_register.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FragmentRegister : BaseFragment() {

    val mainActVM: MainActivityVM by activityViewModels()

    override fun getLayoutId(): Int {
        return R.layout.fragment_register
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        initObserve()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun checkSession(): Boolean  {
        return PrefHelper.apiHeader != ""
    }

    private fun init(){
        button_first.setOnClickListener {
            mainActVM.register(register = Register(et_phone.text.toString().toInt(), et_language_id.text.toString()))
            button_first.isClickable = false

        }

//        btn_resend.setOnClickListener {
//            mainActVM.resendSMS(resendSMS = ResendSMS(et_phone.text.toString().toInt()))
//            btn_resend.isClickable = false
//        }
//
        btn_cancel.setOnClickListener {
            findNavController().navigate(R.id.action_FragmentRegister_to_FirstFragment)
        }
    }

    private fun initObserve(){
//        if(checkSession()){
////            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
//            return
//        }
        mainActVM.getLoginResponse().observe(viewLifecycleOwner, Observer {
            PrefHelper.setApiHeader(it.data.user_token)
            PrefHelper.setChatToken(it.data.chat_auth_token)
            PrefHelper.setChatId(it.data.chat_user_id)
            PrefHelper.setChatLable(it.data.label)
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        })

        mainActVM.getRegisterResponse().observe(viewLifecycleOwner, Observer {
            findNavController().navigate(R.id.action_FragmentRegister_to_FirstFragment)
//            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        })
    }

    fun test(){
        Log.e("Peter","Fragment test")
    }
}