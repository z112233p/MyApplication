package com.example.myapplication.network;

import android.annotation.SuppressLint;

import com.example.myapplication.datamodle.ErrorLogApi;
import com.example.myapplication.datamodle.authorization.LoginData;
import com.example.myapplication.datamodle.authorization.LoginResponse;
import com.example.myapplication.datamodle.authorization.ResendResponse;
import com.example.myapplication.datamodle.authorization.ResendSMS;
import com.example.myapplication.datamodle.authorization.register.Register;
import com.example.myapplication.datamodle.authorization.register.RegisterResponse;
import com.example.myapplication.datamodle.chat.ChatRoomList;
import com.example.myapplication.datamodle.chat.history.ChatHistory;
import com.example.myapplication.datamodle.chat.image_message.response.FileResponse;
import com.example.myapplication.datamodle.chat.text_message.TextMessage;
import com.example.myapplication.datamodle.chat.text_message.response.TextResponse;
import com.example.myapplication.datamodle.dating.DatingSearch;
import com.example.myapplication.datamodle.profile.MyInfo;
import com.example.myapplication.datamodle.profile.delete_photo.DeleteMyPhoto;
import com.example.myapplication.datamodle.profile.delete_photo.response.DeleteMyPhotoResponse;
import com.example.myapplication.datamodle.profile.update_photo.UpdatePhotoResponse;
import com.example.myapplication.datamodle.profile.update.UpdateMtInfo;
import com.example.myapplication.datamodle.profile.update.UpdateMyInfoResponse;

import java.io.File;
import java.util.Date;
import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

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

    public static void register(Observer< RegisterResponse > pbObserver, Register dataBody){
        ApiSubscribe(Objects.requireNonNull(ApiService.Companion.create(false).register(dataBody)), pbObserver);
    }

    public static void resendSMS(Observer<ResendResponse> pbObserver, ResendSMS dataBody){
        ApiSubscribe(Objects.requireNonNull(ApiService.Companion.create(false).resendSMS(dataBody)), pbObserver);
    }

    public static void getMyInfo(Observer<MyInfo> pbObserver){
        ApiSubscribe(Objects.requireNonNull(ApiService.Companion.create(true).getMyInfo()), pbObserver);
    }

    public static void updateMyInfo(Observer<UpdateMyInfoResponse> pbObserver, UpdateMtInfo updateMtInfo){
        ApiSubscribe(Objects.requireNonNull(ApiService.Companion.create(true).updateMyInfo(updateMtInfo)), pbObserver);
    }

    public static void updateMyPhoto(Observer<UpdatePhotoResponse> pbObserver, String sort, File file){
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/jpg"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("photos", file.getName(), fileReqBody);
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), sort);

        ApiSubscribe(Objects.requireNonNull(ApiService.Companion.create(true).updateMyPhoto(part, description)), pbObserver);
    }

    public static void deleteMyInfo(Observer< DeleteMyPhotoResponse > pbObserver, DeleteMyPhoto deleteMyPhoto){
        ApiSubscribe(Objects.requireNonNull(ApiService.Companion.create(true).deleteMyPhoto(deleteMyPhoto)), pbObserver);
    }

    public static void getChatRoomList(Observer<ChatRoomList> pbObserver){
        ApiSubscribe(Objects.requireNonNull(ChatApiService.Companion.create(true, "").getChatRoom()), pbObserver);
    }

    public static void getChatHistory(Observer<ChatHistory> pbObserver, String rID){
        ApiSubscribe(Objects.requireNonNull(ChatApiService.Companion.create(true, "").getChatHistory(rID, 40)), pbObserver);
    }

    public static void getChatHistory(Observer<ChatHistory> pbObserver, String rID, Date latest){
        ApiSubscribe(Objects.requireNonNull(ChatApiService.Companion.create(true, "").getChatHistory(rID, 40, latest)), pbObserver);
    }

    public static void getDatingSearch(Observer<DatingSearch> pbObserver,int gender, int minAge, int maxAge, int maxKm){
        ApiSubscribe(Objects.requireNonNull(ApiService.Companion.create(true).getDatingSearch(gender,minAge,maxAge,maxKm)), pbObserver);
    }

    public static void postImageMessage(Observer<FileResponse> pbObserver, File file, String rId){
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/jpg"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), fileReqBody);
//        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), "image-type");

        ApiSubscribe(Objects.requireNonNull(ChatApiService.Companion.create(true, rId).postImageMessage(rId, part)), pbObserver);
    }

    public static void postAudioMessage(Observer<FileResponse> pbObserver, File file, String rId){
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("audio/m4a"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), fileReqBody);
//        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), "image-type");

        ApiSubscribe(Objects.requireNonNull(ChatApiService.Companion.create(true, rId).postAudioMessage(rId, part)), pbObserver);
    }

    public static void postTextMessage(Observer<TextResponse> pbObserver, TextMessage dataBody, String rId){
        ApiSubscribe(Objects.requireNonNull(ChatApiService.Companion.create(true, rId).postTextMessage(dataBody)), pbObserver);
    }

    public static void setErrorLog(Observer<String> pbObserver, ErrorLogApi dataBody){
        ApiSubscribe(Objects.requireNonNull(ApiService.Companion.create(false).setErrorLog(dataBody)),pbObserver);
    }


}
