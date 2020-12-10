package com.example.myapplication.viewmodle

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.datamodle.authorization.*
import com.example.myapplication.tools.SingleLiveEvent
import com.example.myapplication.datamodle.authorization.register.Register
import com.example.myapplication.datamodle.authorization.register.RegisterResponse
import com.example.myapplication.datamodle.chat.chatroom_list.ChatRoomList
import com.example.myapplication.datamodle.profile.MyInfo
import com.example.myapplication.datamodle.profile.MyInfoData
import com.example.myapplication.datamodle.profile.MyInfoPhoto
import com.example.myapplication.datamodle.profile.delete_photo.DeleteMyPhoto
import com.example.myapplication.datamodle.profile.delete_photo.response.DeleteMyPhotoResponse
import com.example.myapplication.datamodle.profile.update_photo.UpdatePhotoResponse
import com.example.myapplication.datamodle.profile.update.UpdateMtInfo
import com.example.myapplication.datamodle.profile.update.UpdateMyInfoResponse
import com.example.myapplication.network.ApiMethods
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.io.File

class MainActivityVM(application: Application) : AndroidViewModel(application) {

    private val mContent: Context? = application.applicationContext
    private val disContainer :CompositeDisposable = CompositeDisposable()

    //set
    private val loginResponse : SingleLiveEvent<LoginResponse> = SingleLiveEvent<LoginResponse>()
    private val registerResponse : SingleLiveEvent<RegisterResponse> = SingleLiveEvent<RegisterResponse>()
    private val updateMyInfoResponse: SingleLiveEvent<UpdateMyInfoResponse> = SingleLiveEvent<UpdateMyInfoResponse>()
    private val resendSMSCheck: SingleLiveEvent<Boolean> = SingleLiveEvent<Boolean>()
    private val myInfoData: MutableLiveData<MyInfoData> = MutableLiveData<MyInfoData>()
    private val myPhoto: MutableLiveData<List<MyInfoPhoto>> = MutableLiveData<List<MyInfoPhoto>>()
    private val deleteMyPhoto: SingleLiveEvent<DeleteMyPhotoResponse> = SingleLiveEvent<DeleteMyPhotoResponse>()
    private val chatRoomList: MutableLiveData<ChatRoomList> = MutableLiveData<ChatRoomList>()
    private val chatHistory: MutableLiveData<String> = MutableLiveData<String>()
    private val progressStatus: MutableLiveData<Boolean> = MutableLiveData<Boolean>()


    //get
    fun getRegisterResponse(): LiveData<RegisterResponse> {
        return registerResponse
    }

    fun getLoginResponse(): LiveData<LoginResponse> {
        return loginResponse
    }

    fun getMyInfoData(): LiveData<MyInfoData> {
        return myInfoData
    }

    fun getUpdateMyInfoResponse(): LiveData<UpdateMyInfoResponse> {
        return updateMyInfoResponse
    }

    fun getMyPhoto(): LiveData<List<MyInfoPhoto>> {
        return myPhoto
    }

    fun getDeleteMyPhoto(): LiveData<DeleteMyPhotoResponse> {
        return deleteMyPhoto
    }

    fun getChatRoomListData(): LiveData<ChatRoomList> {
        return chatRoomList
    }

    fun getResendSMSCheck(): LiveData<Boolean>{
        return resendSMSCheck
    }

    fun getChatHistoryData(): LiveData<String> {
        return chatHistory
    }

    fun getProgressStatus(): LiveData<Boolean> {
        return progressStatus
    }

    //Api Function
    fun resendSMS(resendSMS: ResendSMS){
        val resendSMSObserver: Observer<ResendResponse> = object : Observer<ResendResponse>{
            override fun onComplete() {
                progressStatus.value = true
            }

            override fun onSubscribe(d: Disposable) {
                progressStatus.value = false
                disContainer.add(d)
            }

            override fun onError(e: Throwable) {
                progressStatus.value = true

                Log.e("Peter222", "onError${e.message}")

            }

            override fun onNext(t: ResendResponse) {
                Log.e("Peter2", "onNext:  "+t)
                resendSMSCheck.value = t.data.resend
            }
        }
        resendSMS.let { ApiMethods.resendSMS(resendSMSObserver,it) }
    }


    fun register(register: Register){
        val registerObserver: Observer<RegisterResponse> = object : Observer<RegisterResponse>{
            override fun onComplete() {
                progressStatus.value = true

            }

            override fun onSubscribe(d: Disposable) {
                progressStatus.value = false
                disContainer.add(d)
            }

            override fun onError(e: Throwable) {
                progressStatus.value = true
                registerResponse.value = null
                Log.e("Peter222", "onError  register   ${e.message}")

            }

            override fun onNext(t: RegisterResponse) {
                registerResponse.value = t
                Log.e("Peter2", "register   onNext:  "+t.data)
            }
        }
        register.let { ApiMethods.register(registerObserver,it) }
    }

