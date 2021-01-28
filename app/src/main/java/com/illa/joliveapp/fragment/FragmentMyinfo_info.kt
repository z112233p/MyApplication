package com.illa.joliveapp.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.illa.joliveapp.BuildConfig
import com.illa.joliveapp.MyApp
import com.illa.joliveapp.R
import com.illa.joliveapp.activity.MyInfoActivity
import com.illa.joliveapp.adapter.Adapter_Events
import com.illa.joliveapp.adapter.Adapter_Instagram_Photo
import com.illa.joliveapp.adapter.CircleViewPager
import com.illa.joliveapp.controller.BannerController
import com.illa.joliveapp.datamodle.friend_status.FriendAgree
import com.illa.joliveapp.datamodle.friend_status.FriendCancel
import com.illa.joliveapp.datamodle.friend_status.FriendRefuse
import com.illa.joliveapp.datamodle.friend_status.FriendRequest
import com.illa.joliveapp.datamodle.instagram.IgDataBody
import com.illa.joliveapp.datamodle.profile.interest.interest
import com.illa.joliveapp.dialog.DialogIGLogin
import com.illa.joliveapp.tools.ImgHelper
import com.illa.joliveapp.tools.IntentHelper
import com.illa.joliveapp.tools.PrefHelper
import com.illa.joliveapp.tools.Tools
import com.illa.joliveapp.viewmodle.ProfileActivityVM
import kotlinx.android.synthetic.main.fragment_my_info_detail.*


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "CAST_NEVER_SUCCEEDS", "DEPRECATION")
class FragmentMyinfo_info : BaseFragment() {

    private val profileActivityVM: ProfileActivityVM by activityViewModels()

    private lateinit var myEventAdapter: Adapter_Events
    private lateinit var imageList: ArrayList<String>
    private lateinit var adapter: Adapter_Instagram_Photo
    private lateinit var act: MyInfoActivity

    private var bannerController: BannerController ?= null
    private var interestMap: ArrayList<Int> = ArrayList<Int>()
    private var interestDataList: interest ?= null
    private var friendStatus = 0
    private var chatRoomId = ""

    override fun getLayoutId(): Int {
        return R.layout.fragment_my_info_detail

    }

    @SuppressLint("NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.e("Peter", "FragmentMyinfo_info  isNavigationViewInit ")

        if(!isNavigationViewInit){
            Log.e("Peter", "FragmentMyinfo_info  isNavigationViewInit  ININ ")
            super.onViewCreated(view, savedInstanceState)
            init()
            initObserve()
            isNavigationViewInit = true
        }
    }

