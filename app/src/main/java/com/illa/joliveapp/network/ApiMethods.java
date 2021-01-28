package com.illa.joliveapp.network;

import android.annotation.SuppressLint;
import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.RequiresApi;

import com.illa.joliveapp.datamodle.ErrorLogApi;
import com.illa.joliveapp.datamodle.authorization.LoginData;
import com.illa.joliveapp.datamodle.authorization.LoginResponse;
import com.illa.joliveapp.datamodle.authorization.ResendResponse;
import com.illa.joliveapp.datamodle.authorization.ResendSMS;
import com.illa.joliveapp.datamodle.authorization.register.Register;
import com.illa.joliveapp.datamodle.authorization.register.RegisterResponse;
import com.illa.joliveapp.datamodle.chat.chatroom_list.ChatRoomList;
import com.illa.joliveapp.datamodle.chat.history.ChatHistory;
import com.illa.joliveapp.datamodle.chat.image_message.response.FileResponse;
import com.illa.joliveapp.datamodle.chat.text_message.TextMessage;
import com.illa.joliveapp.datamodle.chat.text_message.response.TextResponse;
import com.illa.joliveapp.datamodle.chat_room.Token.ChatRoomToken;
import com.illa.joliveapp.datamodle.dating.DatingSearch;
import com.illa.joliveapp.datamodle.event.detailv2.EventDetailV2;
import com.illa.joliveapp.datamodle.event.event_list.EventList;
import com.illa.joliveapp.datamodle.event.index.EventIndex;
import com.illa.joliveapp.datamodle.event.list.TypeLists;
import com.illa.joliveapp.datamodle.event.my_events.MyEvents;
import com.illa.joliveapp.datamodle.event.review.EventReview;
import com.illa.joliveapp.datamodle.event.review_cancel.PostReviewCancel;
import com.illa.joliveapp.datamodle.event.review_member.ReviewMember;
import com.illa.joliveapp.datamodle.event.set_full_join.SetFullJoinDataBody;
import com.illa.joliveapp.datamodle.firebase.SetFCM;
import com.illa.joliveapp.datamodle.follows.Follows;
import com.illa.joliveapp.datamodle.friend_status.FriendAgree;
import com.illa.joliveapp.datamodle.friend_status.FriendCancel;
import com.illa.joliveapp.datamodle.friend_status.FriendRefuse;
import com.illa.joliveapp.datamodle.friend_status.FriendRequest;
import com.illa.joliveapp.datamodle.instagram.IgDataBody;
import com.illa.joliveapp.datamodle.jomie.Jomie;
import com.illa.joliveapp.datamodle.notice.notice_data.Notice;
import com.illa.joliveapp.datamodle.notice.template.NoticeTemplate;
import com.illa.joliveapp.datamodle.profile.MyInfo;
import com.illa.joliveapp.datamodle.profile.delete_photo.DeleteMyPhoto;
import com.illa.joliveapp.datamodle.profile.delete_photo.response.DeleteMyPhotoResponse;
import com.illa.joliveapp.datamodle.profile.interest.interest;
import com.illa.joliveapp.datamodle.profile.job.job;
import com.illa.joliveapp.datamodle.profile.sort_photo.SortMyPhoto;
import com.illa.joliveapp.datamodle.profile.sort_photo.SortPhotoDataBody;
import com.illa.joliveapp.datamodle.profile.update_photo.UpdatePhotoResponse;
import com.illa.joliveapp.datamodle.profile.update.UpdateMtInfo;
import com.illa.joliveapp.datamodle.profile.update.UpdateMyInfoResponse;
import com.illa.joliveapp.datamodle.profile.user_info.UserInfo;
import com.illa.joliveapp.datamodle.wallet.Wallet;

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

    public static void getEventDetail(Observer<EventDetailV2> pbObserver, String label){
        ApiSubscribe(Objects.requireNonNull(ApiService.Companion.create(true).getEventDetail(label)), pbObserver);
    }

    public static void getEventDetailV2(Observer< EventDetailV2 > pbObserver, String label){
        ApiSubscribe(Objects.requireNonNull(ApiService.Companion.create(true).getEventDetailV2(label)), pbObserver);
    }

    public static void getEventDetailById(Observer< EventDetailV2 > pbObserver, String eventId){
        ApiSubscribe(Objects.requireNonNull(ApiService.Companion.create(true).getEventDetailById(eventId)), pbObserver);
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

    public static void setNoticeRead(Observer< String > pbObserver, String id ){
        ApiSubscribe(Objects.requireNonNull(ApiService.Companion.create(true).setNoticeRead(id)), pbObserver);
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

    public static void getFollows(Observer< Follows > pbObserver ){
        ApiSubscribe(Objects.requireNonNull(ApiService.Companion.create(true).getFollows()), pbObserver);
    }

    public static void postFollow(Observer< String > pbObserver, String label){
        ApiSubscribe(Objects.requireNonNull(ApiService.Companion.create(true).postFollow(label)), pbObserver);
    }

    public static void postUnFollow(Observer< String > pbObserver, String label){
        ApiSubscribe(Objects.requireNonNull(ApiService.Companion.create(true).postUnFollow(label)), pbObserver);
    }

    public static void closeEvent(Observer< String > pbObserver, String eventId){
        ApiSubscribe(Objects.requireNonNull(ApiService.Companion.create(true).closeEvent(eventId)), pbObserver);
    }

    public static void noticeAllRead(Observer< String > pbObserver){
        ApiSubscribe(Objects.requireNonNull(ApiService.Companion.create(true).noticeAllRead()), pbObserver);
    }

    public static void setIgToken(Observer<String> pbObserver, IgDataBody dataBody){
        ApiSubscribe(Objects.requireNonNull(ApiService.Companion.create(true).setIgToken(dataBody)), pbObserver);
    }

    public static void igDisconnect(Observer<String> pbObserver){
        ApiSubscribe(Objects.requireNonNull(ApiService.Companion.create(true).igDisconnect()), pbObserver);
    }

    public static void sortMyPhoto(Observer< SortMyPhoto > pbObserver, SortPhotoDataBody dataBdy){
        ApiSubscribe(Objects.requireNonNull(ApiService.Companion.create(true).sortMyPhoto(dataBdy)), pbObserver);
    }

    public static void setFcmToken(Observer< String > pbObserver, SetFCM dataBdy){
        ApiSubscribe(Objects.requireNonNull(ApiService.Companion.create(true).setFcmToken(dataBdy)), pbObserver);
    }

    public static void deleteFcmToken(Observer< String > pbObserver, SetFCM dataBdy){
        ApiSubscribe(Objects.requireNonNull(ApiService.Companion.create(true).deleteFcmToken(dataBdy)), pbObserver);
    }

    public static void cancelReview(Observer< String > pbObserver, PostReviewCancel dataBdy){
        ApiSubscribe(Objects.requireNonNull(ApiService.Companion.create(true).cancelReview(dataBdy)), pbObserver);
    }

    public static void setFullJoin(Observer< String > pbObserver, String eventId, SetFullJoinDataBody dataBdy){
        ApiSubscribe(Objects.requireNonNull(ApiService.Companion.create(true).setFullJoin(eventId, dataBdy)), pbObserver);
    }

    public static void getJomie(Observer< Jomie > pbObserver ){
        ApiSubscribe(Objects.requireNonNull(ApiService.Companion.create(true).getJomie()), pbObserver);
    }

    public static void friendRequest(Observer< String > pbObserver, FriendRequest dataBody){
        ApiSubscribe(Objects.requireNonNull(ApiService.Companion.create(true).friendRequest(dataBody)), pbObserver);
    }

    public static void friendAgree(Observer< String > pbObserver, FriendAgree dataBody){
        ApiSubscribe(Objects.requireNonNull(ApiService.Companion.create(true).friendAgree(dataBody)), pbObserver);
    }

    public static void friendRefuse(Observer< String > pbObserver, FriendRefuse dataBody){
        ApiSubscribe(Objects.requireNonNull(ApiService.Companion.create(true).friendRefuse(dataBody)), pbObserver);
    }

    public static void friendCancel(Observer< String > pbObserver, FriendCancel dataBody){
        ApiSubscribe(Objects.requireNonNull(ApiService.Companion.create(true).friendCancel(dataBody)), pbObserver);
    }

    public static void getWallet(Observer< Wallet > pbObserver){
        ApiSubscribe(Objects.requireNonNull(ApiService.Companion.create(true).getWallet()), pbObserver);
    }
}