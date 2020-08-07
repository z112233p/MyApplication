package com.example.myapplication.rx;

import android.annotation.SuppressLint;

import com.example.myapplication.datamodle.ErrorLogApi;
import com.example.myapplication.datamodle.authorization.LoginData;
import com.example.myapplication.datamodle.authorization.LoginResponse;
import com.example.myapplication.datamodle.profile.MyInfo;

import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ApiMethods {
    @SuppressLint("CheckResult")
    private static void ApiSubscribe(Observable observable, Observer observer){
        observable.subscribeOn(Schedulers.io())
        .unsubscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(observer);

    }

    public static void login(Observer<LoginResponse> pbObserver, LoginData dataBody){
        ApiSubscribe(Objects.requireNonNull(ApiService.Companion.create(false).login(dataBody)), pbObserver);
    }


    public static void getMyInfo(Observer<MyInfo> pbObserver){
        ApiSubscribe(Objects.requireNonNull(ApiService.Companion.create(true).getMyInfo()), pbObserver);
    }

    public static void setErrorLog(Observer<String> pbObserver, ErrorLogApi dataBody){
        ApiSubscribe(Objects.requireNonNull(ApiService.Companion.create(false).setErrorLog(dataBody)),pbObserver);
    }


}