    override fun onResume() {
        super.onResume()
        if(bannerController != null){
            bannerController!!.stopSwipe()
            bannerController!!.stopLooper()
        }
        act = getMContext().get() as MyInfoActivity
        callApis()
        Log.e("peter", "FragmentMyinfo_info    onResume")

//        setTitle("Hello.")
    }


    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    private fun init(){
        imageList = ArrayList()

//        imageList.add("https://dev.illa.me/images/f67b208659af9a031e9f6c6091e07557.jpg")
//        imageList.add("https://dev.illa.me/images/39f2a851b051e1b6a7c5feb9258cc8b4.png")
//        imageList.add("https://dev.illa.me/images/f67b208659af9a031e9f6c6091e07557.jpg")
//        imageList.add("https://dev.illa.me/images/39f2a851b051e1b6a7c5feb9258cc8b4.png")
//        tv_follow_btn.setOnClickListener(onClick)
        tv_follow_btn

        myEventAdapter = Adapter_Events(getMContext().get(), 3, false)
        val layoutManager =  LinearLayoutManager(
            getMContext().get(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        rv_my_events.layoutManager = layoutManager
        rv_my_events.adapter = myEventAdapter

        myEventAdapter.setOnItemClickListener(object : Adapter_Events.OnItemClickListener {
            override fun onItemClick(view: View?, position: Int, label: String) {
                getMContext().get()?.let {
                    IntentHelper.gotoEventDetailActivity(it, label, false)
                }
            }

            override fun onAvatarClick(label: String) {
                getMContext().get()?.let {
                    IntentHelper.gotoMyInfoActivity(it, label)
                }
                Log.e("Peter", "onAvatarClick")
            }
        })

        adapter = getMContext().get()?.let { Adapter_Instagram_Photo(it) }!!
        val vfr = GridLayoutManager(getMContext().get(), 3)
        rv_instagram.layoutManager = vfr
        rv_instagram.adapter = adapter

        ll_follow.setOnClickListener(onClick)
        ll_fan.setOnClickListener(onClick)
        tv_follow_btn.setOnClickListener(onClick)
        ll_instagram.setOnClickListener(onClick)
        tv_instagram_disconnect.setOnClickListener(onClick)
        ll_events_count.setOnClickListener(onClick)
        tv_friend_status.setOnClickListener(onClick)


        if((getMContext().get() as MyInfoActivity).userLabel == PrefHelper.chatLable){
            tv_follow_btn.visibility = View.GONE
            tv_friend_status.visibility = View.GONE

        } else {
            tv_follow_btn.visibility = View.VISIBLE
            tv_friend_status.visibility = View.VISIBLE
            ll_follow.isClickable = false
            ll_fan.isClickable = false

        }
    }

    private fun initUserPhoto(){
        if(bannerController != null){
            bannerController!!.stopSwipe()
            bannerController!!.stopLooper()
        }
        Log.e("Peter", "FRAG  CircleViewPager   $imageList")
        bannerController = BannerController(
            getMContext().get()!!,
            imageList.size,
            vp_banner_view_pager,
            ll_dot_layout
        )

        val adapter = CircleViewPager(getMContext().get()!!, imageList)
        adapter.setImageList(ArrayList())

        adapter.setCircleViewPagerInterface(object : CircleViewPager.CircleViewPagerInterface {
            override fun itemOnClick(pos: Int) {
                Log.e("peter", "itemOnClick")
            }

            override fun itemOnLongClick(pos: Int) {
                Log.e("peter", "itemOnLongClick")
            }

        })
        vp_banner_view_pager.adapter = adapter
//        vp_banner_view_pager.setPageTransformer(true, object : )
        vp_banner_view_pager.setController(bannerController)

    }

    override fun onDetach() {
        super.onDetach()
        Log.e("peter", "onDetachonDetachonDetach    ")
    }

    override fun onPause() {
        super.onPause()

        Log.e("peter", "onPause    ")

    }

    private val onItemSelectedListener= object : AdapterView.OnItemSelectedListener{
        override fun onNothingSelected(p0: AdapterView<*>?) {
            Log.e("peter", "sp_payment_method   onNothingSelected ")
        }

        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n", "ShowToast")
    private val onClick = View.OnClickListener {
        when (it.id) {
            R.id.tv_friend_status -> {
                when (friendStatus) {
                    0 -> {
                        val container = LinearLayout(getMContext().get())
                        container.orientation = LinearLayout.VERTICAL
                        val inputNumber: EditText = EditText(getMContext().get())
//                        getMContext().get()?.resources?.let { it1 ->
//                            inputNumber.setBackgroundColor(
//                                it1.getColor(R.color.gray)
//                            )
//                        }
                        inputNumber.setTextColor(getMContext().get()?.resources?.getColor(R.color.colorWhite)!!)
                        inputNumber.inputType = InputType.TYPE_CLASS_NUMBER or
                                InputType.TYPE_CLASS_PHONE
                        val lp = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        lp.setMargins(50, 100, 50, 100)
                        container.layoutParams = lp
                        container.addView(inputNumber)
                        container.setPadding(80,0,80,0)
                        val builder: AlertDialog.Builder = AlertDialog.Builder(
                            getMContext().get(),
                            R.style.DarkDialog
                        )
                        builder.setTitle("花費點數和邀請對方聊天")
                        builder.setMessage("最低50代幣")
                        builder.setView(container)
                        builder.setPositiveButton("確定") { p0, p1 ->

                            profileActivityVM.friendRequest(
                                FriendRequest(
                                    "1",
                                    inputNumber.text.toString(), act.userLabel
                                )
                            )
                        }
                        builder.setNegativeButton("取消") { p0, p1 ->
                            Log.e("Peter", "dialog cancel")
                        }
                        val dialog = builder.create()
                        dialog.show()

                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getMContext().get()?.resources?.getColor(R.color.colorAccent)!!)
                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getMContext().get()?.resources?.getColor(R.color.colorAccent)!!)
                    }
                    1 -> profileActivityVM.friendCancel(FriendCancel(act.userLabel))
                    2 -> {
                        val builder: AlertDialog.Builder = AlertDialog.Builder(
                            getMContext().get(),
                            R.style.DarkDialog
                        )
                        builder.setTitle("回應對方")
                        builder.setPositiveButton("同意") { p0, p1 ->
                            profileActivityVM.friendAgree(FriendAgree(act.userLabel))
                        }
                        builder.setNegativeButton("拒絕") { p0, p1 ->
                            profileActivityVM.friendRefuse(FriendRefuse(act.userLabel))
                            Log.e("Peter", "dialog cancel")
                        }
                        val dialog = builder.create()
                        dialog.show()
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getMContext().get()?.resources?.getColor(R.color.colorAccent)!!)
                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getMContext().get()?.resources?.getColor(R.color.colorAccent)!!)
                    }
                    3 -> getMContext().get()?.let { it1 -> IntentHelper.gotoChatRoom(it1,chatRoomId,"") }
                }
            }
            R.id.ll_events_count -> act.setCurrentPage(1)
            R.id.tv_instagram_disconnect -> profileActivityVM.igDisconnect()

            R.id.ll_instagram -> {
                val dialog = getMContext().get()?.let { it1 ->
                    DialogIGLogin(
                        it1,
                        R.style.FullScreenDialogStyle
                    )
                }
                dialog?.setCallBack(object : DialogIGLogin.onUrlSuccess {
                    override fun codeResponse(code: String?) {
                        Log.e("Peter", "DialogIGLogin  callback code  $code")
                        profileActivityVM.setIgToken(IgDataBody(code.toString()))
                    }

                })
                dialog?.show()
            }
            R.id.ll_fan -> getMContext().get()?.let { it1 ->
                IntentHelper.gotoFollowsActivity(
                    it1,
                    1
                )
            }
            R.id.ll_follow -> getMContext().get()?.let { it1 ->
                IntentHelper.gotoFollowsActivity(
                    it1,
                    0
                )
            }
            R.id.tv_follow_btn -> {

                if (tv_follow_btn.text == "已追蹤") {
                    profileActivityVM.postUnFollow((getMContext().get() as MyInfoActivity).userLabel)

                } else {
                    profileActivityVM.postFollow((getMContext().get() as MyInfoActivity).userLabel)

                }
            }
        }
    }

    private fun dealInterest(){
        var interests = ""
        interestMap.forEach { id ->
            if(id == interestMap.last()){
                interests += MyApp.get()!!.getInterest(id)

            } else {
                interests += MyApp.get()!!.getInterest(id)+", "

            }
        }
        tv_my_interest.text = interests
    }

    private fun checkInterest(){
        if(interestDataList != null && interestMap.size > 0){
            dealInterest()
        }
    }

    private fun initObserve(){
        profileActivityVM.getUserInfoData().observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer {
                val it = it.data
                friendStatus = it.user.friend_status
                chatRoomId = it.user.chat_rid
                Log.e("peter", "getUserInfoData  USER IGCO   ${it.user.instargam_connection}")
                if (it.user.instargam_images!!.isNotEmpty()) {
                    tv_divider_line_four.visibility = View.VISIBLE
                    tv_instagram.visibility = View.VISIBLE
                    rv_instagram.visibility = View.VISIBLE

                } else {
                    tv_divider_line_four.visibility = View.GONE
                    tv_instagram.visibility = View.GONE
                    rv_instagram.visibility = View.GONE

                }
                ll_instagram.visibility = View.GONE
                tv_instagram_disconnect.visibility = View.GONE

                val constellationTitle =
                    mutableListOf<String>(*MyApp.get()!!.resources.getStringArray(R.array.constellation_cn))

                adapter.setData(it.user.instargam_images)

                if (it.user.is_follow) {
                    tv_follow_btn.text = "已追蹤"
                    tv_follow_btn.background =
                        getMContext().get()!!.resources.getDrawable(R.drawable.bg_event_type_btn)
                } else {
                    tv_follow_btn.text = "追蹤"
                    tv_follow_btn.background =
                        getMContext().get()!!.resources.getDrawable(R.drawable.bg_clickable_btn)
                }
                when (it.user.friend_status) {
                    0 -> tv_friend_status.text = "認識對方"
                    1 -> tv_friend_status.text = "收回"
                    2 -> tv_friend_status.text = "回應對方"
                    3 -> tv_friend_status.text = "發訊息"
                }

                if (it.user.photos?.isNotEmpty()!!) {
                    ImgHelper.loadNormalImg(
                        getMContext().get(),
                        BuildConfig.IMAGE_URL + it.user.photos!![0].url,
                        iv_my_photo
                    )
                    imageList.clear()
                    it.user.photos!!.forEach {
                        imageList.add(BuildConfig.IMAGE_URL + it.url)
                    }
                }

                Log.e("Peter", "FRAG OB CircleViewPager   $imageList")
                initUserPhoto()


//            ImgHelper.loadNormalImgNoCache(getMContext().get(),BuildConfig.CHATROOM_IMAGE_URL+"dating/"+ it.user.label +".jpg", iv_my_photo)
                tv_name.text = it.user.nickname
                if (it.user.constellation_id == 0) {
                    it.user.constellation_id = 1
                }
                val birth =
                    "Taipei City, " + it.user.age + ", " + constellationTitle[it.user.constellation_id - 1]

//            val birth = "Taipei City, "+it.user.age+", "+Tools.getConstellation(it.user.birthday.split("-")[1],it.user.birthday.split("-")[2])
                tv_birth.text = birth

                var interests = ""
                interestMap.clear()
                it.user.interest_map?.forEach { id ->
                    interestMap.add(id)

                    if (id == it.user.interest_map!!.last()) {
                        interests += MyApp.get()!!.getInterest(id)

                    } else {
                        interests += MyApp.get()!!.getInterest(id) + ", "

                    }
                }
                tv_my_interest.text = interests
                checkInterest()

                if (it.user.job_id != null) {
                    if (TextUtils.isEmpty(MyApp.get()!!.getJob(it.user.job_id.toInt()))) {
                        profileActivityVM.getJbList()
                    } else {
                        tv_my_job.text = MyApp.get()!!.getJob(it.user.job_id.toInt())

                    }
                }

                if (it.user.gender == 0) {
                    iv_gender.setImageDrawable(getMContext().get()?.resources?.getDrawable(R.mipmap.ic_gender_woman))
                } else {
                    iv_gender.setImageDrawable(getMContext().get()?.resources?.getDrawable(R.mipmap.ic_gender_man))

                }
                iv_gender.visibility = View.VISIBLE
                tv_about_me_data.text = it.user.about
                tv_follow.text = it.user.follows_count.toString()
                tv_fans_count.text = it.user.fans_count.toString()
            })


        profileActivityVM.getMyInfoData().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            adapter.setData(it.user.instargam_images)

            if (it.user.instargam_connection) {

                ll_instagram.visibility = View.GONE
                tv_instagram_disconnect.visibility = View.VISIBLE

            } else {
                ll_instagram.visibility = View.VISIBLE
                tv_instagram_disconnect.visibility = View.GONE

            }
            /** 暫時先藏 **/
