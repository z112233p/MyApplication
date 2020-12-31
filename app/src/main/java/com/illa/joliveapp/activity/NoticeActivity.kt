package com.illa.joliveapp.activity

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.illa.joliveapp.R
import com.illa.joliveapp.tools.ProgressDialogController
import com.illa.joliveapp.tools.Tools
import com.illa.joliveapp.viewmodle.MainActivityVM

import kotlinx.android.synthetic.main.activity_main.toolbar
import org.koin.androidx.viewmodel.ext.android.viewModel


class NoticeActivity : AppCompatActivity() {

    val  mainActVM: MainActivityVM by viewModel()
    val dataBody :HashMap<String, String> = HashMap()

    private lateinit var actionItem: MenuItem
    private var actionReadDisClickable: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notice)
        setSupportActionBar(toolbar)
        ProgressDialogController.setContext(this)
        title = ""
        initObserve()
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        initObserve()

        ProgressDialogController.setContext(this)
    }

    fun setActionReadDisClickable(){
        actionReadDisClickable = false
        actionItem.isCheckable = false

    }

    fun setActionReadClickable(){
        actionReadDisClickable = true
        actionItem.isCheckable = true

    }


    private fun initObserve(){
        mainActVM.getProgressStatus().observe(this, Observer {
            if(it){
                ProgressDialogController.dismissProgress()
            } else {
                ProgressDialogController.showProgress()
            }
        })
        mainActVM.noticeAllReadResponse().observe(this, Observer {
            Tools.toast(this, "已設為全已讀")
            setActionReadDisClickable()
            mainActVM.getNotice()
        })
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        actionItem = menu!!.findItem(R.id.action_read)
        actionItem.isCheckable = false

        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_notice, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_read ->{
                if(actionReadDisClickable){
                    mainActVM.setNoticeAllRead()
                }
                false
            }
            else -> false

        }
        return false
    }
}