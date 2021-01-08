package com.illa.joliveapp.activity

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.illa.joliveapp.BuildConfig
import com.illa.joliveapp.R
import com.illa.joliveapp.datamodle.event.detailv2.Author
import com.illa.joliveapp.tools.*
import com.illa.joliveapp.viewmodle.EventDetailActivityVM
import kotlinx.android.synthetic.main.activity_event_detail.*
import kotlinx.android.synthetic.main.activity_main.toolbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*


class EventDetailActivity : AppCompatActivity() {

    val eventDetailActVM: EventDetailActivityVM by viewModel()
    var eventLabel = ""
    var eventID = 0
    var userLabel = ""
    var gotoReview = false

    private var dateSdf = SimpleDateFormat("yyyy-MM-dd")
    private var newDate: Long = -1
    private lateinit var actionItem: MenuItem
    private lateinit var optionItem: MenuItem


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_detail)
        setSupportActionBar(toolbar)
        ProgressDialogController.setContext(this)
        title = ""
        getIntentData()
        init()
        initObserve()
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
//        checkIntentData()
    }

    override fun onResume() {
        super.onResume()
        ProgressDialogController.setContext(this)

    }

    private fun getIntentData(){
        Log.e("Peter","DEEPLINK  IN ${intent.dataString}")

        if(intent.dataString == null){
            val b = intent.extras
            eventLabel = b?.getString("Label")!!
            gotoReview = b.getBoolean("gotoReview")
        } else {
            val uri = intent.dataString
            Log.e("Peter","DEEPLINK  ${uri?.split("/")}")
            val splitArray = uri?.split("/")
            eventLabel = splitArray?.get(splitArray.size -1) ?: ""
        }

    }


    private fun init(){
        tv_event_join_btn.setOnClickListener(onClick)
        tv_event_cancel_btn.setOnClickListener(onClick)
        ll_event_owner.setOnClickListener(onClick)
        iv_share.setOnClickListener(onClick)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        actionItem = menu!!.findItem(R.id.action_edit)
        optionItem = menu.findItem(R.id.action_option)

        actionItem.isVisible = false
        optionItem.isVisible = false
        return true
    }

    private val onClick = View.OnClickListener {
        when(it.id){
            R.id.tv_event_join_btn -> eventDetailActVM.joinEvent(eventID.toString())
            R.id.tv_event_cancel_btn -> eventDetailActVM.cancelJoinEvent(eventID.toString())
            R.id.ll_event_owner -> {
                IntentHelper.gotoMyInfoActivity(this,userLabel)
                this.finish()
            }
            R.id.iv_share -> Tools.shareLink(this, Tools.makeShareLink(eventLabel))
        }
    }

    private fun initObserve(){
        eventDetailActVM.getProgressStatus().observe(this, Observer {
            if(it){
                ProgressDialogController.dismissProgress()
            } else {
                ProgressDialogController.showProgress()
            }
        })

        eventDetailActVM.getJoinEventResponse().observe(this, Observer {
            Log.e("Peter","getJoinEventResponse   $it")
            if(!TextUtils.isEmpty(it)){
                Log.e("Peter","getJoinEventResponse IN  $it")

                Tools.toast(this, "報名成功")
                dealEventStatus(0.0, newDate.toLong())

            }
        })

        eventDetailActVM.getCancelJoinEventResponse().observe(this, Observer {
            Log.e("Peter","getCancelJoinEventResponse   $it")

            if(!TextUtils.isEmpty(it)){
                Log.e("Peter","getCancelJoinEventResponse IN  $it")

                Tools.toast(this, "已取消報名")
               dealEventStatus(1.0, newDate.toLong())
            }
        })

        eventDetailActVM.getErrorMsg().observe(this, Observer {
            Tools.toast(this, it)
        })
    }

    fun dealEventStatus(joinType: Any?, newDate: Long){
        Log.e("Peter","dealEventStatus $joinType")

        this.newDate = newDate


        val diffDay = newDate / (1000 * 60 * 60 * 24);
        val diffHour =(newDate - diffDay * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60)
        val diffMinute = (newDate - diffDay * (1000 * 60 * 60 * 24) - diffHour * (1000 * 60 * 60)) / (1000 * 60)

        if(newDate >= 0){
            tv_diif_time.text = "${diffDay}天 ${diffHour}小時 ${diffMinute}分"
            tv_diif_time.visibility = View.VISIBLE
        } else {
            tv_diif_time.visibility = View.GONE

        }

        when(joinType){
            0.0 ,1.0-> {
                Log.e("Peter","dealEventStatus 1")
                if(newDate < 0){
                    ll_event_status.visibility = View.VISIBLE
                    tv_event_status.text = getString(R.string.sign_up_deadline)
                    tv_event_status.visibility = View.VISIBLE
                    tv_event_cancel_btn.visibility = View.GONE
                    tv_event_join_btn.visibility = View.GONE

                } else {
                    ll_event_status.visibility = View.VISIBLE
                    tv_event_cancel_btn.visibility = View.VISIBLE
                    tv_event_join_btn.visibility = View.GONE
                }

                actionItem.isVisible = false
                optionItem.isVisible = true
            }
            9.0 ->{
                Log.e("Peter","dealEventStatus 9")

                ll_event_status.visibility = View.VISIBLE
                tv_diif_time.visibility = View.GONE
                tv_event_status.visibility = View.VISIBLE

                if(newDate < 0){
                    tv_event_status.text = getString(R.string.event_incoming)
                } else {
                    tv_event_status.text = getString(R.string.invite_friend)
                }
                tv_event_cancel_btn.visibility = View.GONE
                tv_event_join_btn.visibility = View.GONE
                actionItem.isVisible = true
                optionItem.isVisible = true

            }
            else -> {
                Log.e("Peter","dealEventStatus else")

                if(newDate < 0){
                    ll_event_status.visibility = View.VISIBLE
                    tv_event_status.text = getString(R.string.sign_up_deadline)
                    tv_event_status.visibility = View.VISIBLE

                    tv_event_cancel_btn.visibility = View.GONE
                    tv_event_join_btn.visibility = View.GONE

                } else {
                    ll_event_status.visibility = View.VISIBLE
                    tv_event_cancel_btn.visibility = View.GONE
                    tv_event_join_btn.visibility = View.VISIBLE
                }

                actionItem.isVisible = false
                optionItem.isVisible = true


            }
        }
    }

    fun setOwnerData(author: Author) {
        userLabel = author.label
        tv_owner_name.text = author.nickname
        if(author.birthday != null){
            val ff: Date = dateSdf.parse(author.birthday)
            tv_age.text = DateGetAge.getAge(ff).toString()+"歲"
            Log.e("Peter","setOwnerData    $ff  ${author.birthday}")
        }



//        ImgHelper.loadNormalImg(this, BuildConfig.CHATROOM_IMAGE_URL+"dating/"+ author.label +".jpg", iv_owner)
        ImgHelper.loadNormalImgNoCache(this, BuildConfig.CHATROOM_IMAGE_URL+"dating/"+ author.label +".jpg", iv_owner)

        Glide.with(this)
            .asBitmap()
            .load(BuildConfig.CHATROOM_IMAGE_URL+"dating/"+ author.label +".jpg")
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(object : CustomTarget<Bitmap?>() {
                override fun onLoadCleared(placeholder: Drawable?) {
                    Log.e("Peter","onLoadCleared  ")
                }

                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap?>?) {
                    Palette.from(resource).generate {
                        val vibrantSwatch = it?.vibrantSwatch
                        val mutedSwatch = it?.mutedSwatch
                        var swatch: Palette.Swatch? = null
                        var mainColor = 0
                        var secondColor = 0

                        if (mutedSwatch == null ) {
                            if(it?.darkVibrantSwatch == null){
                                mainColor = vibrantSwatch!!.rgb
                                secondColor = (it.lightVibrantSwatch)!!.rgb
                            } else {
                                mainColor = (it.darkVibrantSwatch)!!.rgb
                                secondColor = vibrantSwatch!!.rgb
                            }

                        } else if(vibrantSwatch == null){
                            if(it.darkMutedSwatch == null){
                                mainColor = mutedSwatch.rgb
                                secondColor = (it.lightMutedSwatch)!!.rgb
                            } else {
                                mainColor = (it.darkMutedSwatch)!!.rgb
                                secondColor = mutedSwatch.rgb
                            }


                        } else {
                            mainColor = vibrantSwatch.rgb
                            secondColor = mutedSwatch.rgb
                        }

                        val gd = GradientDrawable()

                        gd.colors = intArrayOf(secondColor, mainColor)
                        gd.useLevel = false
                        gd.gradientType = GradientDrawable.LINEAR_GRADIENT
//                        setEventOwnerLayout(gd)
                    }
                }
            })
    }

    fun setEventOwnerLayout(drawable: Drawable){
        ll_event_owner.background = drawable
    }

    fun showEventOwnerLayout(){
        ll_event_owner.visibility = View.VISIBLE
    }

    fun hideEventOwnerLayout(){
        ll_event_owner.visibility = View.GONE
    }

    fun hideEventStatusLayout(){
        ll_event_status.visibility = View.GONE
    }

    fun hideMenu(){
        optionItem.isVisible = false
        actionItem.isVisible = false
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_event_detail, menu)
        return true
    }

    @SuppressLint("Range")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        messagesList.scrollToPosition((messagesList.getChildAt (0).layoutParams as RecyclerView.LayoutParams).viewAdapterPosition)
        return when (item.itemId) {
            R.id.action_option -> {
                Log.e("Peter","onOptionsItemSelected ACT")

//                 val dialog = DialogEventDetailOption(this)
//                dialog.setSeeDetailOnclick(View.OnClickListener {
//                    val navController= Navigation.findNavController(this, R.id.nav_event_detail)
//
//                    // val bundle = bundleOf("eventID" to eventID)
//                    //                findNavController().navigate(R.id.action_FragmentEventDetail_to_FragmentEventReview, bundle)
//
//                })
//                dialog.show()

                false
            }
            R.id.action_edit -> {
                IntentHelper.gotoCreateEventActivity(this, eventLabel)
                false
            }
            else -> false
        }
    }
}