    fun login(loginData: LoginData?){
        Log.e("Peter222", "login")

        val loginObserver: Observer<LoginResponse> = object : Observer<LoginResponse>{
            override fun onComplete() {
                progressStatus.value = true
            }

            override fun onSubscribe(d: Disposable) {
                progressStatus.value = false
                disContainer.add(d)
            }

            override fun onError(e: Throwable) {
                progressStatus.value = true

                Log.e("Peterlogin", "onError${e.localizedMessage}")
                val data = LoginResponse()
                loginResponse.value = data

            }

            override fun onNext(t: LoginResponse) {
                Log.e("Peterlogin", "onNext:  "+t)
                loginResponse.value = t
            }
        }
        loginData.let { ApiMethods.login(loginObserver,it) }
    }

    fun getMyInfo(){
        val myInfoObserver: Observer<MyInfo> = object  : Observer<MyInfo>{
            override fun onComplete() {
                progressStatus.value = true
            }

            override fun onSubscribe(d: Disposable) {
                progressStatus.value = false
                disContainer.add(d)
            }

            override fun onNext(t: MyInfo) {
                Log.e("Peter2", "getMyInfo  onNext:  "+t.data.user.birthday)
                myInfoData.value = t.data
            }

            override fun onError(e: Throwable) {
                progressStatus.value = true

            }
        }
        ApiMethods.getMyInfo(myInfoObserver)
    }

    fun clearMyInfo(){
        myInfoData.value = null
    }

    fun updateMyInfo(updateMtInfo: UpdateMtInfo){
        val myInfoObserver: Observer<UpdateMyInfoResponse> = object  : Observer<UpdateMyInfoResponse>{
            override fun onComplete() {
                progressStatus.value = true
            }

            override fun onSubscribe(d: Disposable) {
                progressStatus.value = false
                disContainer.add(d)
            }

            override fun onNext(t: UpdateMyInfoResponse) {
                updateMyInfoResponse.value = t
                Log.e("Peter2", "updateMyInfo  onNext:  "+t)
            }

            override fun onError(e: Throwable) {
                progressStatus.value = true

                Log.e("Peter2", "updateMyInfo  onError:  "+e.message)

            }
        }
        ApiMethods.updateMyInfo(myInfoObserver, updateMtInfo)
    }

    fun updateMyPhoto(
        sort: String,
        file: File
    ){
        val imageMessageObserve: Observer<UpdatePhotoResponse> = object :Observer<UpdatePhotoResponse>{
            override fun onComplete() {
                Log.e("Peter", "postImageMessage onComplete")
                progressStatus.value = true
            }

            override fun onSubscribe(d: Disposable) {
                progressStatus.value = false
            }

            override fun onNext(t: UpdatePhotoResponse) {
                myPhoto.value = t.data.photos
                Log.e("Peter", "updateMyPhoto onNext:  ${t.toString()}")

            }

            override fun onError(e: Throwable) {
                progressStatus.value = true

                Log.e("Peter", "updateMyPhoto onError:  $e")
            }

        }
        ApiMethods.updateMyPhoto(imageMessageObserve, sort, file)
    }

    fun deleteMyInfo(dataBody: DeleteMyPhoto){
        val deleteMyPhotoObserver: Observer<DeleteMyPhotoResponse> = object  : Observer<DeleteMyPhotoResponse>{
            override fun onComplete() {
                progressStatus.value = true
            }

            override fun onSubscribe(d: Disposable) {
                progressStatus.value = false
                disContainer.add(d)
            }

            override fun onNext(t: DeleteMyPhotoResponse) {
                deleteMyPhoto.value = t
                Log.e("Peter2", "deleteMyInfo  onNext:  "+t)
            }

            override fun onError(e: Throwable) {
                progressStatus.value = true

                Log.e("Peter2", "deleteMyInfo  onError:  "+e.message)

            }
        }
        ApiMethods.deleteMyInfo(deleteMyPhotoObserver, dataBody)
    }

    fun getChatRoomList(){
        val chatRoomList: Observer<ChatRoomList> = object : Observer<ChatRoomList>{
            override fun onComplete() {
                progressStatus.value = true
            }

            override fun onSubscribe(d: Disposable) {
                progressStatus.value = false
            }

            override fun onError(e: Throwable) {
                progressStatus.value = true

                Log.e("Peter2", "chatRoomList onError:  $e")

            }

            override fun onNext(t: ChatRoomList) {
                chatRoomList.value = t
            }

        }
        ApiMethods.getChatRoomList(chatRoomList)
    }

}