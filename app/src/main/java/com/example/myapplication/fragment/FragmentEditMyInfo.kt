package com.example.myapplication.fragment

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.adapter.Adapter_Profile_Photo
import com.example.myapplication.datamodle.profile.delete_photo.DeleteMyPhoto
import com.example.myapplication.datamodle.profile.update.UpdateMtInfo
import com.example.myapplication.tools.PrefHelper
import com.example.myapplication.viewmodle.MainActivityVM
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.fragment_edit_myinfo.*
import kotlinx.android.synthetic.main.fragment_edit_myinfo.rv_profile_photo
import kotlinx.android.synthetic.main.fragment_edit_myinfo.tv_age
import kotlinx.android.synthetic.main.fragment_edit_myinfo.tv_language_id
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs
import kotlin.properties.Delegates


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
@Suppress("UNUSED_CHANGED_VALUE", "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class FragmentEditMyInfo : BaseFragment() {
    private val mainActVM: MainActivityVM by activityViewModels()
    private val myCalendar: Calendar = Calendar.getInstance()
    private lateinit var date: OnDateSetListener
    private lateinit var adapter: Adapter_Profile_Photo
    private var sort by Delegates.notNull<Int>()


    override fun getLayoutId(): Int {
        return R.layout.fragment_edit_myinfo
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initObserve()
        mainActVM.getMyInfo()
    }

    private fun init(){
        date = OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                myCalendar[Calendar.YEAR] = year
                myCalendar[Calendar.MONTH] = monthOfYear
                myCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth
                updateLabel()
        }

        adapter = getMContext().get()?.let { Adapter_Profile_Photo(it, true) }!!
        adapter.setOnItemClickListener(object : Adapter_Profile_Photo.OnItemClickListener{
            override fun onItemClick(view: View?, position: Int, url: String) {
                sort = position
                if(TextUtils.isEmpty(url)){
                    getMContext().get()?.let { CropImage.activity().start(it,this@FragmentEditMyInfo) }
                } else {
                    val dataBody = DeleteMyPhoto("delete",url)
                    mainActVM.deleteMyInfo(dataBody)
                }
            }
        })

        rv_profile_photo.layoutManager = LinearLayoutManager(getMContext().get(), RecyclerView.HORIZONTAL, false)
        rv_profile_photo.adapter = adapter

        btn_confirm.setOnClickListener(onClick)
        ed_birthday.setOnClickListener(onClick)
    }

    private var onClick: View.OnClickListener ?= View.OnClickListener {
        when(it.id){
            R.id.btn_confirm -> mainActVM.updateMyInfo(UpdateMtInfo(ed_name.text.toString(),ed_birthday.text.toString(),tv_age.text.toString()))
            R.id.ed_birthday ->{
                DatePickerDialog(
                    getMContext().get()!!, date, myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH],
                    myCalendar[Calendar.DAY_OF_MONTH]
                ).show()
            }
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

        ed_birthday.setText(sdf.format(myCalendar.time))
        tv_age.setText(age.toString())
    }

    private fun initObserve(){
        mainActVM.getMyInfoData().observe(viewLifecycleOwner, Observer {
            tv_age.setText(it.user.age)
            ed_birthday.setText(it.user.birthday)
            tv_language_id.text = it.user.language_id.toString()
            ed_name.setText(it.user.name)
            PrefHelper.setChatName(it.user.name)
            adapter.setData(it.user.photos)

        })

        mainActVM.getUpdateMyInfoResponse().observe(viewLifecycleOwner, Observer {
            if(it.code == 0){
                findNavController().navigate(R.id.action_FragmentEditMyInfo_to_SecondFragment)
            }
        })

        mainActVM.getMyPhoto().observe(viewLifecycleOwner, Observer {
            adapter.setData(it)
        })

        mainActVM.getDeleteMyPhoto().observe(viewLifecycleOwner, Observer {
            if(it.code == 0){
                mainActVM.getMyInfo()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == AppCompatActivity.RESULT_OK){
                Log.e("Peter", "updateMyPhoto onError:  $sort")
                val result = CropImage.getActivityResult(data)
                val file = File(result.uri.path)
                mainActVM.updateMyPhoto(sort.toString(), file)
            }
        }
    }
}