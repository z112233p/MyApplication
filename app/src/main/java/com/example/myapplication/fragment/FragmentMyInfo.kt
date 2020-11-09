package com.example.myapplication.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.annotation.VisibleForTesting
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.adapter.Adapter_Chat_Room
import com.example.myapplication.adapter.Adapter_Profile_Photo
import com.example.myapplication.datamodle.authorization.ResendResponse
import com.example.myapplication.datamodle.profile.MyInfoData
import com.example.myapplication.tools.IntentHelper
import com.example.myapplication.tools.PrefHelper
import com.example.myapplication.tools.Tools
import com.example.myapplication.viewmodle.MainActivityVM
import io.reactivex.Flowable.fromIterable
import io.reactivex.Observable.fromIterable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_myinfo.*
import kotlinx.android.synthetic.main.fragment_myinfo.tv_age
import kotlinx.android.synthetic.main.fragment_myinfo.tv_id
import kotlinx.android.synthetic.main.fragment_myinfo.tv_language_id
import java.util.*

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class FragmentMyInfo : BaseFragment() {
    val mainActVM: MainActivityVM by activityViewModels()
    private lateinit var adapter : Adapter_Profile_Photo


    override fun getLayoutId(): Int {
        return R.layout.fragment_myinfo
    }

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e("Peter","FragmentMyInfoonViewCreated")
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
        btn_events.setOnClickListener(onClick)
        btn_logout.setOnClickListener(onClick)
    }

    private var onClick: View.OnClickListener ?= View.OnClickListener {
        when(it.id){
            R.id.button_second -> findNavController().navigate(R.id.action_SecondFragment_to_FragmentChatRoom)
            R.id.btn_edit_my_info -> findNavController().navigate(R.id.action_SecondFragment_to_FragmentEditMyInfo)
            R.id.btn_events -> getMContext().get()?.let { it1 -> IntentHelper.gotoEventActivity(it1) }
            R.id.btn_logout -> {
                Tools.logout()
                tv_age.text = ""
                tv_birthday.text = ""
                tv_id.text = ""
                tv_language_id.text = ""
                tv_name.text = ""
                adapter.setData(null)
                this.onDetach()
                mainActVM.clearMyInfo()
                findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
            }
        }
    }

    private fun initObserve(){
        mainActVM.getMyInfoData().observe(viewLifecycleOwner, myInfoDataObserver)
    }

    private var myInfoDataObserver = object: Observer<MyInfoData?> {
        override fun onChanged(it: MyInfoData?) {
            if(it == null){
                return
            }
            tv_age.text = it.user.age
            tv_birthday.text = it.user.birthday
            tv_id.text = it.user.id.toString()
            tv_language_id.text = it.user.language_id.toString()
            tv_name.text = it.user.name

            adapter.setData(it.user.photos)
            PrefHelper.setChatName(it.user.name)
        }

    }

    override fun onDetach() {
        super.onDetach()

    }

}