package com.illa.joliveapp.activity

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.illa.joliveapp.R
import com.illa.joliveapp.tools.ProgressDialogController

import com.illa.joliveapp.viewmodle.MainActivityVM

import kotlinx.android.synthetic.main.activity_main.toolbar
import org.koin.androidx.viewmodel.ext.android.viewModel


@Suppress("CAST_NEVER_SUCCEEDS", "UNREACHABLE_CODE")
class MainActivity : AppCompatActivity() {

    val mainActVM: MainActivityVM by viewModel()
    private val PERMISSION_REQUEST_READ_STORAGE = 0
    private val PERMISSION_REQUEST_WRITE_STORAGE = 1
    private val PERMISSION_REQUEST_RECORD_AUDIO = 2
    private val PERMISSION_ACCESS_COARSE_LOCATION = 3
    private val PERMISSION_ACCESS_FINE_LOCATION = 4

    private var hasAskREAD_STORAGE = false
    private var hasAskWRITE_STORAGE = false
    private var hasAskRECORD_AUDIO = false
    private var hasAskCOARSE_LOCATION = false
    private var hasAskFINE_LOCATION = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        ProgressDialogController.setContext(this)

        title = ""
        initObserve()
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        askPermissions()
    }

    private fun askPermissions(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && !hasAskWRITE_STORAGE){
            ActivityCompat.requestPermissions((this as Activity?)!!, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), PERMISSION_REQUEST_WRITE_STORAGE)

//        } else if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && !hasAskREAD_STORAGE){
//            ActivityCompat.requestPermissions((this as Activity?)!!, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSION_REQUEST_READ_STORAGE)

        }  else if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED && !hasAskRECORD_AUDIO){
            ActivityCompat.requestPermissions((this as Activity?)!!, arrayOf(Manifest.permission.RECORD_AUDIO), PERMISSION_REQUEST_RECORD_AUDIO)

//        } else if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && !hasAskCOARSE_LOCATION){
//            ActivityCompat.requestPermissions((this as Activity?)!!, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), PERMISSION_ACCESS_COARSE_LOCATION)

        } else if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && !hasAskFINE_LOCATION){
            ActivityCompat.requestPermissions((this as Activity?)!!, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_ACCESS_FINE_LOCATION)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.e("Peter","onRequestPermissionsResult   $requestCode    ${grantResults[0]}}")

        when(requestCode){
            PERMISSION_REQUEST_READ_STORAGE -> hasAskREAD_STORAGE = true
            PERMISSION_REQUEST_RECORD_AUDIO -> hasAskRECORD_AUDIO = true
            PERMISSION_REQUEST_WRITE_STORAGE -> hasAskWRITE_STORAGE = true
            PERMISSION_ACCESS_COARSE_LOCATION -> hasAskCOARSE_LOCATION = true
            PERMISSION_ACCESS_FINE_LOCATION -> hasAskFINE_LOCATION = true

        }

        if(!hasAskREAD_STORAGE || !hasAskRECORD_AUDIO || !hasAskWRITE_STORAGE
            || !hasAskCOARSE_LOCATION || !hasAskFINE_LOCATION){
            askPermissions()
        }
    }

    override fun onResume() {
        super.onResume()
        Log.e("Peter","MainActivity onResume")
        ProgressDialogController.setContext(this)
    }

    private fun initObserve(){
        mainActVM.getProgressStatus().observe(this, Observer {
            if(it){
                ProgressDialogController.dismissProgress()
            } else {
                ProgressDialogController.showProgress()
            }
        })
    }
}