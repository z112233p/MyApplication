package com.example.myapplication.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.example.myapplication.BuildConfig
import com.example.myapplication.R
import com.example.myapplication.datamodle.chat_room.Message
import com.example.myapplication.fragment.FragmentLogin
import com.example.myapplication.tools.PrefHelper

import com.example.myapplication.viewmodle.MainActivityVM
import com.theartofdev.edmodo.cropper.CropImage

import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File


@Suppress("CAST_NEVER_SUCCEEDS", "UNREACHABLE_CODE")
class MainActivity : AppCompatActivity() {

    val mainActVM: MainActivityVM by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        Log.e("Peter","MainActivity ")

//        initObserve()
    }

    fun initObserve(){
        mainActVM.getLoginResponse().observe(this, androidx.lifecycle.Observer {
            Log.e("Peter","MAINACT Observe")

            val navHostFragment = supportFragmentManager.fragments.first() as? NavHostFragment
            if(navHostFragment != null) {
                val childFragments = navHostFragment.childFragmentManager.fragments
                childFragments.forEach { f ->
                    if (f != null && f is FragmentLogin){
                        Log.e("Peter","MAINACT Observe Loop  IN "+f.parentFragment)
                        f.test()
                        return@Observer
                    }
                }
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}