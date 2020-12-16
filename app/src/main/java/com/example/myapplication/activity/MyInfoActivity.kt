package com.example.myapplication.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Observer
import com.example.myapplication.R
import com.example.myapplication.adapter.AdapterPager
import com.example.myapplication.fragment.*
import com.example.myapplication.tools.IntentHelper
import com.example.myapplication.tools.ProgressDialogController
import com.example.myapplication.tools.Tools
import com.example.myapplication.viewmodle.ProfileActivityVM
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_myinfo.*
import kotlinx.android.synthetic.main.activity_myinfo.toolbar
import kotlinx.android.synthetic.main.fragment_create_event_v2.*
import kotlinx.android.synthetic.main.fragment_event_main_v2.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyInfoActivity : AppCompatActivity() {
    val profileActivityVM: ProfileActivityVM by viewModel()


    private var f1 = FragmentMyinfo_info()
    private var f2 = FragmentMyinfo_event()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_myinfo)
        setSupportActionBar(toolbar)
        title = ""
        init()
        initObserve()
    }

    override fun onResume() {
        super.onResume()
        ProgressDialogController.setContext(this)
    }

    private fun init(){
        val tabTitle = mutableListOf<String>(*resources.getStringArray(R.array.my_info_pager))
        val myInfoFragments : ArrayList<Fragment> = arrayListOf()
        myInfoFragments.add(f1)
        myInfoFragments.add(f2)

        val pagerAdapter = AdapterPager(supportFragmentManager, tabTitle, myInfoFragments)
        vp_my_info.adapter = pagerAdapter
        vp_my_info.currentItem = 0
        tab_my_info.setupWithViewPager(vp_my_info)


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
        menuInflater.inflate(R.menu.menu_my_info, menu)
        return true
    }

    @SuppressLint("Range")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        messagesList.scrollToPosition((messagesList.getChildAt (0).layoutParams as RecyclerView.LayoutParams).viewAdapterPosition)
        return when (item.itemId) {
            R.id.action_option -> {
                Log.e("Peter","MyInfoActivity onOptionsItemSelected action_option")
                IntentHelper.gotoSettingActivity(this)

                false
            }
            R.id.action_edit -> {
                Log.e("Peter","MyInfoActivity onOptionsItemSelected action_edit")

                IntentHelper.gotoEditMyInfoActivity(this)
                false
            }
            else -> false
        }
    }
}