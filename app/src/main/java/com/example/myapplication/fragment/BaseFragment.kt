package com.example.myapplication.fragment

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.tools.Tools
import com.google.android.material.appbar.AppBarLayout
import java.lang.ref.WeakReference

@Suppress("CAST_NEVER_SUCCEEDS")
abstract class BaseFragment : Fragment(){

    private lateinit var mContext: WeakReference<Context>
    private lateinit var toolbarMain: Toolbar
    private lateinit var toolbarTitle: TextView

    private lateinit var tvTitle:TextView
    private lateinit var appBarLayout: AppBarLayout

    private var actionBar: ActionBar? = null


    abstract fun getLayoutId(): Int


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = WeakReference(context)
        Tools.hideSoftKeyboard(context as Activity)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        toolbarMain = (container!!.parent as ViewGroup).findViewById(R.id.toolbar)
        toolbarTitle = (container!!.parent as ViewGroup).findViewById(R.id.tv_toolbar_title)

        tvTitle = requireActivity().findViewById(R.id.tv_title)
        appBarLayout = (container.parent as ViewGroup).findViewById(R.id.appbar)
        actionBar = (activity as AppCompatActivity).supportActionBar
        return inflater.inflate(getLayoutId(),container,false)
    }

    fun getMContext(): WeakReference<Context> {
        return mContext
    }

    fun hideToolBar(){
        actionBar?.hide()
        toolbarMain.visibility = View.GONE
    }

    fun showToolBar(){
        actionBar?.show()
        toolbarMain.visibility = View.VISIBLE
    }

    fun expandToolbar(expend: Boolean){
        appBarLayout.setExpanded(expend, false)
    }

    fun setTitle(title: String){
        tvTitle.text = title
    }

    fun setToolbarTitle(title: String){
        toolbarTitle.text = title
    }

    fun hideTitle(){
        tvTitle.visibility = View.GONE
    }

    fun showTitle(){
        tvTitle.visibility = View.VISIBLE
    }

    fun hideToolbarTitle(){
        toolbarTitle.visibility = View.GONE
    }

    fun showToolbarTitle(){
        toolbarTitle.visibility = View.VISIBLE
    }
}