package com.example.myapplication

import android.app.Application
import com.example.myapplication.viewmodle.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import java.util.HashMap


class MyApp : Application(){
    val location: HashMap<String, Int> = HashMap()


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
                MatchActVM, ChatRoomActVM, EventsActVM, MapsActVM)
            modules(list)
        }
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