//            ll_instagram.visibility = View.GONE
//            tv_instagram_disconnect.visibility = View.GONE

            ImgHelper.loadNormalImgNoCache(
                getMContext().get(),
                BuildConfig.CHATROOM_IMAGE_URL + "dating/" + it.user.label + ".jpg",
                iv_my_photo
            )
            imageList.clear()
            it.user.photos!!.forEach {
                imageList.add(BuildConfig.IMAGE_URL + it.url)
            }
            Log.e("Peter", "FRAG OB CircleViewPager   $imageList")
            initUserPhoto()

            tv_name.text = it.user.nickname
            val birth = "Taipei City, " + it.user.age + ", " + Tools.getConstellation(
                it.user.birthday.split(
                    "-"
                )[1], it.user.birthday.split("-")[2]
            )
            tv_birth.text = birth

            var interests = ""
            interestMap.clear()
            it.user.interest_map?.forEach { id ->
                interestMap.add(id)

                if (id == it.user.interest_map!!.last()) {
                    interests += MyApp.get()!!.getInterest(id)

                } else {
                    interests += MyApp.get()!!.getInterest(id) + ", "

                }
            }
            tv_my_interest.text = interests
            checkInterest()


            tv_my_job.text = MyApp.get()!!.getJob(it.user.job_id.toInt())
            if (it.user.gender == 0) {
                iv_gender.setImageDrawable(getMContext().get()?.resources?.getDrawable(R.mipmap.ic_gender_woman))
            } else {
                iv_gender.setImageDrawable(getMContext().get()?.resources?.getDrawable(R.mipmap.ic_gender_man))

            }
            tv_about_me_data.text = it.user.about

            tv_follow.text = it.user.follows_count.toString()
            tv_fans_count.text = it.user.fans_count.toString()
        })

        profileActivityVM.getMyEventsData().observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer {
                myEventAdapter.setData(it.data.processing.filter { it.author.nickname == PrefHelper.chatName })
                tv_event_count.text = (it.data.history.size + it.data.processing.size).toString()
                if (it.data.processing.isEmpty()) {
                    tv_my_event.visibility = View.GONE
                    tv_divider_line_three.visibility = View.GONE
                }
            })

        profileActivityVM.getFollowResponse().observe(viewLifecycleOwner, Observer {
            callApis()
        })

        profileActivityVM.getUnFollowResponse().observe(viewLifecycleOwner, Observer {
            callApis()
        })

        profileActivityVM.getInterestListData().observe(viewLifecycleOwner, Observer {
            MyApp.get()!!.setInterest(it)
            interestDataList = it

        })

        profileActivityVM.getJbListData().observe(viewLifecycleOwner, Observer {
            MyApp.get()!!.setJob(it)
        })

        profileActivityVM.getIgTokenResponse().observe(viewLifecycleOwner, Observer {
            callApis()
            Log.e("peter", "getIgTokenResponse INACT")
        })

        profileActivityVM.getIgDisconnectResponse().observe(viewLifecycleOwner, Observer {
            callApis()

        })

        profileActivityVM.getFriendResponse().observe(viewLifecycleOwner, Observer {
            callApis()

        })

    }

    private fun callApis(){
        if((getMContext().get() as MyInfoActivity).userLabel == PrefHelper.chatLable){
            profileActivityVM.getMyInfo()
            profileActivityVM.getMyEvents()
//            profileActivityVM.getUserEvents((getMContext().get() as MyInfoActivity).userLabel)


        } else {
            profileActivityVM.getUserInfo((getMContext().get() as MyInfoActivity).userLabel)
            profileActivityVM.getUserEvents((getMContext().get() as MyInfoActivity).userLabel)

        }
//        profileActivityVM.getEventsApi(PrefHelper.chatLable, null)
    }

}
