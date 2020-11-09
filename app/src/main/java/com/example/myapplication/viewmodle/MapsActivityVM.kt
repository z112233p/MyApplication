package com.example.myapplication.viewmodle

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.datamodle.event.Events
import com.example.myapplication.network.ApiMethods
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

class MapsActivityVM (application: Application) : AndroidViewModel(application){
    private val mapAddress: MutableLiveData<String> = MutableLiveData<String>()
    
    fun getMapAddress(): LiveData<String>{
        return mapAddress
    }

    fun getMapAddressData(latlng: ArrayList<Double>, key: String){
        val mapAddressObserver = object :Observer<String>{
            override fun onComplete() {
            }

            override fun onSubscribe(d: Disposable) {
            }

            override fun onNext(t: String) {
                Log.e("Peter","getMapAddressData  onNext:  ${t}")
            }

            override fun onError(e: Throwable) {
                Log.e("Peter","getMapAddressData  onError:  ${e.localizedMessage}")
            }

        }
        ApiMethods.getMapAddress(mapAddressObserver, latlng, key)
        
    }
}