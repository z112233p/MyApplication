package com.example.myapplication.viewmodle

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.tools.SingleLiveEvent
import com.example.myapplication.datamodle.authorization.LoginData
import com.example.myapplication.datamodle.authorization.LoginResponse
import com.example.myapplication.datamodle.chat.ChatRoomList
import com.example.myapplication.datamodle.chat.history.ChatHistory
import com.example.myapplication.datamodle.profile.MyInfo
import com.example.myapplication.datamodle.profile.MyInfoData
import com.example.myapplication.network.ApiMethods
import com.example.myapplication.tools.LogUtil
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.json.JSONObject

class MainActivityVM(application: Application) : AndroidViewModel(application) {

    private val mContent: Context? = application.applicationContext
    private val disContainer :CompositeDisposable = CompositeDisposable()

    //set
    private val loginResponse : SingleLiveEvent<LoginResponse> = SingleLiveEvent<LoginResponse>()
    private val myInfoData: MutableLiveData<MyInfoData> = MutableLiveData<MyInfoData>()
    private val chatRoomList: MutableLiveData<ChatRoomList> = MutableLiveData<ChatRoomList>()
    private val chatHistory: MutableLiveData<String> = MutableLiveData<String>()


    //get
    fun getLoginResponse(): LiveData<LoginResponse> {
        return loginResponse
    }
    fun getMyInfoData(): LiveData<MyInfoData> {
        return myInfoData
    }

    fun getChatRoomListData(): LiveData<ChatRoomList> {
        return chatRoomList
    }

    fun getChatHistoryData(): LiveData<String> {
        return chatHistory
    }

    //Api Function
    fun login(loginData: LoginData?){
        Log.e("Peter222", "login")

        val loginObserver: Observer<LoginResponse> = object : Observer<LoginResponse>{
            override fun onComplete() {
            }

            override fun onSubscribe(d: Disposable) {
                disContainer.add(d)
            }

            override fun onError(e: Throwable) {
                Log.e("Peter222", "onError$e")

            }

            override fun onNext(t: LoginResponse) {
                Log.e("Peter2", "onNext:  "+t.data.user_token)
                loginResponse.value = t
            }
        }
        loginData.let { ApiMethods.login(loginObserver,it) }
    }

    fun getMyInfo(){
        val myInfoObserver: Observer<MyInfo> = object  : Observer<MyInfo>{
            override fun onComplete() {
            }

            override fun onSubscribe(d: Disposable) {
                disContainer.add(d)
            }

            override fun onNext(t: MyInfo) {
                Log.e("Peter2", "getMyInfo  onNext:  "+t.data.user.birthday)
                myInfoData.value = t.data
            }

            override fun onError(e: Throwable) {
            }
        }
        ApiMethods.getMyInfo(myInfoObserver)
    }

    fun getChatRoomList(){
        val chatRoomList: Observer<ChatRoomList> = object : Observer<ChatRoomList>{
            override fun onComplete() {
            }

            override fun onSubscribe(d: Disposable) {
            }

            override fun onError(e: Throwable) {
                Log.e("Peter2", "chatRoomList onError:  $e")

            }

            override fun onNext(t: ChatRoomList) {
                chatRoomList.value = t
            }

        }
        ApiMethods.getChatRoomList(chatRoomList)
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
                LogUtil.e("PetergetChatHistory", "getChatHistory onNext:  $t")
            }

        }
        ApiMethods.getChatHistory(chatHistoryObserve, rid)
    }
}