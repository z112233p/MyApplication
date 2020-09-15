package com.example.myapplication.viewmodle

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.BuildConfig
import com.example.myapplication.datamodle.chat.history.ChatHistory
import com.example.myapplication.datamodle.chat.history.Message
import com.example.myapplication.datamodle.chat.image_message.response.FileResponse
import com.example.myapplication.datamodle.chat.message_update.MessageUpdate
import com.example.myapplication.datamodle.chat.text_message.TextMessage
import com.example.myapplication.datamodle.chat.text_message.response.TextResponse
import io.reactivex.Observer
import com.example.myapplication.network.ApiMethods
import com.example.myapplication.tools.LogUtil
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.io.File
import java.util.*

class ChatRoomActivityVM (application: Application) : AndroidViewModel(application){
    private val mContent: Context? = application.applicationContext
    private val disContainer : CompositeDisposable = CompositeDisposable()

    //set
    private val chatHistory: MutableLiveData<List<Message>> = MutableLiveData<List<Message>>()
    private val messageUpdate: MutableLiveData<com.example.myapplication.datamodle.chat_room.Message> = MutableLiveData()

    //get
    fun getChatHistoryData(): LiveData<List<Message>> {
        return chatHistory
    }

    fun getMessageUpdate(): LiveData<com.example.myapplication.datamodle.chat_room.Message> {
        return messageUpdate
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

    fun getChatHistory(rid: String, latest: Date){
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
        ApiMethods.getChatHistory(chatHistoryObserve, rid, latest)
    }

    fun postImageMessage(
        file: File,
        rId: String,
        imgData: com.example.myapplication.datamodle.chat_room.Message
    ){
        val imageMessageObserve: Observer<FileResponse> = object :Observer<FileResponse>{
            override fun onComplete() {
                Log.e("Peter", "postImageMessage onComplete")
            }

            override fun onSubscribe(d: Disposable) {
            }

            override fun onNext(t: FileResponse) {
//                imgData.setImageUrl(BuildConfig.CHATROOM_URL+t.message.attachments[0].image_url)
                imgData.setSuccess(t.success.toString())
                imgData.setImageUrl(BuildConfig.CHATROOM_URL+t.message.attachments[0].image_url)
                messageUpdate.value = imgData
                Log.e("Peter", "postImageMessage onNext:  "+t.success.toString())
                Log.e("Peter", "postImageMessage onNext:  "+t.message._id)

            }

            override fun onError(e: Throwable) {
                imgData.setSuccess("false")
                messageUpdate.value = imgData
                Log.e("Peter", "postImageMessage onError:  $e")
            }

        }
        ApiMethods.postImageMessage(imageMessageObserve, file, rId)
    }

    fun postAudioMessage(
        file: File,
        rId: String,
        audioData: com.example.myapplication.datamodle.chat_room.Message
    ){
        val imageMessageObserve: Observer<FileResponse> = object :Observer<FileResponse>{
            override fun onComplete() {
                Log.e("Peter", "postImageMessage onComplete")
            }

            override fun onSubscribe(d: Disposable) {
            }

            override fun onNext(t: FileResponse) {
//                audioData.setImageUrl(BuildConfig.CHATROOM_URL+t.message.attachments[0].image_url)
                audioData.setSuccess(t.success.toString())
                audioData.setAudioUrl(BuildConfig.CHATROOM_URL+t.message.attachments[0].title_link)
                messageUpdate.value = audioData
                Log.e("Peter", "postImageMessage onNext:  "+t.success.toString())
                Log.e("Peter", "postImageMessage onNext:  "+t.message._id)

            }

            override fun onError(e: Throwable) {
                audioData.setSuccess("false")
                messageUpdate.value = audioData
                Log.e("Peter", "postImageMessage onError:  $e")
            }

        }
        ApiMethods.postAudioMessage(imageMessageObserve, file, rId)
    }

    fun postTextMessage(
        dataBody: TextMessage,
        rId: String,
        data: com.example.myapplication.datamodle.chat_room.Message
    ){
        val textMessageObserve: Observer<TextResponse> = object :Observer<TextResponse>{
            override fun onComplete() {
                Log.e("Peter", "postTextMessage onComplete")
            }

            override fun onSubscribe(d: Disposable) {
            }

            override fun onNext(t: TextResponse) {
                data.setSuccess(t.success.toString())
                messageUpdate.value = data
                Log.e("Peter", "postTextMessage onNext:  "+t.success.toString())
            }

            override fun onError(e: Throwable) {
                data.setSuccess("false")
                messageUpdate.value = data
                Log.e("Peter", "postTextMessage onError:  $e")
            }

        }
        ApiMethods.postTextMessage(textMessageObserve, dataBody, rId)
    }
}