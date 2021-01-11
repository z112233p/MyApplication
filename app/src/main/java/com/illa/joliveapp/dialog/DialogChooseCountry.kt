package com.illa.joliveapp.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.illa.joliveapp.R
import com.illa.joliveapp.adapter.Adapter_Choose_Location
import kotlinx.android.synthetic.main.fragment_choose_location.*
import kotlinx.android.synthetic.main.fragment_choose_location.toolbar

class DialogChooseCountry(context: Context) : Dialog(context,R.style.MyFullScreenDialog) {
    private lateinit var adapter: Adapter_Choose_Location


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_choose_location)

        init()
    }

    private fun init(){
        adapter = Adapter_Choose_Location(context)
        rv_country.layoutManager = GridLayoutManager(context, 2, RecyclerView.VERTICAL, false)
        rv_country.adapter = adapter
        val dataList = ArrayList<String>()
        dataList.add("台北")
        dataList.add("台中")
        dataList.add("高雄")
        dataList.add("香港")
        dataList.add("東京")
        dataList.add("大阪")
        dataList.add("舊金山")
        dataList.add("巴黎")
        dataList.add("其他")

        adapter.setData(dataList)

        toolbar.setNavigationOnClickListener {
            dismiss()
//            val intent = Intent(this, MapsActivity::class.java)
//            startActivity(intent)
        }
    }
}
