package com.example.myapplication.activity

import `in`.arjsna.swipecardlib.SwipeCardView
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.adapter.Adapter_Dating_Search
import com.example.myapplication.datamodle.dating.DatingSearchData
import com.example.myapplication.datamodle.dating.DatingSearchUser
import com.example.myapplication.tools.PrefHelper
import com.example.myapplication.viewmodle.MainActivityVM
import com.example.myapplication.viewmodle.MatchActivityVM
import com.lorentzos.flingswipe.SwipeFlingAdapterView
import com.lorentzos.flingswipe.SwipeFlingAdapterView.onFlingListener
import kotlinx.android.synthetic.main.activity_match.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class MatchActivity : AppCompatActivity(){
    val matchActVM: MatchActivityVM by viewModel()

    private lateinit var flingContainer : SwipeCardView
    private lateinit var adapter: Adapter_Dating_Search
    private lateinit var dataList: ArrayList<DatingSearchUser>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match)

        init()
        initObserve()
        matchActVM.getDatingSearch(2,1,120,20)

    }

    private fun init(){
        flingContainer = frame as SwipeCardView

        val al = ArrayList<String>()
        al.add("php")
        al.add("c")
        al.add("python")
        al.add("java")


        val arrayAdapter = ArrayAdapter<String>(this, R.layout.item_chat_room, R.id.tv_chat_room_name, al)
//        flingContainer.adapter = arrayAdapter
        flingContainer.setFlingListener(object : SwipeCardView.OnCardFlingListener {
            override fun onCardExitLeft(dataObject: Any?) {
                Log.e("Peter","onCardExitLeft")
            }

            override fun onAdapterAboutToEmpty(itemsInAdapter: Int) {
                Log.e("Peter","onAdapterAboutToEmpty")
            }

            override fun onScroll(scrollProgressPercent: Float) {
                Log.e("Peter","onScroll")
            }

            override fun onCardExitTop(dataObject: Any?) {
                Log.e("Peter","onCardExitTop")
            }

            override fun onCardExitBottom(dataObject: Any?) {
                Log.e("Peter","onCardExitBottom")
            }

            override fun onCardExitRight(dataObject: Any?) {
                Log.e("Peter","onCardExitRight")
            }

        })
    }


    private fun initObserve(){
        matchActVM.getDatingSearchData()?.observe(this, Observer {
            dataList = it.users
            adapter = Adapter_Dating_Search(this,R.layout.item_dating_search,dataList)
            flingContainer.adapter = adapter
            adapter.notifyDataSetChanged()

        })
    }
}