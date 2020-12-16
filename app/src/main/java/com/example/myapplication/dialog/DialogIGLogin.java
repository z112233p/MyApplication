package com.example.myapplication.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SyncRequest;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.myapplication.R;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class DialogIGLogin extends Dialog {
    private String request_url = "https://api.instagram.com/oauth/authorize" +
            "?app_id=778792922655851" +
            "&redirect_uri=https%3A%2F%2Fdev.illa.me%2Fig_redirect" +
            "&scope="+"user_profile,user_media"+
            "&response_type=code";



    public DialogIGLogin(@NonNull Context context) {
        super(context);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_ig_login);
        WebView webView = findViewById(R.id.webView);
        LinearLayout llMain = findViewById(R.id.ll_main);
        TextView tvClick = findViewById(R.id.tv_click);



        WebSettings wvSetting = webView.getSettings();
        wvSetting.setJavaScriptEnabled(true);
        wvSetting.setDomStorageEnabled(true);
//        wvSetting.setAppCacheEnabled(false);
//        wvSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);
//        wvSetting.setJavaScriptCanOpenWindowsAutomatically(true);

        webView.clearHistory();
        webView.clearCache(true);
        webView.loadUrl(request_url);
        webView.setWebViewClient(webViewClient);
        webView.reload();
        tvClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView.clearHistory();
                webView.clearCache(true);
                webView.loadUrl(request_url);
                Log.e("Peter","DialogIGLogin    setOnCancelListener");


            }
        });


    }

    private String convertIntoUth8Format(String url) {
        String newStr = "";
        try {
            newStr = URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Log.e("Peter","DialogIGLogin   convertIntoUth8Format "+newStr);

        return newStr;
    }

    WebViewClient webViewClient = new WebViewClient() {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if (url.startsWith("https://dev.illa.me/ig_redirect")) {
                DialogIGLogin.this.dismiss();
                return true;
            }
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.e("Peter","DialogIGLogin    "+url);
            if (url.contains("access_token=")) {
                Uri uri = Uri.EMPTY.parse(url);
                String access_token = uri.getEncodedFragment();
                access_token = access_token.substring(access_token.lastIndexOf("=") + 1);
//                listener.onTokenReceived(access_token);
            }
        }
    };

}
