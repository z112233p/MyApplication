package com.example.myapplication.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.ImageView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.tools.PrefHelper
import com.example.myapplication.datamodle.authorization.LoginData
import com.example.myapplication.datamodle.authorization.ResendSMS
import com.example.myapplication.tools.Tools
import com.example.myapplication.viewmodle.MainActivityVM
import kotlinx.android.synthetic.main.fragment_login_main.*
import kotlinx.android.synthetic.main.item_login_button.view.*
import kotlin.properties.Delegates

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FragmentLoginMain : BaseFragment() {

    val mainActVM: MainActivityVM by activityViewModels()
    private var nowPicPos = 0
    private val imgRes = intArrayOf(R.mipmap.ph_login_1, R.mipmap.ph_login_2, R.mipmap.ph_login_3, R.mipmap.ph_login_4)
    private var imgResSize = imgRes.size


    override fun getLayoutId(): Int {
        return R.layout.fragment_login_main
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        setTitle("請輸入手機號碼")
        nowPicPos %= imgResSize
        iv_peter_test.setImageResource(imgRes[nowPicPos])
        nowPicPos++
        nowPicPos %= imgResSize
        iv_peter_test3.setImageResource(imgRes[nowPicPos])
        nowPicPos++
        fadeOutAndHideImage(iv_peter_test)
//        peter_test2.alpha = 0.2F

    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun checkSession(): Boolean  {
        return PrefHelper.apiHeader != ""
    }

    private fun init(){
        hideToolBar()
        hideTitle()
        tv_login.setOnClickListener(onClick)
        btn_login_phone.run {
            setOnClickListener(onClick)
            tv_btn_text.text = "手機號碼註冊"
            tv_btn_text.setTextColor(getMContext().get()?.resources?.getColor(R.color.colorBlack)!!)

            iv_btn_img.setImageDrawable(getMContext().get()?.resources?.getDrawable(R.mipmap.ic_phone))
        }
        btn_login_facebook.run {
            setOnClickListener(onClick)
            tv_btn_text.text = "Facebook登入"
            tv_btn_text.setTextColor(getMContext().get()?.resources?.getColor(R.color.colorWhite)!!)
            iv_btn_img.setImageDrawable(getMContext().get()?.resources?.getDrawable(R.mipmap.ic_facebook_))
            fl_button_layout.background = getMContext().get()?.resources?.getDrawable(R.drawable.bg_login_button_blue)
        }
        btn_login_apple.run {
            setOnClickListener(onClick)
            tv_btn_text.setTextColor(getMContext().get()?.resources?.getColor(R.color.colorBlack)!!)

            tv_btn_text.text = "Apple登入"
            iv_btn_img.setImageDrawable(getMContext().get()?.resources?.getDrawable(R.mipmap.ic_apple))
        }

    }

    @SuppressLint("WrongConstant", "ShowToast")
    private var onClick :View.OnClickListener ?= View.OnClickListener {
        Tools.hideSoftKeyboard(getMContext().get() as Activity)
        when (it.id){
            R.id.btn_login_phone,R.id.tv_login -> {
                findNavController().navigate(R.id.action_FragmentLoginMain_to_FragmentInputPhone)
            }

        }
    }


    private fun fadeOutAndHideImage(imageView: ImageView){
        val fadeOut: Animation = AlphaAnimation(1F, 0F)
        fadeOut.interpolator = AccelerateInterpolator()
        fadeOut.duration = 3000

        fadeOut.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationRepeat(p0: Animation?) {
                Log.e("Peter", "onAnimationRepeat   out")
            }

            override fun onAnimationEnd(p0: Animation?) {


                Log.e("Peter", "onAnimationEnd  out")
                imageView.alpha = 0F


                Handler().postDelayed({
                    nowPicPos %= imgResSize
                    if(imageView != null){
                        imageView.setImageResource(imgRes[nowPicPos])
                    }
                    nowPicPos++
                    Log.e("Peter", "onAnimationEnd  out2")

                    fadeInAndShowImage(imageView)
                }, 1000)

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
        fadeIn.duration = 3000
        imageView.alpha = 1F

        fadeIn.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationRepeat(p0: Animation?) {
                Log.e("Peter", "onAnimationRepeat  in")
            }

            override fun onAnimationEnd(p0: Animation?) {


                Handler().postDelayed({
                    Log.e("Peter", "onAnimationEnd  in")
                    nowPicPos %= imgResSize
                    if(iv_peter_test3 != null){
                        iv_peter_test3.setImageResource(imgRes[nowPicPos])
                    }
                    nowPicPos++
                    fadeOutAndHideImage(imageView)
                    Log.e("Peter", "onAnimationEnd  in2")

                }, 1000)

            }

            override fun onAnimationStart(p0: Animation?) {

                Log.e("Peter", "onAnimationStart  in")
            }

        })
        imageView.startAnimation(fadeIn)

    }

}