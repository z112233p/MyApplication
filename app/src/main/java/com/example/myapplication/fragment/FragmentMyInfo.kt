package com.example.myapplication.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.adapter.Adapter_Chat_Room
import com.example.myapplication.adapter.Adapter_Profile_Photo
import com.example.myapplication.tools.PrefHelper
import com.example.myapplication.viewmodle.MainActivityVM
import kotlinx.android.synthetic.main.fragment_myinfo.*
import kotlinx.android.synthetic.main.fragment_myinfo.tv_age
import kotlinx.android.synthetic.main.fragment_myinfo.tv_id
import kotlinx.android.synthetic.main.fragment_myinfo.tv_language_id

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class FragmentMyInfo : BaseFragment() {
    val mainActVM: MainActivityVM by activityViewModels()
    private lateinit var adapter : Adapter_Profile_Photo


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
        adapter = getMContext().get()?.let { Adapter_Profile_Photo(it, false) }!!
        rv_profile_photo.layoutManager = LinearLayoutManager(getMContext().get(), RecyclerView.HORIZONTAL, false)
        rv_profile_photo.adapter = adapter

        button_second.setOnClickListener(onClick)
        btn_edit_my_info.setOnClickListener(onClick)
    }

    private var onClick: View.OnClickListener ?= View.OnClickListener {
        when(it.id){
            R.id.button_second -> findNavController().navigate(R.id.action_SecondFragment_to_FragmentChatRoom)
            R.id.btn_edit_my_info -> findNavController().navigate(R.id.action_SecondFragment_to_FragmentEditMyInfo)
        }
    }

    private fun initObserve(){
        mainActVM.getMyInfoData().observe(viewLifecycleOwner, Observer {
            tv_age.text = it.user.age
            tv_birthday.text = it.user.birthday
            tv_id.text = it.user.id.toString()
            tv_language_id.text = it.user.language_id.toString()
            tv_name.text = it.user.name

            adapter.setData(it.user.photos)
            PrefHelper.setChatName(it.user.name)
        })
    }
}