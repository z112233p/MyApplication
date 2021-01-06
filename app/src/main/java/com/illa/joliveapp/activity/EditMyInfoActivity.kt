package com.illa.joliveapp.activity

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.illa.joliveapp.R
import com.illa.joliveapp.tools.PrefHelper
import com.illa.joliveapp.tools.ProgressDialogController
import com.illa.joliveapp.viewmodle.ProfileActivityVM
import kotlinx.android.synthetic.main.activity_main.toolbar
import org.koin.androidx.viewmodel.ext.android.viewModel


class EditMyInfoActivity : AppCompatActivity() {

    val profileActivityVM: ProfileActivityVM by viewModel()
    val dataBody :HashMap<String, String> = HashMap()
    val interestArrayList = ArrayList<Int>()
    var jobId: Int = -1
    private lateinit var actionItem: MenuItem



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_my_info)
        setSupportActionBar(toolbar)
        ProgressDialogController.setContext(this)
        title = ""
        initObserve()
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
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

    fun hideActionItem(){
        actionItem.isVisible = false
    }

    fun showActionItem(){
//        actionItem.isVisible = true
    }


    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        actionItem = menu!!.findItem(R.id.action_store)
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_edit_my_info, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_store ->{
                Log.e("Peter","FragmentEditMyInfoV2 ACT onOptionsItemSelected")


                false
            }
            else -> false

        }
        return false
    }

}