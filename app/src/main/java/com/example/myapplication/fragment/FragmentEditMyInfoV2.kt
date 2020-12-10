package com.example.myapplication.fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
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
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.MyApp
import com.example.myapplication.R
import com.example.myapplication.activity.EditMyInfoActivity
import com.example.myapplication.adapter.Adapter_Profile_Photo
import com.example.myapplication.adapter.Adapter_Profile_PhotoV2
import com.example.myapplication.tools.IntentHelper
import com.example.myapplication.tools.PrefHelper
import com.example.myapplication.tools.Tools
import com.example.myapplication.viewmodle.ProfileActivityVM
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
    private val dataBody :HashMap<String, String> = HashMap()
    private lateinit var adapter: Adapter_Profile_PhotoV2
    private lateinit var date: DatePickerDialog.OnDateSetListener
    private lateinit var genderList: ArrayList<String>

    override fun getLayoutId(): Int {
        return R.layout.fragment_edit_my_info

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
            setHasOptionsMenu(true)
            isNavigationViewInit = true
        }
    }

    override fun onResume() {
        super.onResume()
        setTitle("編輯個人檔案")

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

        cl_edit_nickname.tv_title.text = "暱稱"
        cl_edit_birth.tv_title.text = "生日"
        cl_edit_gender.tv_title.text = "性別"
        cl_edit_location.tv_title.text = "地點"
        cl_edit_interest.tv_title.text = "興趣"
        cl_edit_job.tv_title.text = "職業"

        cl_edit_birth.setOnClickListener(onClick)
        cl_edit_birth.ed_data.isFocusable = false
        cl_edit_birth.ed_data.isClickable = false
        adapter = getMContext().get()?.let { Adapter_Profile_PhotoV2(it, true) }!!
        val vfr = GridLayoutManager(getMContext().get(), 3)
        rv_my_photos.layoutManager = vfr//GridLayoutManager(getMContext().get(), 2, RecyclerView.VERTICAL, false)
        rv_my_photos.adapter = adapter
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
        }
    }

    private val onItemSelectedListener= object : AdapterView.OnItemSelectedListener{
        override fun onNothingSelected(p0: AdapterView<*>?) {
            Log.e("peter","sp_payment_method   onNothingSelected ")
        }

        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            cl_edit_gender.ed_data.setText(genderList[p2])
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

            cl_edit_nickname.ed_data.setText(it.user.nickname)
            cl_edit_birth.ed_data.setText(it.user.birthday)
            if(it.user.gender == 0){
//                cl_edit_gender.ed_data.setText("女")

            } else {
//                cl_edit_gender.ed_data.setText("男")
            }

            sp_gender.setSelection(it.user.gender)

            if(it.user.interest_map?.size!! > 1){
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
            } else {
                cl_edit_interest.ed_data.setText(MyApp.get()!!.getInterest(it.user.interest_map!![0]))
            }

            cl_edit_job.ed_data.setText(MyApp.get()!!.getJob(it.user.job_id.toInt()))
            ed_about_me.setText(it.user.about)
        })

        profileActivityVM.getUpdateMyInfoResponse().observe(viewLifecycleOwner, Observer {
            if(it != null){
                PrefHelper.setChatName(it.data.user.nickname)
                Tools.toast(getMContext().get(), "修改成功")
                (getMContext().get() as EditMyInfoActivity).finish()
            }
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
                profileActivityVM.updateMyInfo(dataBody)

                true
            }
            else -> true

        }
        return super.onOptionsItemSelected(item)

    }

}
