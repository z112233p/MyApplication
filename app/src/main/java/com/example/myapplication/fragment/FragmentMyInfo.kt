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
import com.example.myapplication.viewmodle.MainActivityVM
import kotlinx.android.synthetic.main.fragment_myinfo.*

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class FragmentMyInfo : BaseFragment() {
    val mainActVM: MainActivityVM by activityViewModels()

    override fun getLayoutId(): Int {
        return R.layout.fragment_myinfo
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initObserve()
        mainActVM.getMyInfo()
    }

    private fun init(){
        button_second.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FragmentChatRoom)
            Log.e("Peter","button_second")
        }
    }

    private fun initObserve(){
        mainActVM.getMyInfoData().observe(viewLifecycleOwner, Observer {
            tv_age.text = it.user.age
            tv_birthday.text = it.user.birthday
            tv_id.text = it.user.id.toString()
            tv_language_id.text = it.user.language_id.toString()
            tv_name.text = it.user.name

            PrefHelper.setChatName(it.user.name)
        })
    }
}