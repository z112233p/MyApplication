package com.illa.joliveapp.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.illa.joliveapp.R
import com.illa.joliveapp.activity.FollowsActivity
import com.illa.joliveapp.adapter.Adapter_Follows
import com.illa.joliveapp.tools.IntentHelper
import com.illa.joliveapp.viewmodle.ProfileActivityVM
import kotlinx.android.synthetic.main.fragment_follows_fan.*


class FragmentFollowsFan(pageType: String) : BaseFragment(){

    private val profileActivityVM: ProfileActivityVM by activityViewModels()
    private lateinit var adapter: Adapter_Follows
    private lateinit var act: FollowsActivity
    private var pageType = "Follow"

    init {

        this.pageType = pageType
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_follows_fan
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initObserve()
        profileActivityVM.getFollows()
    }

    private fun init() {
        act = getMContext().get() as FollowsActivity

        if(pageType == "Follow"){
            setTitle("追蹤")
        } else {
            setTitle("粉絲")
        }

        adapter = Adapter_Follows(getMContext().get())
        adapter.setOnItemClickListener(object : Adapter_Follows.OnItemClickListener{
            override fun onItemClick(view: View?, status: Int, userId: Int) {
                Log.e("Peter","onItemClick")
            }

            override fun omAvatarClick(label: String) {
                getMContext().get()?.let { IntentHelper.gotoMyInfoActivity(it, label, 0) }
            }

        })

        rv_follows_fan.layoutManager = LinearLayoutManager(getMContext().get(), RecyclerView.VERTICAL, false)
        rv_follows_fan.adapter = adapter

    }

    private fun initObserve(){
        profileActivityVM.getFollowsData().observe(viewLifecycleOwner, Observer {
            if(pageType == "Follow"){
                adapter.setData(it.data.follows)
            } else {
                adapter.setData(it.data.fans)

            }
        })

    }
}