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
        dataList.add("taipei")
        dataList.add("taichung")
        dataList.add("kaohsiung")
        dataList.add("san_srancisco")
        dataList.add("osaka")
        dataList.add("tokyo")
        dataList.add("hong_kong")
        adapter.setData(dataList)

        toolbar.setNavigationOnClickListener {
            dismiss()
//            val intent = Intent(this, MapsActivity::class.java)
//            startActivity(intent)
        }
    }
}
