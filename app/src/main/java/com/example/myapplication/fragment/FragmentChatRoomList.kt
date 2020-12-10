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
import com.example.myapplication.adapter.Adapter_Chat_Room_All
import com.example.myapplication.datamodle.chat.chatroom_list.Update
import com.example.myapplication.network.WebSocketHelper
import com.example.myapplication.tools.PrefHelper
import com.example.myapplication.viewmodle.EventsActivityVM
import com.example.myapplication.viewmodle.MainActivityVM
import io.reactivex.Observable
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_chatroom.*
import org.json.JSONObject


class FragmentChatRoomList : BaseFragment() {

    val eventsActivityVM: EventsActivityVM by activityViewModels()
    private lateinit var adapter : Adapter_Chat_Room
    private lateinit var adapterAll : Adapter_Chat_Room_All


    override fun getLayoutId(): Int {
        return R.layout.fragment_chatroom
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        expandToolbar(false)
        showToolBar()
        showTitle()
        setTitle(getMContext().get()!!.resources.getString(R.string.chat_room))
        WebSocketHelper.connect()
        init()
        initObserve()

    }

    override fun onStart() {
        super.onStart()
        eventsActivityVM.getChatRoomToken()

    }
    private fun init(){
        adapter = getMContext().get()?.let { Adapter_Chat_Room(it) }!!
        adapterAll = getMContext().get()?.let { Adapter_Chat_Room_All(it) }!!

        rv_chat_list.layoutManager = LinearLayoutManager(getMContext().get(), RecyclerView.VERTICAL, false)
        rv_chat_list.adapter = adapter

        rv_chat_list_all.layoutManager = LinearLayoutManager(getMContext().get(), RecyclerView.HORIZONTAL, false)
        rv_chat_list_all.adapter = adapterAll


        btn_start.setOnClickListener {
//            findNavController().navigate(R.id.action_FragmentChatRoom_to_FirstFragment)
            val intent = Intent(getMContext().get(), MatchActivity::class.java)
            getMContext().get()?.startActivities(arrayOf(intent))
        }
    }

    private fun initObserve(){
        eventsActivityVM.getChatRoomTokenData().observe(viewLifecycleOwner, Observer {
            if(it != null){
                PrefHelper.setChatToken(it.data.chat_auth_token)
                PrefHelper.setChatId(it.data.chat_user_id)
                eventsActivityVM.getChatRoomList()

            }
        })

        eventsActivityVM.getChatRoomListData().observe(viewLifecycleOwner, Observer {

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
                    return@filter if (jsonObject.has(PrefHelper.chatLable)){
                        true
                    } else {
                        jsonObject.has("name")
                    }
                }
                .toList()
                .subscribe(object : SingleObserver<List<Update>> {
                    override fun onSubscribe(d: Disposable) {
                        /* to do */
                    }

                    override fun onError(e: Throwable) {
                        Log.e("Peter", "FragmentChatRoom getChatRoom FINISH2 ERR $e")
                        adapter.setData(it.update)
                        adapterAll.setData(it.update)

                    }

                    override fun onSuccess(t: List<Update>) {
                        Log.e("Peter", "FragmentChatRoom getChatRoom FINISH2  ${t}")
                        val dataList = t.sortedByDescending { data ->
                            data.lastMessage?._updatedAt
                        }
                        adapter.setData(dataList)
                        adapterAll.setData(t)

                    }
                })
        })
    }

    override fun onDetach() {
//        expandToolbar(true)
        super.onDetach()
    }
}