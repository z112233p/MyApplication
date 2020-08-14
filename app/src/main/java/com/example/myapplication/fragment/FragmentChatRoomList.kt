package com.example.myapplication.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.activity.MatchActivity
import com.example.myapplication.adapter.Adapter_Chat_Room
import com.example.myapplication.datamodle.chat.ChatRoomListUpdate
import com.example.myapplication.network.WebSocketHelper
import com.example.myapplication.tools.PrefHelper
import com.example.myapplication.viewmodle.MainActivityVM
import io.reactivex.Observable
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_chatroom.*
import org.json.JSONObject


class FragmentChatRoomList : BaseFragment() {

    val mainActVM: MainActivityVM by activityViewModels()
    private lateinit var adapter : Adapter_Chat_Room


    override fun getLayoutId(): Int {
        return R.layout.fragment_chatroom
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        WebSocketHelper.connect()
        init()
        initObserve()

        mainActVM.getChatRoomList()
//        mainActVM.getChatHistory("7vrg5RGpG9dhK7Q92")
    }

    private fun init(){
        adapter = getMContext().get()?.let { Adapter_Chat_Room(it) }!!
        rv_chat_list.layoutManager = LinearLayoutManager(getMContext().get(), RecyclerView.VERTICAL, false)
        rv_chat_list.adapter = adapter


        btn_start.setOnClickListener {
//            findNavController().navigate(R.id.action_FragmentChatRoom_to_FirstFragment)
            val intent = Intent(getMContext().get(), MatchActivity::class.java)
            getMContext().get()?.startActivities(arrayOf(intent))
        }
    }

    private fun initObserve(){
        mainActVM.getChatRoomListData().observe(viewLifecycleOwner, Observer {

            Observable
                .fromIterable(it.update)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation())
                .filter {
                    val jsonString = it.topic ?: "null"
                    "null" != jsonString
                    if ("null" == jsonString){
                        return@filter false
                    }
                    val jsonObject = JSONObject(jsonString)
                    return@filter if (jsonObject.has(PrefHelper.getChatLable())){
                        true
                    } else {
                        jsonObject.has("name")
                    }
                }
                .toList()
                .subscribe(object : SingleObserver<List<ChatRoomListUpdate>> {
                    override fun onSubscribe(d: Disposable) {
                        /* to do */
                    }

                    override fun onError(e: Throwable) {
                        Log.e("Peter", "FragmentChatRoom getChatRoom FINISH2 ERR $e")
                        adapter.setData(it.update)

                    }

                    override fun onSuccess(t: List<ChatRoomListUpdate>) {
                        Log.e("Peter", "FragmentChatRoom getChatRoom FINISH2  ${t.size}")
                        adapter.setData(t)

                    }
                })
        })


    }

}