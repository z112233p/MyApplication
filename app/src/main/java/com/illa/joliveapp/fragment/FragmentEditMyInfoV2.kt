package com.illa.joliveapp.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import com.illa.joliveapp.MyApp
import com.illa.joliveapp.R
import com.illa.joliveapp.activity.EditMyInfoActivity
import com.illa.joliveapp.adapter.Adapter_Profile_PhotoV2
import com.illa.joliveapp.datamodle.profile.delete_photo.DeleteMyPhoto
import com.illa.joliveapp.datamodle.profile.sort_photo.SortPhotoDataBody
import com.illa.joliveapp.tools.PermissionsHelper
import com.illa.joliveapp.tools.PrefHelper
import com.illa.joliveapp.tools.RZItemTouchHelperCallback
import com.illa.joliveapp.tools.Tools
import com.illa.joliveapp.viewmodle.ProfileActivityVM
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_edit_my_info.view.tv_title
import kotlinx.android.synthetic.main.fragment_edit_my_info.*
import kotlinx.android.synthetic.main.item_edit_my_info.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "CAST_NEVER_SUCCEEDS")
class FragmentEditMyInfoV2 : BaseFragment() {
    private val profileActivityVM: ProfileActivityVM by activityViewModels()
    private val myCalendar: Calendar = Calendar.getInstance()
    private var selectedGender = 0
    private var photoPosition = 0
    private var currentPhotoSize = 0
    private val dataBody :HashMap<String, String> = HashMap()
    private lateinit var adapter: Adapter_Profile_PhotoV2
    private lateinit var date: DatePickerDialog.OnDateSetListener
    private lateinit var genderList: ArrayList<String>
    private lateinit var act: EditMyInfoActivity
    private lateinit var touchHelperCallback: RZItemTouchHelperCallback


    override fun getLayoutId(): Int {
        return R.layout.fragment_edit_my_info

    }

