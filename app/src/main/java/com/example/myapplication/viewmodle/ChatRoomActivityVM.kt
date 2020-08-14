package com.example.myapplication.viewmodle

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.datamodle.chat.history.ChatHistory
import com.example.myapplication.datamodle.chat.history.Message
import io.reactivex.Observer
import com.example.myapplication.datamodle.dating.DatingSearch
import com.example.myapplication.datamodle.dating.DatingSearchData
import com.example.myapplication.network.ApiMethods
import com.example.myapplication.tools.LogUtil
import com.example.myapplication.tools.SingleLiveEvent
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.json.JSONObject

class ChatRoomActivityVM (application: Application) : AndroidViewModel(application){
    private val mContent: Context? = application.applicationContext
    private val disContainer : CompositeDisposable = CompositeDisposable()

    //set
    private val chatHistory: MutableLiveData<List<Message>> = MutableLiveData<List<Message>>()

    //get
    fun getChatHistoryData(): LiveData<List<Message>> {
        return chatHistory
    }


    fun getChatHistory(rid: String){
        val chatHistoryObserve: Observer<ChatHistory> = object : Observer<ChatHistory>{
            override fun onComplete() {
            }

            override fun onSubscribe(d: Disposable) {
            }

            override fun onError(e: Throwable) {
                Log.e("Peter", "getChatHistory onError:  $e")
            }

            override fun onNext(t: ChatHistory) {
                chatHistory.value = t.messages
                for ((f, i) in t.messages.withIndex()){
                    LogUtil.e("PetergetChatHistory", "getChatHistory onNext:  ${i.u.name}"+"     "+i.t+"   "+i.msg)
                }
            }

        }
        ApiMethods.getChatHistory(chatHistoryObserve, rid)
    }


}