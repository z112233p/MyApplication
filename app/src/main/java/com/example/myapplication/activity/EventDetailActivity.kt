package com.example.myapplication.activity

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
import androidx.navigation.Navigation
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.myapplication.BuildConfig
import com.example.myapplication.R
import com.example.myapplication.datamodle.event.detailv2.Author
import com.example.myapplication.dialog.DialogEventDetailOption
import com.example.myapplication.tools.ImgHelper
import com.example.myapplication.tools.IntentHelper
import com.example.myapplication.tools.ProgressDialogController
import com.example.myapplication.tools.Tools
import com.example.myapplication.viewmodle.EventDetailActivityVM
import kotlinx.android.synthetic.main.activity_event_detail.*
import kotlinx.android.synthetic.main.activity_main.toolbar
import org.koin.androidx.viewmodel.ext.android.viewModel


class EventDetailActivity : AppCompatActivity() {

    val eventDetailActVM: EventDetailActivityVM by viewModel()
    var eventLabel = ""
    var eventID = 0
    private lateinit var actionItem: MenuItem
    private lateinit var optionItem: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_detail)
        setSupportActionBar(toolbar)
        title = ""
        getIntentData()
        init()
        initObserve()
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        ProgressDialogController.setContext(this)

    }

    private fun getIntentData(){
        val b = intent.extras
        eventLabel = b?.getString("Label")!!

    }

    private fun init(){
        tv_event_join_btn.setOnClickListener(onClick)
        tv_event_cancel_btn.setOnClickListener(onClick)
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
                dealEventStatus(0.0)


            }
        })

        eventDetailActVM.getCancelJoinEventResponse().observe(this, Observer {
            Log.e("Peter","getCancelJoinEventResponse   $it")

            if(!TextUtils.isEmpty(it)){
                Log.e("Peter","getCancelJoinEventResponse IN  $it")

                Tools.toast(this, "已取消報名")
               dealEventStatus(1.0)
            }
        })

        eventDetailActVM.getErrorMsg().observe(this, Observer {
            Tools.toast(this, it)
        })
    }

    fun dealEventStatus(joinType: Any?){
        Log.e("Peter","dealEventStatus $joinType")

        when(joinType){
            0.0 ,1.0-> {
                Log.e("Peter","dealEventStatus 1")
                ll_event_status.visibility = View.VISIBLE
                tv_event_cancel_btn.visibility = View.VISIBLE
                tv_event_join_btn.visibility = View.GONE
                actionItem.isVisible = false
                optionItem.isVisible = true
            }
            9.0 ->{
                Log.e("Peter","dealEventStatus 9")

                ll_event_status.visibility = View.GONE
                actionItem.isVisible = true
                optionItem.isVisible = true



            }
            else -> {
                Log.e("Peter","dealEventStatus else")

                ll_event_status.visibility = View.VISIBLE
                tv_event_cancel_btn.visibility = View.GONE
                tv_event_join_btn.visibility = View.VISIBLE
                actionItem.isVisible = false
                optionItem.isVisible = true


            }
        }
    }

    fun setOwnerData(author: Author) {
        tv_owner_name.text = author.nickname
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
                        setEventOwnerLayout(gd)
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