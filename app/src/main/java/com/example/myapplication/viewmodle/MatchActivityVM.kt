package com.example.myapplication.viewmodle

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.Observer
import com.example.myapplication.datamodle.dating.DatingSearch
import com.example.myapplication.datamodle.dating.DatingSearchData
import com.example.myapplication.network.ApiMethods
import com.example.myapplication.tools.SingleLiveEvent
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.json.JSONObject

class MatchActivityVM (application: Application) : AndroidViewModel(application){
    private val mContent: Context? = application.applicationContext
    private val disContainer : CompositeDisposable = CompositeDisposable()

    //set
    private val datingSearchData: MutableLiveData<DatingSearchData> = MutableLiveData<DatingSearchData>()

    //get
    fun getDatingSearchData(): LiveData<DatingSearchData>? {
        return datingSearchData
    }

    fun getDatingSearch(gender: Int, minAge: Int, maxAge: Int, maxKm: Int){
        val datingSearchObserver: Observer<DatingSearch> = object : Observer<DatingSearch>{
            override fun onComplete() {
            }

            override fun onSubscribe(d: Disposable) {
            }

            override fun onNext(t: DatingSearch) {
                Log.e("Peter","getDatingSearch onNext   "+ t)
                datingSearchData.value = t.data
            }

            override fun onError(e: Throwable) {
                Log.e("Peter", "getDatingSearch onError   ${e.message}")

            }
        }
        ApiMethods.getDatingSearch(datingSearchObserver, gender, minAge, maxAge, maxKm)
        val test = "{\n" +
                "  \"code\": 0,\n" +
                "  \"msg\": \"\",\n" +
                "  \"data\": {\n" +
                "    \"users\": [\n" +
                "      {\n" +
                "        \"id\": 29,\n" +
                "        \"name\": \"peter\",\n" +
                "        \"gender\": 0,\n" +
                "        \"about\": null,\n" +
                "        \"job_title\": null,\n" +
                "        \"job_id\": null,\n" +
                "        \"country_id\": null,\n" +
                "        \"age\": 30,\n" +
                "        \"label\": \"djilz1\",\n" +
                "        \"photos\": [\n" +
                "          {\n" +
                "            \"url\": \"ff36e62eee53304476677b193de905df.jpg\",\n" +
                "            \"sort\": 0\n" +
                "          }\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 32,\n" +
                "        \"name\": \"555\",\n" +
                "        \"gender\": 0,\n" +
                "        \"about\": null,\n" +
                "        \"job_title\": null,\n" +
                "        \"job_id\": null,\n" +
                "        \"country_id\": null,\n" +
                "        \"age\": 30,\n" +
                "        \"label\": \"HiNPQ1\",\n" +
                "        \"photos\": []\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}"

        val jsonObject = JSONObject(test)
        var gson = Gson()

        var testModel = gson.fromJson(test, DatingSearch::class.java)
        Log.e("Peter","????    "+ testModel.data?.users?.get(0)?.photos?.size)
//        datingSearchData.value = testModel.data


    }


}