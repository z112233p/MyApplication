package com.example.myapplication.network;

import android.annotation.SuppressLint;

import com.example.myapplication.datamodle.ErrorLogApi;
import com.example.myapplication.datamodle.authorization.LoginData;
import com.example.myapplication.datamodle.authorization.LoginResponse;
import com.example.myapplication.datamodle.chat.ChatRoomList;
import com.example.myapplication.datamodle.chat.history.ChatHistory;
import com.example.myapplication.datamodle.chat.image_message.ImageMessage;
import com.example.myapplication.datamodle.chat.image_message.response.ImageResponse;
import com.example.myapplication.datamodle.chat.text_message.TextMessage;
import com.example.myapplication.datamodle.chat.text_message.response.TextResponse;
import com.example.myapplication.datamodle.dating.DatingSearch;
import com.example.myapplication.datamodle.dating.DatingSearchData;
import com.example.myapplication.datamodle.profile.MyInfo;

import java.io.File;
import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static okhttp3.MediaType.*;

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

    public static void getChatRoomList(Observer<ChatRoomList> pbObserver){
        ApiSubscribe(Objects.requireNonNull(ChatApiService.Companion.create(true, "").getChatRoom()), pbObserver);
    }

    public static void getChatHistory(Observer<ChatHistory> pbObserver, String rID){
        ApiSubscribe(Objects.requireNonNull(ChatApiService.Companion.create(true, "").getChatHistory(rID, 20)), pbObserver);
    }

    public static void getDatingSearch(Observer<DatingSearch> pbObserver,int gender, int minAge, int maxAge, int maxKm){
        ApiSubscribe(Objects.requireNonNull(ApiService.Companion.create(true).getDatingSearch(gender,minAge,maxAge,maxKm)), pbObserver);
    }

    public static void postImageMessage(Observer<ImageResponse> pbObserver, File file, String rId){
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), fileReqBody);
//        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), "image-type");

        ApiSubscribe(Objects.requireNonNull(ChatApiService.Companion.create(true, rId).postImageMessage(rId, part)), pbObserver);
    }

    public static void postTextMessage(Observer<TextResponse> pbObserver, TextMessage dataBody, String rId){
        ApiSubscribe(Objects.requireNonNull(ChatApiService.Companion.create(true, rId).postTextMessage(dataBody)), pbObserver);
    }

    public static void setErrorLog(Observer<String> pbObserver, ErrorLogApi dataBody){
        ApiSubscribe(Objects.requireNonNull(ApiService.Companion.create(false).setErrorLog(dataBody)),pbObserver);
    }


}
