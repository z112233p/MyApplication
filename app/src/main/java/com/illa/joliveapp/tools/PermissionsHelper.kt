@file:Suppress("CAST_NEVER_SUCCEEDS")

package com.illa.joliveapp.tools

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionsHelper {

    private lateinit var mContext: Context

    private val PERMISSION_REQUEST_READ_STORAGE = 0
    private val PERMISSION_REQUEST_WRITE_STORAGE = 1
    private val PERMISSION_REQUEST_RECORD_AUDIO = 2
    private val PERMISSION_ACCESS_COARSE_LOCATION = 3
    private val PERMISSION_ACCESS_FINE_LOCATION = 4

    private var hasAskREAD_STORAGE = true
    private var hasAskWRITE_STORAGE = true
    private var hasAskRECORD_AUDIO = true
    private var hasAskCOARSE_LOCATION = true
    private var hasAskFINE_LOCATION = true

    private var callback: onResultCallback ?= object : onResultCallback{
        override fun permissionResult() {
            Log.e("Peter","PermissionsHelper   permissionResult")
        }

    }
    interface onResultCallback{
        fun permissionResult()
    }

    fun setContext(ctx: Context){
        mContext = ctx
    }

    fun askReadStorage(){
        hasAskREAD_STORAGE = false
    }

    fun askWriteStorage(){
        hasAskWRITE_STORAGE = false
    }

    fun askRecordAudio(){
        hasAskRECORD_AUDIO = false
    }

    fun askCoarseLocation(){
        hasAskCOARSE_LOCATION = false
    }

    fun askFineLocation(){
        hasAskFINE_LOCATION = false
    }

    fun setCallBack(param: onResultCallback){
        this.callback = null
        this.callback = param
    }

    fun startAskPermissions(){
        if(ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && !hasAskWRITE_STORAGE){
            ActivityCompat.requestPermissions((mContext as Activity?)!!, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), PERMISSION_REQUEST_WRITE_STORAGE)

//        } else if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && !hasAskREAD_STORAGE){
//            ActivityCompat.requestPermissions((this as Activity?)!!, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSION_REQUEST_READ_STORAGE)

        }  else if(ContextCompat.checkSelfPermission(mContext, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED && !hasAskRECORD_AUDIO){
            ActivityCompat.requestPermissions((mContext as Activity?)!!, arrayOf(Manifest.permission.RECORD_AUDIO), PERMISSION_REQUEST_RECORD_AUDIO)

//        } else if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && !hasAskCOARSE_LOCATION){
//            ActivityCompat.requestPermissions((this as Activity?)!!, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), PERMISSION_ACCESS_COARSE_LOCATION)

        } else if(ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && !hasAskFINE_LOCATION){
            ActivityCompat.requestPermissions((mContext as Activity?)!!, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_ACCESS_FINE_LOCATION)
        }
    }

    fun onResult(requestCode: Int, i: Int) {
        when (requestCode) {
            PERMISSION_REQUEST_READ_STORAGE -> hasAskREAD_STORAGE = true
            PERMISSION_REQUEST_RECORD_AUDIO -> hasAskRECORD_AUDIO = true
            PERMISSION_REQUEST_WRITE_STORAGE -> hasAskWRITE_STORAGE = true
            PERMISSION_ACCESS_COARSE_LOCATION -> hasAskCOARSE_LOCATION = true
            PERMISSION_ACCESS_FINE_LOCATION -> hasAskFINE_LOCATION = true

        }

        if (!hasAskREAD_STORAGE || !hasAskRECORD_AUDIO || !hasAskWRITE_STORAGE
            || !hasAskCOARSE_LOCATION || !hasAskFINE_LOCATION
        ) {
            startAskPermissions()
        }
        if (i == 0) {
            callback?.permissionResult()
        }
    }
}