package com.example.myapplication.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.tools.PrefHelper
import com.example.myapplication.datamodle.authorization.LoginData
import com.example.myapplication.datamodle.authorization.ResendSMS
import com.example.myapplication.datamodle.authorization.register.Register
import com.example.myapplication.tools.Config
import com.example.myapplication.tools.Tools
import com.example.myapplication.viewmodle.MainActivityVM
import kotlinx.android.synthetic.main.fragment_input_phone.*
import kotlinx.android.synthetic.main.fragment_register.*
import kotlin.properties.Delegates

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FragmentInputPhone : BaseFragment() {

    val mainActVM: MainActivityVM by activityViewModels()
    private var clickAble by Delegates.notNull<Boolean>()

    override fun getLayoutId(): Int {
        return R.layout.fragment_input_phone
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showToolBar()
        setToolbarTitle("請輸入手機號碼")
        clickAble = true
        initObserve()
        init()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun checkSession(): Boolean  {
        return PrefHelper.apiHeader != ""
    }

    private fun init(){
        ll_send_code_btn.setOnClickListener(onClick)
    }

    @SuppressLint("WrongConstant", "ShowToast")
    private var onClick :View.OnClickListener ?= View.OnClickListener {
        Tools.hideSoftKeyboard(getMContext().get() as Activity)
        when (it.id){
            R.id.ll_send_code_btn -> {
                Log.e("Peter","ed_phone_number   ${ed_phone_number.text}")

                mainActVM.register(register = Register(ed_phone_number.text.toString().toInt(), "1"))

            }
        }
    }

    private fun initObserve(){
        mainActVM.getRegisterResponse().observe(viewLifecycleOwner, Observer {
            val bundle = bundleOf(Config.INPUT_NUMBER to ed_phone_number.text)
            findNavController().navigate(R.id.action_FragmentInputPhone_to_FragmentInputVerify, bundle)
        })
    }
}