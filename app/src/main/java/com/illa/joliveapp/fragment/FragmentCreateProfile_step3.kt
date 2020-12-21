package com.illa.joliveapp.fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.illa.joliveapp.BuildConfig
import com.illa.joliveapp.R
import com.illa.joliveapp.activity.ProfileActivity
import com.illa.joliveapp.dialog.DialogChooseGender
import com.illa.joliveapp.tools.ImgHelper
import com.illa.joliveapp.tools.IntentHelper
import com.illa.joliveapp.tools.PrefHelper
import com.illa.joliveapp.tools.Tools
import com.illa.joliveapp.viewmodle.ProfileActivityVM
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.fragment_creat_profile_step_3.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.forEach
import kotlin.collections.set
import kotlin.math.abs


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "CAST_NEVER_SUCCEEDS")
class FragmentCreateProfile_step3 : BaseFragment() {

    private val profileActivityVM: ProfileActivityVM by activityViewModels()
    private val myCalendar: Calendar = Calendar.getInstance()
    private var selectedGender = 0

    private lateinit var act: ProfileActivity
    private lateinit var date: DatePickerDialog.OnDateSetListener
    private lateinit var genderList: ArrayList<String>
    private lateinit var chooseGender: DialogChooseGender

    private var hasName: Boolean = false
    private var hasPhoto: Boolean = false
    private var hasBirth: Boolean = false
    private var hasGender: Boolean = false



    override fun getLayoutId(): Int {
        return R.layout.fragment_creat_profile_step_3

    }

    @SuppressLint("NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        Log.e("Peter","isNavigationViewInit ")

        if(!isNavigationViewInit){
            Log.e("Peter","isNavigationViewInit  ININ ")
            super.onViewCreated(view, savedInstanceState)
            init()
            initObserve()
            callApis()
            isNavigationViewInit = true
        }
    }

    override fun onResume() {
        super.onResume()
        setTitle("即將完成")
        act.setSubTitle("請上傳一張照片，建立基本資訊")
        (getMContext().get() as ProfileActivity).setStepThree()

    }


    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    private fun init(){
        act = getMContext().get() as ProfileActivity

        genderList = ArrayList()
        genderList.add("Female")
        genderList.add("male")
        val listAdapter = ArrayAdapter<String>(getMContext().get()!!, android.R.layout.simple_spinner_item, genderList)
        sp_gender.adapter = listAdapter
        sp_gender.onItemSelectedListener = onItemSelectedListener

        date = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            myCalendar[Calendar.YEAR] = year
            myCalendar[Calendar.MONTH] = monthOfYear
            myCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth
            updateLabel()
            hasBirth = true
            checkNextStep()
        }
        ed_user_birth.setOnClickListener(onClick)
        ed_user_gender.setOnClickListener(onClick)
        iv_profile_photo.setOnClickListener(onClick)
        tv_next_step.setOnClickListener(onClick)
        tv_next_step.isClickable = false

