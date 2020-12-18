package com.example.myapplication.network;

import android.annotation.SuppressLint;
import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.RequiresApi;

import com.example.myapplication.datamodle.ErrorLogApi;
import com.example.myapplication.datamodle.authorization.LoginData;
import com.example.myapplication.datamodle.authorization.LoginResponse;
import com.example.myapplication.datamodle.authorization.ResendResponse;
import com.example.myapplication.datamodle.authorization.ResendSMS;
import com.example.myapplication.datamodle.authorization.register.Register;
import com.example.myapplication.datamodle.authorization.register.RegisterResponse;
import com.example.myapplication.datamodle.chat.chatroom_list.ChatRoomList;
import com.example.myapplication.datamodle.chat.history.ChatHistory;
import com.example.myapplication.datamodle.chat.image_message.response.FileResponse;
import com.example.myapplication.datamodle.chat.text_message.TextMessage;
import com.example.myapplication.datamodle.chat.text_message.response.TextResponse;
import com.example.myapplication.datamodle.chat_room.Token.ChatRoomToken;
import com.example.myapplication.datamodle.dating.DatingSearch;
import com.example.myapplication.datamodle.event.detail.EventDetail;
import com.example.myapplication.datamodle.event.detailv2.EventDetailV2;
import com.example.myapplication.datamodle.event.event_list.EventList;
import com.example.myapplication.datamodle.event.index.EventIndex;
import com.example.myapplication.datamodle.event.list.TypeLists;
import com.example.myapplication.datamodle.event.my_events.MyEvents;
import com.example.myapplication.datamodle.event.review.EventReview;
import com.example.myapplication.datamodle.event.review_member.ReviewMember;
import com.example.myapplication.datamodle.notice.notice_data.Notice;
import com.example.myapplication.datamodle.notice.template.NoticeTemplate;
import com.example.myapplication.datamodle.profile.MyInfo;
import com.example.myapplication.datamodle.profile.delete_photo.DeleteMyPhoto;
import com.example.myapplication.datamodle.profile.delete_photo.response.DeleteMyPhotoResponse;
import com.example.myapplication.datamodle.profile.interest.interest;
import com.example.myapplication.datamodle.profile.job.job;
import com.example.myapplication.datamodle.profile.update_photo.UpdatePhotoResponse;
import com.example.myapplication.datamodle.profile.update.UpdateMtInfo;
import com.example.myapplication.datamodle.profile.update.UpdateMyInfoResponse;
import com.example.myapplication.datamodle.profile.user_info.UserInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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
    }//getUserInfo

    public static void getUserInfo(Observer< UserInfo > pbObserver, String userLabel){
        ApiSubscribe(Objects.requireNonNull(ApiService.Companion.create(true).getUserInfo(userLabel)), pbObserver);
    }

    public static void updateMyInfo(Observer<UpdateMyInfoResponse> pbObserver, UpdateMtInfo updateMtInfo){
        ApiSubscribe(Objects.requireNonNull(ApiService.Companion.create(true).updateMyInfo(updateMtInfo)), pbObserver);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void updateMyInfo_v2(Observer<UpdateMyInfoResponse> pbObserver, HashMap<String, String> dataBody){
        String[] keySet = dataBody.keySet().toArray(new String[0]);

        MultipartBody.Builder bodyBuilder = new MultipartBody.Builder();
        MultipartBody.Builder builder = bodyBuilder.setType(MultipartBody.FORM);
        Arrays.stream(keySet)
                .forEach(key -> builder.addFormDataPart(key, Objects.requireNonNull(dataBody.get(key))));

        RequestBody body = builder.build();

        ApiSubscribe(Objects.requireNonNull(ApiService.Companion.create(true).updateMyInfo_v2(body)), pbObserver);
    }



    public static void updateMyPhoto(Observer<UpdatePhotoResponse> pbObserver, String sort, File file){
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/jpg"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("photos", file.getName(), fileReqBody);
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), sort);

        ApiSubscribe(Objects.requireNonNull(ApiService.Companion.create(true).updateMyPhoto(part, description)), pbObserver);
    }

    public static void deleteMyPhoto(Observer< DeleteMyPhotoResponse > pbObserver, DeleteMyPhoto deleteMyPhoto){
        ApiSubscribe(Objects.requireNonNull(ApiService.Companion.create(true).deleteMyPhoto(deleteMyPhoto)), pbObserver);
    }

    public static void getEvents(Observer< EventList > pbObserver, String label, String eventsCategorysId){
        String labelValue = TextUtils.isEmpty(label)? null: label;
        ApiSubscribe(Objects.requireNonNull(ApiService.Companion.create(true).getEvents(1, 99, 1,labelValue, eventsCategorysId)), pbObserver);
    }

    public static void joinEvent(Observer<String> pbObserver, String id){
        ApiSubscribe(Objects.requireNonNull(ApiService.Companion.create(true).joinEvent(id)), pbObserver);
    }

    public static void cancelJoinEvent(Observer<String> pbObserver, String id){
        ApiSubscribe(Objects.requireNonNull(ApiService.Companion.create(true).cancelJoinEvent(id)), pbObserver);
    }

    public static void getReviewList(Observer<EventReview> pbObserver, String id){
        ApiSubscribe(Objects.requireNonNull(ApiService.Companion.create(true).getReviewList(id)), pbObserver);
    }

    public static void getEventDetail(Observer<EventDetail> pbObserver, String label){
        ApiSubscribe(Objects.requireNonNull(ApiService.Companion.create(true).getEventDetail(label)), pbObserver);
    }

    public static void getEventDetailV2(Observer< EventDetailV2 > pbObserver, String label){
        ApiSubscribe(Objects.requireNonNull(ApiService.Companion.create(true).getEventDetailV2(label)), pbObserver);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void createEvent(Observer<String> pbObserver, Map<String, String> dataBody, File file){
        String[] keySet = dataBody.keySet().toArray(new String[0]);

        MultipartBody.Builder bodyBuilder = new MultipartBody.Builder();
        MultipartBody.Builder builder = bodyBuilder.setType(MultipartBody.FORM);
        Arrays.stream(keySet)
                .forEach(key -> builder.addFormDataPart(key, Objects.requireNonNull(dataBody.get(key))));

        RequestBody fileRQ = RequestBody.create(MediaType.parse("mage/jpg"), file);
        builder.addFormDataPart("image",file.getName(),fileRQ);
        RequestBody body = builder.build();

       ApiSubscribe(Objects.requireNonNull(ApiService.Companion.create(true).createEvent(body)), pbObserver);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void updateEvent(Observer<String> pbObserver, Map<String, String> dataBody, File file, String eventID){
        String[] keySet = dataBody.keySet().toArray(new String[0]);

        MultipartBody.Builder bodyBuilder = new MultipartBody.Builder();
        MultipartBody.Builder builder = bodyBuilder.setType(MultipartBody.FORM);
        Arrays.stream(keySet)
                .forEach(key -> builder.addFormDataPart(key, Objects.requireNonNull(dataBody.get(key))));

        if(file.exists()){
            RequestBody fileRQ = RequestBody.create(MediaType.parse("mage/jpg"), file);
            builder.addFormDataPart("image",file.getName(),fileRQ);
        }

        RequestBody body = builder.build();

        ApiSubscribe(Objects.requireNonNull(ApiService.Companion.create(true).updateEvent(eventID, body)), pbObserver);
    }

    public static void postEventReview(Observer<String> pbObserver, ReviewMember dataBody){
        ApiSubscribe(Objects.requireNonNull(ApiService.Companion.create(true).postEventReview(dataBody)), pbObserver);
    }

    public static void postEventRollCall(Observer<String> pbObserver, ReviewMember dataBody){
        ApiSubscribe(Objects.requireNonNull(ApiService.Companion.create(true).postEventRollCall(dataBody)), pbObserver);
    }

    public static void getPaymentMethod(Observer<TypeLists> pbObserver){
        ApiSubscribe(Objects.requireNonNull(ApiService.Companion.create(true).getPaymentMethod()), pbObserver);
    }

    public static void getCurrencyType(Observer<TypeLists> pbObserver){
        ApiSubscribe(Objects.requireNonNull(ApiService.Companion.create(true).getCurrencyType()), pbObserver);
    }

    public static void getEventCategory(Observer<TypeLists> pbObserver){
        ApiSubscribe(Objects.requireNonNull(ApiService.Companion.create(true).getEventCategory()), pbObserver);
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

    public static void getJobList(Observer<job> pbObserver){
        ApiSubscribe(Objects.requireNonNull(ApiService.Companion.create(false).getJobList()), pbObserver);
    }

    public static void getInterestList(Observer<interest> pbObserver){
        ApiSubscribe(Objects.requireNonNull(ApiService.Companion.create(false).getInterestList()), pbObserver);
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

    public static void getMapAddress(Observer<String> pbObserver, ArrayList<Double> latlng, String key){
        ApiSubscribe(Objects.requireNonNull(GoogleApiService.Companion.create(false).getMapAddress(latlng, key)), pbObserver);
    }


    public static void setErrorLog(Observer<String> pbObserver, ErrorLogApi dataBody){
        ApiSubscribe(Objects.requireNonNull(ApiService.Companion.create(false).setErrorLog(dataBody)),pbObserver);
    }


    public static void getChatRoomToken(Observer< ChatRoomToken > pbObserver ){
        ApiSubscribe(Objects.requireNonNull(ApiService.Companion.create(true).getChatRoomToken()), pbObserver);
    }

    public static void getNoticeTemplate(Observer< NoticeTemplate > pbObserver ){
        ApiSubscribe(Objects.requireNonNull(ApiService.Companion.create(true).getNoticeTemplate()), pbObserver);
    }

    public static void getNotice(Observer< Notice > pbObserver ){
        ApiSubscribe(Objects.requireNonNull(ApiService.Companion.create(true).getNotice()), pbObserver);
    }

    public static void getEventIndex(Observer< EventIndex > pbObserver ){
        ApiSubscribe(Objects.requireNonNull(ApiService.Companion.create(true).getEventIndex()), pbObserver);
    }

    public static void getMyEvents(Observer< MyEvents > pbObserver ){
        ApiSubscribe(Objects.requireNonNull(ApiService.Companion.create(true).getMyEvents()), pbObserver);
    }

    public static void getUserEvents(Observer< MyEvents > pbObserver, String userLabel ){
        ApiSubscribe(Objects.requireNonNull(ApiService.Companion.create(true).getUserEvents(userLabel)), pbObserver);
    }

}