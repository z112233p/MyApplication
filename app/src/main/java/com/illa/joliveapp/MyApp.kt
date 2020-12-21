package com.illa.joliveapp

import android.app.Application
import com.illa.joliveapp.datamodle.profile.interest.Data
import com.illa.joliveapp.datamodle.profile.interest.interest
import com.illa.joliveapp.datamodle.profile.job.job
import com.illa.joliveapp.viewmodle.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import java.util.HashMap


class MyApp : Application(){
    val location: HashMap<String, Int> = HashMap()
    var interestDataList:interest = interest(0, ArrayList<Data>(), "")
    var jobDataList: job = job(0, ArrayList<com.illa.joliveapp.datamodle.profile.job.Data>(), "")

    override fun onCreate() {
        super.onCreate()
        _instance = this

        location["taipei"] = R.mipmap.ph_taipei
        location["taichung"] = R.mipmap.ph_taichung
        location["kaohsiung"] = R.mipmap.ph_kaohsiung
        location["san_srancisco"] = R.mipmap.ph_san_srancisco
        location["osaka"] = R.mipmap.ph_osaka
        location["tokyo"] = R.mipmap.ph_tokyo
        location["hong_kong"] = R.mipmap.ph_hong_kong

        startKoin {
            // Android context
            androidContext(this@MyApp)
            // modules
            val list = listOf(MainActVM,
                MatchActVM, ChatRoomActVM, EventsActVM,
                MapsActVM, CreateEventsActVM, ProfileAcVM, EventDetailActVM)
            modules(list)
        }
    }

    fun getInterest(id: Int): String{
        interestDataList.data.forEach {
            if(id == it.id){
                return it.i18n
            }
        }
        return ""
    }

    fun getJob(id: Int): String{
        jobDataList.data.forEach {
            if(id == it.id){
                return it.i18n
            }
        }
        return ""
    }

    companion object {
        val TAG = MyApp::class.java.simpleName
        private var _instance: MyApp? = null

        @JvmStatic
        fun get(): MyApp? {
            return _instance
        }
    }
}