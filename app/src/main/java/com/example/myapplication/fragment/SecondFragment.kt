package com.example.myapplication.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.viewmodle.MainActivityVM
import kotlinx.android.synthetic.main.fragment_second.*

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : BaseFragment() {
    val mainActVM: MainActivityVM by activityViewModels()

    override fun getLayoutId(): Int {
        return R.layout.fragment_second
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initObserve()
        mainActVM.getMyInfo()
    }

    private fun init(){
        button_second.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
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
        })
    }
}