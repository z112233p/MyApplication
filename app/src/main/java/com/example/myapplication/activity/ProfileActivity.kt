package com.example.myapplication.activity

import android.os.Bundle
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


class ProfileActivity : AppCompatActivity() {

    val profileActivityVM: ProfileActivityVM by viewModel()
    val dataBody :HashMap<String, String> = HashMap()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setSupportActionBar(toolbar)
        ProgressDialogController.setContext(this)
        title = ""
        initObserve()
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }


    fun setStepOne(){
        ll_step.weightSum = 3F

        val param = LinearLayout.LayoutParams(
            0,
           3,
            1F
        )
        tv_step.layoutParams = param
        ll_step.weightSum = 3F

    }

    fun setStepTwo(){
        ll_step.weightSum = 3F

        val param = LinearLayout.LayoutParams(
            0,
           3,
            2F
        )
        tv_step.layoutParams = param
        ll_step.weightSum = 3F

    }

    fun setStepThree(){
        ll_step.weightSum = 3F

        val param = LinearLayout.LayoutParams(
            0,
            3,
            3F
        )
        tv_step.layoutParams = param
        ll_step.weightSum = 3F

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
}