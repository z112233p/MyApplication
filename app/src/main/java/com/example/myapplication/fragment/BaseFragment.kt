package com.example.myapplication.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import java.lang.ref.WeakReference

abstract class BaseFragment : Fragment(){

    private lateinit var mContext: WeakReference<Context>

    abstract fun getLayoutId(): Int

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = WeakReference(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutId(),container,false)
    }

    fun getMContext():WeakReference<Context> {
        return mContext
    }
}