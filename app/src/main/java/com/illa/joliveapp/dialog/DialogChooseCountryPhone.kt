package com.illa.joliveapp.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.illa.joliveapp.R
import com.illa.joliveapp.adapter.Adapter_Choose_Country_Phone
import com.jay.widget.StickyHeadersLinearLayoutManager
import kotlinx.android.synthetic.main.dialog_choose_country_phone.*

class DialogChooseCountryPhone(context: Context, themeResId: Int) : Dialog(context, themeResId) {

    private lateinit var adapter: Adapter_Choose_Country_Phone


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_choose_country_phone)
        init()
    }

    private fun init(){
        adapter = Adapter_Choose_Country_Phone()
        rv_country_phone.layoutManager = StickyHeadersLinearLayoutManager<Adapter_Choose_Country_Phone>(context)
        rv_country_phone.adapter = adapter
    }
}