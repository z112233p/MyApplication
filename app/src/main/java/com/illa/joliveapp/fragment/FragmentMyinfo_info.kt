package com.illa.joliveapp.fragment

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.illa.joliveapp.BuildConfig
import com.illa.joliveapp.MyApp
import com.illa.joliveapp.R
import com.illa.joliveapp.activity.MyInfoActivity
import com.illa.joliveapp.adapter.Adapter_Events
import com.illa.joliveapp.adapter.CircleViewPager
import com.illa.joliveapp.controller.BannerController
import com.illa.joliveapp.dialog.DialogIGLogin
import com.illa.joliveapp.tools.ImgHelper
import com.illa.joliveapp.tools.IntentHelper
import com.illa.joliveapp.tools.PrefHelper
import com.illa.joliveapp.tools.Tools
import com.illa.joliveapp.viewmodle.ProfileActivityVM
import kotlinx.android.synthetic.main.fragment_my_info_detail.*

import kotlin.collections.ArrayList


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "CAST_NEVER_SUCCEEDS", "DEPRECATION")
class FragmentMyinfo_info : BaseFragment() {

    private val profileActivityVM: ProfileActivityVM by activityViewModels()
    private lateinit var myEventAdapter: Adapter_Events
    private lateinit var bannerController: BannerController
    private lateinit var imageList: ArrayList<String>

    override fun getLayoutId(): Int {
        return R.layout.fragment_my_info_detail

    }

    @SuppressLint("NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.e("Peter","isNavigationViewInit ")

        if(!isNavigationViewInit){
            Log.e("Peter","isNavigationViewInit  ININ ")
            super.onViewCreated(view, savedInstanceState)
            init()
            initObserve()
            isNavigationViewInit = true
        }
    }

    override fun onResume() {
        super.onResume()
        callApis()
        Log.e("peter","FragmentMyinfo_info    onResume")

//        setTitle("Hello.")
    }


    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    private fun init(){
        imageList = ArrayList()

        imageList.add("https://dev.illa.me/images/f67b208659af9a031e9f6c6091e07557.jpg")
        imageList.add("https://dev.illa.me/images/39f2a851b051e1b6a7c5feb9258cc8b4.png")
        imageList.add("https://dev.illa.me/images/f67b208659af9a031e9f6c6091e07557.jpg")
        imageList.add("https://dev.illa.me/images/39f2a851b051e1b6a7c5feb9258cc8b4.png")
//        tv_follow_btn.setOnClickListener(onClick)
        tv_follow_btn
        if((getMContext().get() as MyInfoActivity).userLabel == PrefHelper.chatLable){
            tv_follow_btn.visibility = View.GONE

        } else {
            tv_follow_btn.visibility = View.VISIBLE


        }

        myEventAdapter = Adapter_Events(getMContext().get(), 3, false)
        val layoutManager =  LinearLayoutManager(getMContext().get(), LinearLayoutManager.HORIZONTAL, false)
        rv_my_events.layoutManager = layoutManager
        rv_my_events.adapter = myEventAdapter

        myEventAdapter.setOnItemClickListener(object : Adapter_Events.OnItemClickListener{
            override fun onItemClick(view: View?, position: Int, label: String) {
                getMContext().get()?.let {
                    IntentHelper.gotoEventDetailActivity(it, label, false)
                }
            }
        })
    }

    private fun initUserPhoto(){
        Log.e("Peter","FRAG  CircleViewPager   $imageList")
        bannerController = BannerController(getMContext().get()!!, imageList.size, vp_banner_view_pager, ll_dot_layout)

        val adapter = CircleViewPager(getMContext().get()!! , imageList)
        adapter.setImageList(ArrayList())

        adapter.setCircleViewPagerInterface(object : CircleViewPager.CircleViewPagerInterface{
            override fun itemOnClick(pos: Int) {
                Log.e("peter","itemOnClick")
            }

            override fun itemOnLongClick(pos: Int) {
                Log.e("peter","itemOnLongClick")
            }

        })
        vp_banner_view_pager.adapter = adapter
//        vp_banner_view_pager.setPageTransformer(true, object : )
        vp_banner_view_pager.setController(bannerController)

    }

    override fun onDetach() {
        super.onDetach()
        Log.e("peter","onDetachonDetachonDetach    ")
    }

    override fun onPause() {
        super.onPause()
        if(bannerController != null){
            bannerController.stopSwipe()
            bannerController.stopLooper()
        }

        Log.e("peter","onPause    ")

    }

