package com.example.myapplication

import android.app.Application
import com.example.myapplication.viewmodle.MainActVM
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class MyApp : Application(){

    override fun onCreate() {
        super.onCreate()
        _instance = this


        startKoin {
            // Android context
            androidContext(this@MyApp)
            // modules
            val list = listOf(MainActVM)
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