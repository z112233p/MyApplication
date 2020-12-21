package com.illa.joliveapp.activity

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.illa.joliveapp.MyApp
import com.illa.joliveapp.R
import com.illa.joliveapp.tools.IntentHelper
import com.illa.joliveapp.tools.PrefHelper
import com.illa.joliveapp.viewmodle.ProfileActivityVM
import kotlinx.android.synthetic.main.activity_launch.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class  LaunchActivity: AppCompatActivity() {
    val profileActivityVM: ProfileActivityVM by viewModel()

    private var nowPicPos = 0
    private val imgRes = intArrayOf(R.mipmap.ph_tokyo, R.mipmap.ph_taipei)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)
        initObserve()
        callApis()
//        fadeOutAndHideImage(iv_launch_image)


        object : CountDownTimer(2000, 1000) {
            override fun onFinish() {
                Log.e("Peter","CountDownTimer   ${PrefHelper.chatName}         ")
                Log.e("Peter","CountDownTimer   ${PrefHelper.apiHeader}         ")
                Log.e("Peter","CountDownTimer   ${PrefHelper.chatId}         ")
                Log.e("Peter","CountDownTimer   ${PrefHelper.chatLable}         ")
                Log.e("Peter","CountDownTimer   ${PrefHelper.chatToken}         ")
                Log.e("Peter","CountDownTimer   ${PrefHelper.userID}         ")
                Log.e("Peter","CountDownTimer   ${PrefHelper.userPhone}         ")

                if(checkSession()){

                    if (checkNickName()){
                        IntentHelper.gotoEventActivity(this@LaunchActivity)

                    } else {
                        IntentHelper.gotoProfileActivity(this@LaunchActivity)

                    }
                } else {
                    IntentHelper.gotoPersonalActivity(this@LaunchActivity)

                }

                finish()
            }

            override fun onTick(millisUntilFinished: Long) {

            }
        }.start()
    }


    private fun checkSession(): Boolean  {
        return PrefHelper.apiHeader != ""
    }

    private fun checkNickName(): Boolean {
        return PrefHelper.chatName != ""
    }

    private fun fadeOutAndHideImage(imageView: ImageView){
        val fadeOut: Animation = AlphaAnimation(1F, 0F)
        fadeOut.interpolator = AccelerateInterpolator()
        fadeOut.duration = 2000

        fadeOut.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationRepeat(p0: Animation?) {
                Log.e("Peter", "onAnimationRepeat   out")
            }

            override fun onAnimationEnd(p0: Animation?) {
                Log.e("Peter", "onAnimationEnd  out")

                nowPicPos %= 2
                iv_launch_image.setImageResource(imgRes[nowPicPos])
                nowPicPos++

                fadeInAndShowImage(imageView)

            }

            override fun onAnimationStart(p0: Animation?) {
                Log.e("Peter", "onAnimationStart  out")
            }

        })
        imageView.startAnimation(fadeOut)
    }

    private fun fadeInAndShowImage(imageView: ImageView){
        val fadeIn: Animation = AlphaAnimation(0F, 1F)
        fadeIn.interpolator = AccelerateInterpolator()
        fadeIn.duration = 2000

        fadeIn.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationRepeat(p0: Animation?) {
                Log.e("Peter", "onAnimationRepeat  in")
            }

            override fun onAnimationEnd(p0: Animation?) {
                Log.e("Peter", "onAnimationEnd  in")

                fadeOutAndHideImage(imageView)

            }

            override fun onAnimationStart(p0: Animation?) {
                Log.e("Peter", "onAnimationStart  in")
            }

        })
        imageView.startAnimation(fadeIn)

    }


    private fun callApis(){
        profileActivityVM.getInterestList()
        profileActivityVM.getJbList()
    }

    private fun initObserve(){

        profileActivityVM.getInterestListData().observe(this, Observer {
            MyApp.get()!!.interestDataList = it
        })

        profileActivityVM.getJbListData().observe(this, Observer {
            MyApp.get()!!.jobDataList = it
        })


    }


}