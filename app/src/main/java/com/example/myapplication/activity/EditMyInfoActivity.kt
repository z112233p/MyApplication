package com.example.myapplication.activity

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.myapplication.R
import com.example.myapplication.tools.ProgressDialogController
import com.example.myapplication.viewmodle.ProfileActivityVM
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.android.synthetic.main.activity_profile.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class EditMyInfoActivity : AppCompatActivity() {

    val profileActivityVM: ProfileActivityVM by viewModel()
    val dataBody :HashMap<String, String> = HashMap()
    val interestArrayList = ArrayList<Int>()
    var jobId: Int = -1


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