    @SuppressLint("NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        Log.e("Peter","FragmentEditMyInfoV2  isNavigationViewInit ")

        if(!isNavigationViewInit){
            Log.e("Peter","FragmentEditMyInfoV2  isNavigationViewInit  ININ ")
            super.onViewCreated(view, savedInstanceState)
            act = getMContext().get() as EditMyInfoActivity
            act.showActionItem()
            init()

            setHasOptionsMenu(true)
            isNavigationViewInit = true
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onResume() {
        Log.e("Peter","FragmentEditMyInfoV2  onResume ")

        super.onResume()
        getMContext().get()?.let { PermissionsHelper.setContext(it) }
        setTitle("編輯個人檔案")
        initObserve()
        callApis()
    }


    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    private fun init(){

        date = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            myCalendar[Calendar.YEAR] = year
            myCalendar[Calendar.MONTH] = monthOfYear
            myCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth
            updateLabel()
        }

        genderList = ArrayList()
        genderList.add("Female")
        genderList.add("male")
        val listAdapter = ArrayAdapter<String>(getMContext().get()!!, android.R.layout.simple_spinner_item, genderList)
        sp_gender.adapter = listAdapter
        sp_gender.onItemSelectedListener = onItemSelectedListener
        sp_gender.visibility = View.GONE
        cl_edit_nickname.tv_title.text = "暱稱"
        cl_edit_birth.tv_title.text = "生日"
        cl_edit_gender.tv_title.text = "性別"
        cl_edit_location.tv_title.text = "地點"
        cl_edit_interest.tv_title.text = "興趣"
        cl_edit_job.tv_title.text = "職業"

//        cl_edit_birth.setOnClickListener(onClick)
        cl_edit_birth.ed_data.isFocusable = false
        cl_edit_birth.ed_data.isClickable = false
        cl_edit_gender.ed_data.isFocusable = false
        cl_edit_gender.ed_data.isClickable = false
        cl_edit_location.ed_data.isFocusable = false
        cl_edit_location.ed_data.isClickable = false
        cl_edit_interest.ed_data.isFocusable = false
        cl_edit_interest.ed_data.isClickable = false
        cl_edit_interest.ed_data_under.isFocusable = false
        cl_edit_interest.ed_data_under.isClickable = false
        cl_edit_job.ed_data.isFocusable = false
        cl_edit_job.ed_data.isClickable = false

        cl_edit_location.ed_data.visibility = View.INVISIBLE

        cl_edit_birth.ed_data.setTextColor(getMContext().get()!!.resources.getColor(R.color.colorYellowD1))
        cl_edit_gender.ed_data.setTextColor(getMContext().get()!!.resources.getColor(R.color.colorYellowD1))


        cl_edit_interest.setOnClickListener(onClick)
        cl_edit_interest.ed_data_under.setOnClickListener {
            cl_edit_interest.performClick()
        }
        cl_edit_job.setOnClickListener(onClick)
        cl_edit_job.ed_data.setOnClickListener {
            cl_edit_job.performClick()
        }
//        cl_edit_location.ed_data.setTextColor(getMContext().get()!!.resources.getColor(R.color.colorYellowD1))

        adapter = getMContext().get()?.let { Adapter_Profile_PhotoV2(it, true) }!!

        adapter.setOnItemClickListener(object : Adapter_Profile_PhotoV2.OnItemClickListener{
            override fun onItemClick(view: View?, position: Int, url: String) {
                photoPosition = position
                if(ContextCompat.checkSelfPermission(getMContext().get()!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    PermissionsHelper.askWriteStorage()
                    PermissionsHelper.setCallBack(object : PermissionsHelper.onResultCallback{
                        override fun permissionResult() {
                            getMContext().get()?.let { ctx -> CropImage.activity().setAspectRatio(150,150).start(ctx,this@FragmentEditMyInfoV2) }
                        }

                    })
                    PermissionsHelper.startAskPermissions()
                } else {
                    getMContext().get()?.let { ctx -> CropImage.activity().setAspectRatio(150,150).start(ctx,this@FragmentEditMyInfoV2) }

                }
            }

            override fun onDeletePhoto(view: View?, position: Int, url: String) {
                if(currentPhotoSize == 1){
                 Tools.toast(getMContext().get(), "必須保留一張照片")
                    return
                }
                Log.e("Peter","onDeletePhoto    $url")
                val dataBody = DeleteMyPhoto("delete",url)
                profileActivityVM.deleteMyPhoto(dataBody)
            }

            override fun onSort(sortDataList: String) {
                profileActivityVM.sortMyPhoto(SortPhotoDataBody(sortDataList))
            }

        })
        val vfr = GridLayoutManager(getMContext().get(), 3)


        rv_my_photos.layoutManager = vfr//GridLayoutManager(getMContext().get(), 2, RecyclerView.VERTICAL, false)
        rv_my_photos.adapter = adapter

        touchHelperCallback = RZItemTouchHelperCallback(adapter,6)
        val itemTouchHelper = ItemTouchHelper(touchHelperCallback)
        itemTouchHelper.attachToRecyclerView(rv_my_photos);

    }



    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n", "ShowToast")
    private val onClick = View.OnClickListener {
        when (it.id) {
            R.id.cl_edit_birth -> {
                DatePickerDialog(
                    getMContext().get()!!, date, myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH],
                    myCalendar[Calendar.DAY_OF_MONTH]
                ).show()
            }
            R.id.cl_edit_interest -> findNavController().navigate(R.id.action_FragmentEditMyInfoV2_to_FragmentEditMyInfo_Interest)
            R.id.cl_edit_job -> findNavController().navigate(R.id.action_FragmentEditMyInfoV2_to_FragmentEditMyInfo_Job)
        }
    }

    private val onItemSelectedListener= object : AdapterView.OnItemSelectedListener{
        override fun onNothingSelected(p0: AdapterView<*>?) {
            Log.e("peter","sp_payment_method   onNothingSelected ")
        }

        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
//            cl_edit_gender.ed_data.setText(genderList[p2])
            selectedGender = p2

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

        cl_edit_birth.ed_data.setText(sdf.format(myCalendar.time))
    }


    private fun initObserve(){
        profileActivityVM.getMyInfoData().observe(viewLifecycleOwner, Observer {

            adapter.setData(it.user.photos)
            it.user.photos?.size?.let { it1 -> touchHelperCallback.setListSize(it1) }
            currentPhotoSize = it.user.photos?.size!!

            cl_edit_nickname.ed_data.setText(it.user.nickname)
            cl_edit_birth.ed_data.setText(it.user.birthday)
            if(it.user.gender == 0){
                cl_edit_gender.ed_data.setText("女")

            } else {
                cl_edit_gender.ed_data.setText("男")
            }

            sp_gender.setSelection(it.user.gender)


            Log.e("peter","act.interestArrayList    ${act.interestArrayList}")
            if(act.interestArrayList.size == 1){
                cl_edit_interest.ed_data.setText(MyApp.get()!!.getInterest(act.interestArrayList[0]))
                cl_edit_interest.ed_data.visibility = View.VISIBLE
                cl_edit_interest.ed_data_under.visibility = View.GONE

            } else if(act.interestArrayList.size > 1){
                var interests = ""
                act.interestArrayList.forEach { id ->
                    if(id == act.interestArrayList.last()){
                        interests += MyApp.get()!!.getInterest(id)

                    } else {
                        interests += MyApp.get()!!.getInterest(id)+", "

                    }
                }
                cl_edit_interest.ed_data_under.setText(interests)
                cl_edit_interest.ed_data_under.visibility = View.VISIBLE
                cl_edit_interest.ed_data.visibility = View.GONE

            } else if(it.user.interest_map?.size!! > 1){
                var interests = ""
                it.user.interest_map?.forEach { id ->
                    if(id == it.user.interest_map!!.last()){
                        interests += MyApp.get()!!.getInterest(id)

                    } else {
                        interests += MyApp.get()!!.getInterest(id)+", "

                    }
                }
                cl_edit_interest.ed_data_under.setText(interests)
                cl_edit_interest.ed_data_under.visibility = View.VISIBLE
                cl_edit_interest.ed_data.visibility = View.GONE

                act.interestArrayList.clear()
                it.user.interest_map?.forEach {
                    act.interestArrayList.add(it)
                }
                Log.e("peter","act.interestArrayList2    ${act.interestArrayList}")

            } else {
                cl_edit_interest.ed_data.setText(MyApp.get()!!.getInterest(it.user.interest_map!![0]))
                cl_edit_interest.ed_data.visibility = View.VISIBLE
                cl_edit_interest.ed_data_under.visibility = View.GONE

                act.interestArrayList.clear()
                it.user.interest_map?.forEach {
                    act.interestArrayList.add(it)
                }
                Log.e("peter","act.interestArrayList3    ${act.interestArrayList}")

            }

            if(act.jobId == -1){
                cl_edit_job.ed_data.setText(MyApp.get()!!.getJob(it.user.job_id.toInt()))
                act.jobId = it.user.job_id.toInt()

            } else {
                cl_edit_job.ed_data.setText(MyApp.get()!!.getJob(act.jobId))

            }

            ed_about_me.setText(it.user.about)
        })

        profileActivityVM.getUpdateMyInfoResponse().observe(viewLifecycleOwner, Observer {
            if(it != null){
                PrefHelper.setChatName(it.data.user.nickname)
                Tools.toast(getMContext().get(), "修改成功")
                (getMContext().get() as EditMyInfoActivity).finish()
            }
        })

        profileActivityVM.getMyPhoto().observe(viewLifecycleOwner, Observer {
            adapter.setData(it)
            currentPhotoSize = it.size
            touchHelperCallback.setListSize(currentPhotoSize)
        })

        profileActivityVM.getDeleteMyPhoto().observe(viewLifecycleOwner, Observer {
            if(it.code == 0){
                profileActivityVM.getMyInfo()
            }
        })

        profileActivityVM.getSortMyPhotoResponse().observe(viewLifecycleOwner, Observer {
            profileActivityVM.getMyInfo()

        })

    }


    private fun callApis(){
        profileActivityVM.getMyInfo()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.e("Peter","FragmentEditMyInfoV2 onOptionsItemSelected")
        return when(item.itemId){
            R.id.action_store ->{
                dataBody["nickname"] = cl_edit_nickname.ed_data.text.toString()
                dataBody["birthday"] = cl_edit_birth.ed_data.text.toString()
                dataBody["gender"] = selectedGender.toString()
                dataBody["about"] = ed_about_me.text.toString()
                dataBody["job_id"] = act.jobId.toString()

                var selectedPosition = 0
                Log.e("Peter","act.interestArrayList next   ${act.interestArrayList}")
                act.interestArrayList.forEach {
                    dataBody["interests[$selectedPosition]"] = it.toString()
                    selectedPosition++
                }


                profileActivityVM.updateMyInfo(dataBody)

                true
            }
            else -> true

        }
        return super.onOptionsItemSelected(item)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == AppCompatActivity.RESULT_OK){
                Log.e("Peter", "updateMyPhoto onError:  ")
                val result = CropImage.getActivityResult(data)
                Tools.saveUserPhoto(result.uri, photoPosition)
                val file = Tools.dealUserPhoto(photoPosition)
                Tools.deleteUserPhoto(photoPosition)
//                profileActivityVM.updateMyPhoto(photoPosition.toString(), file)
                profileActivityVM.updateMyPhoto(currentPhotoSize.toString(), file)

//                iv_background_icon.visibility = View.GONE
//                hasPhoto = true
//                checkNextStep()
            }
        }
    }

}
