package com.example.myapplication.activity

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.myapplication.R
import com.example.myapplication.tools.ProgressDialogController
import com.example.myapplication.viewmodle.MainActivityVM
import com.example.myapplication.viewmodle.ProfileActivityVM

import kotlinx.android.synthetic.main.activity_main.toolbar
import org.koin.androidx.viewmodel.ext.android.viewModel


class SeeMoreActivity : AppCompatActivity() {

    val profileActivityVM: ProfileActivityVM by viewModel()
    var sortType: String? = ""
    var eventsCategorysId: String? = ""
    var barTitle: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notice)
        setSupportActionBar(toolbar)
        getIntentData()
        title = ""

        initObserve()
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        ProgressDialogController.setContext(this)

    }
    private fun getIntentData(){
        val b = intent.extras ?: return
        sortType = b.getString("sortType")!!
        eventsCategorysId = b.getString("eventsCategorysId")!!
        if(TextUtils.isEmpty(eventsCategorysId)){
            eventsCategorysId = null
        }
        if(TextUtils.isEmpty(sortType)){
            sortType = null
        }
        barTitle = b.getString("title")!!

    }

    private fun initObserve(){
        profileActivityVM.getProgressStatus().observe(this, Observer {
            if(it){
                ProgressDialogController.dismissProgress()
            } else {
                ProgressDialogController.showProgress()
            }
        })
    }


//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.menu_edit_my_info, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when(item.itemId){
//            R.id.action_store ->{
//                Log.e("Peter","FragmentEditMyInfoV2 ACT onOptionsItemSelected")
//
//
//                false
//            }
//            else -> false
//
//        }
//        return false
//    }
}