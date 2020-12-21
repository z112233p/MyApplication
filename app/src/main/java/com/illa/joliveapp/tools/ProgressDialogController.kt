package com.illa.joliveapp.tools

import android.content.Context
import com.illa.joliveapp.dialog.DialogProgress

object ProgressDialogController {

    private var mContext: Context ?= null
    private var progressDialog: DialogProgress ?= null

    fun setContext(context: Context){
        if(context == mContext){
            return
        }
        mContext = context
        progressDialog = null
        progressDialog = DialogProgress(context,0)
    }

    fun showProgress(){
        progressDialog?.show()
    }

    fun dismissProgress(){
        progressDialog?.dismiss()
    }
}