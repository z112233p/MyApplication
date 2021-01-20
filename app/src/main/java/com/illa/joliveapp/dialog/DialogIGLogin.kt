package com.illa.joliveapp.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.webkit.*
import android.widget.LinearLayout
import android.widget.TextView
import com.illa.joliveapp.R
import java.io.UnsupportedEncodingException
import java.net.URLDecoder


class DialogIGLogin(context: Context, themeResId: Int) : Dialog(context, themeResId) {
    private var callBack: onUrlSuccess? = null
    private lateinit var webView: WebView
    private val request_url = "https://api.instagram.com/oauth/authorize" +
            "?app_id=778792922655851" +
            "&redirect_uri=https://api.illa.me/ig_redirect" +
            "&scope=" + "user_profile,user_media" +
            "&response_type=code"

    interface onUrlSuccess {
        fun codeResponse(code: String?)
    }

    fun setCallBack(callBack: onUrlSuccess?) {
        this.callBack = callBack
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.dialog_ig_login)
        webView = findViewById<WebView>(R.id.webView)
        webView.clearCache(true)


        CookieSyncManager.createInstance(context);
        CookieSyncManager.getInstance().startSync();
        CookieManager.getInstance().removeSessionCookie();
        CookieManager.getInstance().removeAllCookie();
        CookieSyncManager.getInstance().sync();
        webView.clearCache(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        val llMain = findViewById<LinearLayout>(R.id.ll_main)
        val tvClick = findViewById<TextView>(R.id.tv_click)
        val wvSetting = webView.settings
        wvSetting.javaScriptEnabled = true
        wvSetting.domStorageEnabled = false
                wvSetting.setAppCacheEnabled(false);
        wvSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);
//        wvSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        webView.clearHistory()
        webView.clearCache(true)
        synCookies(context, request_url)
        webView.loadUrl(request_url)
        webView.webViewClient = webViewClient
        webView.reload()
        tvClick.setOnClickListener {
            webView.clearHistory()
            webView.clearCache(true)
            webView.loadUrl(request_url)
            Log.e("Peter", "DialogIGLogin    setOnCancelListener")
        }
    }

    override fun dismiss() {
        CookieSyncManager.createInstance(context);
        CookieSyncManager.getInstance().startSync();
        CookieManager.getInstance().removeSessionCookie();
        CookieManager.getInstance().removeAllCookie();
        CookieSyncManager.getInstance().sync();
        webView.clearCache(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        super.dismiss()
    }

    fun synCookies(context: Context?, url: String?) {

        CookieSyncManager.createInstance(context);
        CookieSyncManager.getInstance().startSync();
        CookieManager.getInstance().removeSessionCookie();

//
//        CookieSyncManager.createInstance(context)
//        val cookieManager: CookieManager = CookieManager.getInstance()
//        cookieManager.setAcceptCookie(true)
//        cookieManager.removeSessionCookie() //移除
////        cookieManager.setCookie(url, cookies) //指定要修改的cookies
//        CookieSyncManager.getInstance().sync()
    }

    private fun convertIntoUth8Format(url: String): String {
        var newStr = ""
        try {
            newStr = URLDecoder.decode(url, "UTF-8")
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        Log.e("Peter", "DialogIGLogin   convertIntoUth8Format $newStr")
        return newStr
    }

    var webViewClient: WebViewClient = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: WebView,
            url: String
        ): Boolean {
            return false
        }

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            Log.e("Peter", "DialogIGLogin    $url")
            val uri = Uri.parse(url)
            val code = uri.getQueryParameter("code")
            Log.e("Peter", "DialogIGLogin  uri $uri")
            Log.e("Peter", "DialogIGLogin  code $code")
            if (!TextUtils.isEmpty(code)) {
                Log.e("Peter", "DialogIGLogin  codeResponse  $code")

                callBack!!.codeResponse(code)
                dismiss()
            }
        }
    }
}