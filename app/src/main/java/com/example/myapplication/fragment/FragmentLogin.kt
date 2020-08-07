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
import com.example.myapplication.datamodle.authorization.LoginData
import com.example.myapplication.viewmodle.MainActivityVM
import kotlinx.android.synthetic.main.fragment_login.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FragmentLogin : BaseFragment() {

    val mainActVM: MainActivityVM by activityViewModels()

    override fun getLayoutId(): Int {
        return R.layout.fragment_login
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        initObserve()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun init(){
        button_first.setOnClickListener {
            mainActVM.login(loginData = LoginData(et_phone.text.toString().toInt(), et_verification.text.toString()))
            button_first.isClickable = false
        }
    }

    private fun initObserve(){
        mainActVM.getLoginResponse().observe(viewLifecycleOwner, Observer {
            Log.e("Peter","Fragment Observe"+it.data.user_token)
            PrefHelper.setApiHeader(it.data.user_token)
            PrefHelper.setChatToken(it.data.chat_auth_token)
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        })
    }

    fun test(){
        Log.e("Peter","Fragment test")
    }
}