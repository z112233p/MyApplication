package com.example.myapplication.fragment

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.tools.WebSocketHelper
import kotlinx.android.synthetic.main.fragment_chatroom.*

class FragmentChatRoom : BaseFragment() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_chatroom
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        WebSocketHelper.connect()
        WebSocketHelper.sendTest("YOYOYO")
        init()
    }

    private fun init(){
        btn_start.setOnClickListener {
            findNavController().navigate(R.id.action_FragmentChatRoom_to_FirstFragment)
        }
    }

}