    private val onItemSelectedListener= object : AdapterView.OnItemSelectedListener{
        override fun onNothingSelected(p0: AdapterView<*>?) {
            Log.e("peter","sp_payment_method   onNothingSelected ")
        }

        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n", "ShowToast")
    private val onClick = View.OnClickListener {
        when (it.id) {
            R.id.tv_follow_btn -> {
                val dialog = getMContext().get()?.let { it1 -> DialogIGLogin(it1) }
                dialog?.show()
            }

        }
    }


    private fun initObserve(){
        profileActivityVM.getUserInfoData().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            val it = it.data
            val constellationTitle = mutableListOf<String>(*MyApp.get()!!.resources.getStringArray(R.array.constellation_cn))

            ImgHelper.loadNormalImg(getMContext().get(),BuildConfig.IMAGE_URL+ it.user.photos!![0].url, iv_my_photo)
            imageList.clear()
            it.user.photos!!.forEach {
                imageList.add(BuildConfig.IMAGE_URL+ it.url)
            }
            Log.e("Peter","FRAG OB CircleViewPager   $imageList")
            initUserPhoto()


//            ImgHelper.loadNormalImgNoCache(getMContext().get(),BuildConfig.CHATROOM_IMAGE_URL+"dating/"+ it.user.label +".jpg", iv_my_photo)
            tv_name.text = it.user.nickname
            val birth = "Taipei City, "+it.user.age+", "+constellationTitle[it.user.constellation_id]

//            val birth = "Taipei City, "+it.user.age+", "+Tools.getConstellation(it.user.birthday.split("-")[1],it.user.birthday.split("-")[2])
            tv_birth.text = birth

            var interests = ""
            it.user.interest_map?.forEach { id ->
                if(id == it.user.interest_map!!.last()){
                    interests += MyApp.get()!!.getInterest(id)

                } else {
                    interests += MyApp.get()!!.getInterest(id)+", "

                }
            }
            tv_my_interest.text = interests
            tv_my_job.text = MyApp.get()!!.getJob(it.user.job_id.toInt())
            if(it.user.gender == 0){
                iv_gender.setImageDrawable(getMContext().get()?.resources?.getDrawable(R.mipmap.ic_gender_woman))
            } else {
                iv_gender.setImageDrawable(getMContext().get()?.resources?.getDrawable(R.mipmap.ic_gender_man))

            }
            tv_about_me_data.text = it.user.about


        })


        profileActivityVM.getMyInfoData().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            ImgHelper.loadNormalImgNoCache(getMContext().get(),BuildConfig.CHATROOM_IMAGE_URL+"dating/"+ it.user.label +".jpg", iv_my_photo)
            imageList.clear()
            it.user.photos!!.forEach {
                imageList.add(BuildConfig.IMAGE_URL+ it.url)
            }
            Log.e("Peter","FRAG OB CircleViewPager   $imageList")
            initUserPhoto()

            tv_name.text = it.user.nickname
            val birth = "Taipei City, "+it.user.age+", "+Tools.getConstellation(it.user.birthday.split("-")[1],it.user.birthday.split("-")[2])
            tv_birth.text = birth

            var interests = ""
            it.user.interest_map?.forEach { id ->
                if(id == it.user.interest_map!!.last()){
                    interests += MyApp.get()!!.getInterest(id)

                } else {
                    interests += MyApp.get()!!.getInterest(id)+", "

                }
            }
            tv_my_interest.text = interests
            tv_my_job.text = MyApp.get()!!.getJob(it.user.job_id.toInt())
            if(it.user.gender == 0){
                iv_gender.setImageDrawable(getMContext().get()?.resources?.getDrawable(R.mipmap.ic_gender_woman))
            } else {
                iv_gender.setImageDrawable(getMContext().get()?.resources?.getDrawable(R.mipmap.ic_gender_man))

            }
            tv_about_me_data.text = it.user.about
        })

        profileActivityVM.getMyEventsData().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            myEventAdapter.setData(it.data.processing)
        })
    }

    private fun callApis(){
        if((getMContext().get() as MyInfoActivity).userLabel == PrefHelper.chatLable){
            profileActivityVM.getMyInfo()
            profileActivityVM.getMyEvents()


        } else {
            profileActivityVM.getUserInfo((getMContext().get() as MyInfoActivity).userLabel)
            profileActivityVM.getUserEvents((getMContext().get() as MyInfoActivity).userLabel)

        }
//        profileActivityVM.getEventsApi(PrefHelper.chatLable, null)
    }

}
