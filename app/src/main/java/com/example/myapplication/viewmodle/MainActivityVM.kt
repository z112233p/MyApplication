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
import com.example.myapplication.datamodle.profile.MyInfo
import com.example.myapplication.datamodle.profile.MyInfoData
import com.example.myapplication.rx.ApiMethods
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class MainActivityVM(application: Application) : AndroidViewModel(application) {

    private val mContent: Context? = application.applicationContext
    private val disContainer :CompositeDisposable = CompositeDisposable()

    //set
    private val loginResponse : SingleLiveEvent<LoginResponse> = SingleLiveEvent<LoginResponse>()
    private val myInfoData: MutableLiveData<MyInfoData> = MutableLiveData<MyInfoData>()

    //get
    fun getLoginResponse(): LiveData<LoginResponse> {
        return loginResponse
    }
    fun getMyInfoData(): LiveData<MyInfoData> {
        return myInfoData
    }

    fun login(loginData: LoginData?){
        val loginObserver: Observer<LoginResponse> = object : Observer<LoginResponse>{
            override fun onComplete() {
            }

            override fun onSubscribe(d: Disposable) {
                disContainer.add(d)
            }

            override fun onError(e: Throwable) {
                Log.e("Peter2", "onError$e")

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
}