        ed_user_name.addTextChangedListener {
            if(it!!.isNotEmpty()){
                hasName = true
                checkNextStep()
            }
        }

    }

    private val onItemSelectedListener= object : AdapterView.OnItemSelectedListener{
        override fun onNothingSelected(p0: AdapterView<*>?) {
            Log.e("peter","sp_payment_method   onNothingSelected ")
        }

        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            ed_user_gender.setText(genderList[p2])
            selectedGender = p2
            hasGender = true
            checkNextStep()
        }
    }


    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n", "ShowToast")
    private val onClick = View.OnClickListener {
        when (it.id) {
            R.id.ed_user_gender -> {
//                sp_gender.performClick()
//                val chooseGender = getMContext().get()?.let { it1 -> DialogChooseGender(it1) }
                val chooseGender = getMContext().get()?.let { it1 -> DialogChooseGender(it1) }
                chooseGender?.setOnItemClick(object : DialogChooseGender.OnItemClickListener{
                    override fun onFemaleClick(gender: String) {
                        ed_user_gender.setText(gender)
                        selectedGender = 0
                        hasGender = true
                        checkNextStep()
                    }

                    override fun onMaleClick(gender: String) {
                        ed_user_gender.setText(gender)
                        selectedGender = 1
                        hasGender = true
                        checkNextStep()
                    }

                })
                chooseGender?.show()
            }
            R.id.iv_profile_photo -> {
//                v2.setColor("#1778f2")
//                v1.setColor("#ec663c")
                getMContext().get()?.let { ctx -> CropImage.activity().start(ctx,this@FragmentCreateProfile_step3) }

            }
            R.id.ed_user_birth -> {
                val cal = Calendar.getInstance()

                val year = cal[Calendar.YEAR]
                val month = cal[Calendar.MONTH]
                val day = cal[Calendar.DAY_OF_MONTH]
                val picker = DatePickerDialog(
                    getMContext().get()!!, date, myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH],
                    myCalendar[Calendar.DAY_OF_MONTH]
                )
                picker.updateDate(1990,month,day)
                picker.show()
            }
            R.id.tv_next_step -> {
                act.dataBody["nickname"] = ed_user_name.text.toString()
                act.dataBody["birthday"] = ed_user_birth.text.toString()
                act.dataBody["gender"] = selectedGender.toString()
                Log.e("Peter","profileActivityVM.updateMyInfo(act.dataBody)   ${act.dataBody}")
                profileActivityVM.updateMyInfo(act.dataBody)

            }
        }
    }

    private fun checkNextStep(){
        Log.e("Peter","checkNextStep  hasBirth   $hasBirth")
        Log.e("Peter","checkNextStep  hasGender  $hasGender")
        Log.e("Peter","checkNextStep  hasName   $hasName")
        Log.e("Peter","checkNextStep  hasPhoto   $hasPhoto")

        if(hasBirth && hasGender && hasName &&hasPhoto){
            tv_next_step.isClickable = true
            tv_next_step.background = getMContext().get()!!.resources.getDrawable(R.drawable.bg_clickable_btn)
            tv_next_step.setTextColor(getMContext().get()!!.resources.getColor(R.color.colorWhite))
        }
    }

    private fun updateLabel() {
        val myFormat = "yyyy-MM-dd"
        val yearFormat = "yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        val sdfYear = SimpleDateFormat(yearFormat, Locale.US)
        val currentTime = Calendar.getInstance().time
        val currentYear = sdfYear.format(currentTime).toInt()
        val chooseYear = sdfYear.format(myCalendar.time).toInt()
        var age = abs(currentYear - chooseYear)


        if (currentTime.month - myCalendar[Calendar.MONTH] < 0){
            age--
        } else if (currentTime.month - myCalendar[Calendar.MONTH] == 0 && currentTime.date - myCalendar[Calendar.DAY_OF_MONTH] < 0){
            age--
        }

        ed_user_birth.setText(sdf.format(myCalendar.time))
    }

    private fun initObserve(){

        profileActivityVM.getMyInfoData().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it.user.photos?.forEach {
                if(it.sort == 0){
                    dealProfilePhoto(BuildConfig.IMAGE_URL+it.url)
                    ImgHelper.loadNormalImg(getMContext().get(), BuildConfig.IMAGE_URL+it.url, iv_profile_photo)
                }
            }
        })

        profileActivityVM.getMyPhoto().observe(viewLifecycleOwner, androidx.lifecycle.Observer {

            dealProfilePhoto(BuildConfig.IMAGE_URL+it[0].url)
            ImgHelper.loadNormalImg(getMContext().get(), BuildConfig.IMAGE_URL+it[0].url, iv_profile_photo)

        })

        profileActivityVM.getUpdateMyInfoResponse().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if(it != null){
                Log.e("Peter","getUpdateMyInfoResponse FINISH  ")
                PrefHelper.setChatName(it.data.user.nickname)

                act.finish()
                getMContext().get()?.let { it1 -> IntentHelper.gotoEventActivity(it1) }
            }
        })
    }

    private fun dealProfilePhoto(url: String){
        Glide.with(getMContext().get()!!)
            .asBitmap()
            .load(url)
            .into(object : CustomTarget<Bitmap?>() {
                override fun onLoadCleared(placeholder: Drawable?) {
                    Log.e("Peter","onLoadCleared  ")
                }

                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap?>?) {
                    Palette.from(resource).generate {
                        Log.e("Peter","onResourceReady0  ${it?.swatches}")

                        Log.e("Peter","onResourceReady  ${it?.vibrantSwatch}")
                        Log.e("Peter","onResourceReady  ${it?.darkVibrantSwatch}")
                        Log.e("Peter","onResourceReady  ${it?.lightVibrantSwatch}")
                        Log.e("Peter","onResourceReady  ${it?.mutedSwatch}")
                        Log.e("Peter","onResourceReady  ${it?.darkMutedSwatch}")
                        Log.e("Peter","onResourceReady  ${it?.lightMutedSwatch}")

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

                        v2.setColor(mainColor)
                        v1.setColor(secondColor)
                    }
                }
            })
    }


    private fun callApis(){
//        profileActivityVM.getMyInfo()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == AppCompatActivity.RESULT_OK){
                Log.e("Peter", "updateMyPhoto onError:  ")
                val result = CropImage.getActivityResult(data)
                Tools.saveCropImage(result.uri)
                val file = Tools.dealCropImage()
                Tools.deleteCropImage()
                profileActivityVM.updateMyPhoto("0", file)
                iv_background_icon.visibility = View.GONE
                hasPhoto = true
                checkNextStep()
            }
        }
    }

}
