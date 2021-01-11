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


    override fun onCreate() {
        super.onCreate()
        _instance = this

        location["台北"] = R.mipmap.ph_taipei
        location["台中"] = R.mipmap.ph_taichung_gray
        location["高雄"] = R.mipmap.ph_kaohsiung_gray
        location["舊金山"] = R.mipmap.ph_san_srancisco_gray
        location["大阪"] = R.mipmap.ph_osaka_gray
        location["東京"] = R.mipmap.ph_tokyo_gray
        location["香港"] = R.mipmap.ph_hong_kong_gray
        location["巴黎"] = R.mipmap.ph_paris_gray
        location["其他"] = R.mipmap.ph_other_gray

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
    
    
    fun setInterest(it: interest) {
        interestDataList = it
    }
    
    fun setJob(it: job) {
        jobDataList = it
    }

    companion object {
        val TAG = MyApp::class.java.simpleName
        private var _instance: MyApp? = null
        var interestDataList:interest = interest(0, ArrayList<Data>(), "")
        var jobDataList: job = job(0, ArrayList<com.illa.joliveapp.datamodle.profile.job.Data>(), "")
        @JvmStatic
        fun get(): MyApp? {
            return _instance
        }
    }
}