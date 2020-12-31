package com.illa.joliveapp.tools

import android.app.Activity
import android.content.Context
import android.util.Log
import com.illa.joliveapp.dialog.DialogProgress

object ProgressDialogController {

    private var mContext: Context ?= null
    private var progressDialog: DialogProgress ?= null

    fun setContext(context: Context){
        Log.e("Peter", "ProgressDialogController    $context")
        if(context == mContext){
            return
        }
        mContext = context
        progressDialog = null
        progressDialog = DialogProgress(context,0)
    }

    fun showProgress(){
        Log.e("peter", "ProgressDialogController showProgress")

        if((mContext as Activity).isFinishing){
        } else {
            progressDialog?.show()
        }
    }

    fun dismissProgress(){
        Log.e("peter", "ProgressDialogController dismissProgress")

        progressDialog?.dismiss()
    }
}