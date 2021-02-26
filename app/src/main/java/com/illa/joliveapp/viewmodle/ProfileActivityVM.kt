package com.illa.joliveapp.viewmodle

import android.app.Application
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.illa.joliveapp.datamodle.event.event_list.EventList
import com.illa.joliveapp.datamodle.event.my_events.MyEvents
import com.illa.joliveapp.datamodle.follows.Follows
import com.illa.joliveapp.datamodle.friend_status.FriendAgree
import com.illa.joliveapp.datamodle.friend_status.FriendCancel
import com.illa.joliveapp.datamodle.friend_status.FriendRefuse
import com.illa.joliveapp.datamodle.friend_status.FriendRequest
import com.illa.joliveapp.datamodle.instagram.IgDataBody
import com.illa.joliveapp.datamodle.invitation.InvitationDataBody
import com.illa.joliveapp.datamodle.profile.MyInfo
import com.illa.joliveapp.datamodle.profile.MyInfoData
import com.illa.joliveapp.datamodle.profile.MyInfoPhoto
import com.illa.joliveapp.datamodle.profile.delete_photo.DeleteMyPhoto
import com.illa.joliveapp.datamodle.profile.delete_photo.response.DeleteMyPhotoResponse
import com.illa.joliveapp.datamodle.profile.interest.interest
import com.illa.joliveapp.datamodle.profile.job.job
import com.illa.joliveapp.datamodle.profile.sort_photo.SortMyPhoto
import com.illa.joliveapp.datamodle.profile.sort_photo.SortPhotoDataBody
import com.illa.joliveapp.datamodle.profile.update.UpdateMtInfo
import com.illa.joliveapp.datamodle.profile.update.UpdateMyInfoResponse
import com.illa.joliveapp.datamodle.profile.update_photo.UpdatePhotoResponse
import com.illa.joliveapp.datamodle.profile.user_info.UserInfo
import com.illa.joliveapp.datamodle.wallet.Wallet
import com.illa.joliveapp.network.ApiMethods
import com.illa.joliveapp.tools.PrefHelper
import com.illa.joliveapp.tools.SingleLiveEvent
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.io.File
import java.util.*

class ProfileActivityVM (application: Application) : AndroidViewModel(application){
    private val mContent: Context? = application.applicationContext
    private val disContainer : CompositeDisposable = CompositeDisposable()


    //set
    private val events: MutableLiveData<EventList> = MutableLiveData<EventList>()
    private val myEvents: MutableLiveData<MyEvents> = MutableLiveData<MyEvents>()

    private val jobList: MutableLiveData<job> = MutableLiveData<job>()
    private val interestList: MutableLiveData<interest> = MutableLiveData<interest>()
    private val myPhoto: MutableLiveData<List<MyInfoPhoto>> = MutableLiveData<List<MyInfoPhoto>>()
    private val myInfoData: MutableLiveData<MyInfoData> = MutableLiveData<MyInfoData>()
    private val updateMyInfoResponse: SingleLiveEvent<UpdateMyInfoResponse> = SingleLiveEvent<UpdateMyInfoResponse>()
    private val userInfo:MutableLiveData<UserInfo> = MutableLiveData<UserInfo>()
    private val deleteMyPhoto: SingleLiveEvent<DeleteMyPhotoResponse> = SingleLiveEvent<DeleteMyPhotoResponse>()
    private val follows: MutableLiveData<Follows> = MutableLiveData<Follows>()
    private val postFollowResponse: MutableLiveData<String> = MutableLiveData<String>()
    private val postUnFollowResponse: MutableLiveData<String> = MutableLiveData<String>()
    private val setIgTokenResponse: MutableLiveData<String> = MutableLiveData<String>()
    private val igDisconnectResponse: MutableLiveData<String> = MutableLiveData<String>()
    private val sortMyPhotoResponse: MutableLiveData<SortMyPhoto> = MutableLiveData<SortMyPhoto>()
    private val friendResponse: MutableLiveData<String> = MutableLiveData<String>()
    private val wallet:MutableLiveData<Wallet> = MutableLiveData<Wallet>()
    private val invitation: MutableLiveData<String> = MutableLiveData<String>()

    private val progressStatus: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    //get
    fun getEvents(): LiveData<EventList> {
        return events
    }
    fun getMyEventsData(): LiveData<MyEvents> {
        return myEvents
    }
    fun getJbListData(): LiveData<job> {
        return jobList
    }
    fun getInterestListData(): LiveData<interest> {
        return interestList
    }
    fun getMyInfoData(): LiveData<MyInfoData> {
        return myInfoData
    }
    fun getMyPhoto(): LiveData<List<MyInfoPhoto>> {
        return myPhoto
    }
    fun getUserInfoData(): LiveData<UserInfo> {
        return userInfo
    }
    fun getDeleteMyPhoto(): LiveData<DeleteMyPhotoResponse> {
        return deleteMyPhoto
    }
    fun getProgressStatus(): LiveData<Boolean> {
        return progressStatus
    }
    fun getUpdateMyInfoResponse(): LiveData<UpdateMyInfoResponse> {
        return updateMyInfoResponse
    }
    fun getFollowsData(): LiveData<Follows> {
        return follows
    }
    fun getFollowResponse(): LiveData<String> {
        return postFollowResponse
    }
    fun getUnFollowResponse(): LiveData<String> {
        return postUnFollowResponse
    }
    fun getIgTokenResponse(): LiveData<String> {
        return setIgTokenResponse
    }
    fun getIgDisconnectResponse(): LiveData<String> {
        return igDisconnectResponse
    }
    fun getSortMyPhotoResponse(): LiveData<SortMyPhoto> {
    return sortMyPhotoResponse
    }
    fun getFriendResponse(): LiveData<String> {
        return friendResponse
    }
    fun getWalletResponse(): LiveData<Wallet> {
        return wallet
    }
    fun getInvitation(): LiveData<String> {
        return invitation
    }

    fun getEventsApi(label: String?, eventsCategorysId: String?){
        val getEventsObserver = object : Observer<EventList>{
            override fun onComplete() {
                progressStatus.value = true
            }

            override fun onSubscribe(d: Disposable) {
                progressStatus.value = false
            }

            override fun onNext(t: EventList) {
                events.value = t
                Log.e("Peter","getEventsApi  onNext    $t")
                Log.e("Peter","getEventsApi  onNext   size ${t.data.event.size}")
                Log.e("Peter","getEventsApi  onNext   PARAMS  $label  $eventsCategorysId")

            }

            override fun onError(e: Throwable) {
                progressStatus.value = true

                Log.e("Peter","getEventsApi  onError    "+e.message)
            }

        }
        ApiMethods.getEvents(getEventsObserver,label, eventsCategorysId)
    }


    fun getJbList(){
        val getJbListObserver = object : Observer<job>{
            override fun onComplete() {
                progressStatus.value = true
            }

            override fun onSubscribe(d: Disposable) {
                progressStatus.value = false
            }

            override fun onNext(t: job) {
                Log.e("Peter","getJbList  onNext    $t")
                jobList.value = t
            }

            override fun onError(e: Throwable) {
                progressStatus.value = true

                Log.e("Peter","getJbList  onError    "+e.message)
            }

        }
        ApiMethods.getJobList(getJbListObserver)
    }

    fun getInterestList(){
        val getInterestListObserver = object : Observer<interest>{
            override fun onComplete() {
                progressStatus.value = true
            }

            override fun onSubscribe(d: Disposable) {
                progressStatus.value = false
            }

            override fun onNext(t: interest) {
                Log.e("Peter","getInterestList  onNext    $t")
                interestList.value = t
            }

            override fun onError(e: Throwable) {
                progressStatus.value = true

                Log.e("Peter","getInterestList  onError    "+e.message)
            }

        }
        ApiMethods.getInterestList(getInterestListObserver)
    }

    fun getMyInfo(){
        Log.e("Peter2", "getMyInfo  userLabel:  ${PrefHelper.chatLable}")

        val myInfoObserver: Observer<MyInfo> = object  : Observer<MyInfo>{
            override fun onComplete() {
                progressStatus.value = true
            }

            override fun onSubscribe(d: Disposable) {
                progressStatus.value = false
                disContainer.add(d)
            }

            override fun onNext(t: MyInfo) {
                Log.e("Peter2", "getMyInfo  onNext:  "+t.data.user.instargam_images)
                myInfoData.value = t.data
            }

            override fun onError(e: Throwable) {
                Log.e("Peter2", "getMyInfo  onError:  "+e.message)

                progressStatus.value = true
            }
        }
        ApiMethods.getMyInfo(myInfoObserver)
    }

    fun updateMyPhoto(sort: String, file: File){
        val imageMessageObserve: Observer<UpdatePhotoResponse> = object :Observer<UpdatePhotoResponse>{
            override fun onComplete() {
                progressStatus.value = true
                Log.e("Peter", "postImageMessage onComplete")
            }

            override fun onSubscribe(d: Disposable) {
                progressStatus.value = false
            }

            override fun onNext(t: UpdatePhotoResponse) {
                myPhoto.value = t.data.photos
                Log.e("Peter", "updateMyPhoto onNext:  ${t.toString()}")

            }

            override fun onError(e: Throwable) {
                progressStatus.value = true

                Log.e("Peter", "updateMyPhoto onError:  $e")
            }

        }
        ApiMethods.updateMyPhoto(imageMessageObserve, sort, file)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun updateMyInfo(dataBody: HashMap<String, String>){
        val myInfoObserver: Observer<UpdateMyInfoResponse> = object  : Observer<UpdateMyInfoResponse>{
            override fun onComplete() {
                progressStatus.value = true
            }

            override fun onSubscribe(d: Disposable) {
                progressStatus.value = false
                disContainer.add(d)
            }

            override fun onNext(t: UpdateMyInfoResponse) {
                Log.e("Peter2", "updateMyInfo??  onNext:  "+t)
                updateMyInfoResponse.value = t

            }

            override fun onError(e: Throwable) {
                progressStatus.value = true
                updateMyInfoResponse.value = null

                Log.e("Peter2", "updateMyInfo??  onError:  "+e.message)

            }
        }
        ApiMethods.updateMyInfo_v2(myInfoObserver, dataBody)
    }


    fun updateMyInfo(updateMtInfo: UpdateMtInfo){
        val myInfoObserver: Observer<UpdateMyInfoResponse> = object  : Observer<UpdateMyInfoResponse>{
            override fun onComplete() {
                progressStatus.value = true
            }

            override fun onSubscribe(d: Disposable) {
                progressStatus.value = false
                disContainer.add(d)
            }

            override fun onNext(t: UpdateMyInfoResponse) {
                updateMyInfoResponse.value = t
                Log.e("Peter2", "updateMyInfo  onNext:  "+t)
            }

            override fun onError(e: Throwable) {
                progressStatus.value = true

                Log.e("Peter2", "updateMyInfo  onError:  "+e.message)

            }
        }
        ApiMethods.updateMyInfo(myInfoObserver, updateMtInfo)
    }

    fun getMyEvents(){
        val myEventsObserver: Observer<MyEvents> = object  : Observer<MyEvents>{
            override fun onComplete() {
                progressStatus.value = true
            }

            override fun onSubscribe(d: Disposable) {
                progressStatus.value = false
                disContainer.add(d)
            }

            override fun onNext(t: MyEvents) {
                myEvents.value = t
                Log.e("Peter2", "updateMyInfo  onNext:  "+t)
            }

            override fun onError(e: Throwable) {
                progressStatus.value = true

                Log.e("Peter2", "updateMyInfo  onError:  "+e.message)

            }
        }
        ApiMethods.getMyEvents(myEventsObserver)
    }


    fun getUserInfo(userLabel: String){
        Log.e("Peter2", "getUserInfo  userLabel:  $userLabel")

        val myInfoObserver: Observer<UserInfo> = object  : Observer<UserInfo>{
            override fun onComplete() {
                progressStatus.value = true
            }

            override fun onSubscribe(d: Disposable) {
                progressStatus.value = false
                disContainer.add(d)
            }

            override fun onNext(t: UserInfo) {
                Log.e("Peter2", "getUserInfo  onNext:  "+t.data.user.instargam_images)
                userInfo.value = t
            }

            override fun onError(e: Throwable) {
                Log.e("Peter2", "getUserInfo  onError:  "+e.message)

                progressStatus.value = true
            }
        }
        ApiMethods.getUserInfo(myInfoObserver, userLabel)
    }


    fun getUserEvents(userLabel: String){
        val myEventsObserver: Observer<MyEvents> = object  : Observer<MyEvents>{
            override fun onComplete() {
                progressStatus.value = true
            }

            override fun onSubscribe(d: Disposable) {
                progressStatus.value = false
                disContainer.add(d)
            }

            override fun onNext(t: MyEvents) {
                myEvents.value = t
                Log.e("Peter2", "updateMyInfo  onNext:  "+t)
            }

            override fun onError(e: Throwable) {
                progressStatus.value = true

                Log.e("Peter2", "updateMyInfo  onError:  "+e.message)

            }
        }
        ApiMethods.getUserEvents(myEventsObserver, userLabel)
    }


    fun deleteMyPhoto(dataBody: DeleteMyPhoto){
        val deleteMyPhotoObserver: Observer<DeleteMyPhotoResponse> = object  : Observer<DeleteMyPhotoResponse>{
            override fun onComplete() {
                progressStatus.value = true
            }

            override fun onSubscribe(d: Disposable) {
                progressStatus.value = false
                disContainer.add(d)
            }

            override fun onNext(t: DeleteMyPhotoResponse) {
                deleteMyPhoto.value = t
                Log.e("Peter2", "deleteMyInfo  onNext:  "+t)
            }

            override fun onError(e: Throwable) {
                progressStatus.value = true

                Log.e("Peter2", "deleteMyInfo  onError:  "+e.message)

            }
        }
        ApiMethods.deleteMyPhoto(deleteMyPhotoObserver, dataBody)
    }

    fun getFollows(){
        val followsObserver: Observer<Follows> = object  : Observer<Follows>{
            override fun onComplete() {
                progressStatus.value = true
            }

            override fun onSubscribe(d: Disposable) {
                progressStatus.value = false
                disContainer.add(d)
            }

            override fun onNext(t: Follows) {
                follows.value = t
                Log.e("Peter2", "getFollows  onNext:  "+t)
            }

            override fun onError(e: Throwable) {
                progressStatus.value = true

                Log.e("Peter2", "getFollows  onError:  "+e.message)

            }
        }
        ApiMethods.getFollows(followsObserver)
    }

    fun postFollow(label: String){
        val postFollowObserver: Observer<String> = object  : Observer<String>{
            override fun onComplete() {
                progressStatus.value = true
            }

            override fun onSubscribe(d: Disposable) {
                progressStatus.value = false
                disContainer.add(d)
            }

            override fun onNext(t: String) {
                postFollowResponse.value = t
                Log.e("Peter2", "postFollow  onNext:  "+t)
            }

            override fun onError(e: Throwable) {
                progressStatus.value = true
                Log.e("Peter2", "postFollow  onError:  "+e.message)
            }
        }
        ApiMethods.postFollow(postFollowObserver, label)
    }

    fun postUnFollow(label: String){
        val postUnFollowObserver: Observer<String> = object  : Observer<String>{
            override fun onComplete() {
                progressStatus.value = true
            }

            override fun onSubscribe(d: Disposable) {
                progressStatus.value = false
                disContainer.add(d)
            }

            override fun onNext(t: String) {
                postUnFollowResponse.value = t
                Log.e("Peter2", "postUnFollow  onNext:  "+t)
            }

            override fun onError(e: Throwable) {
                progressStatus.value = true
                Log.e("Peter2", "postUnFollow  onError:  "+e.message)
            }
        }
        ApiMethods.postUnFollow(postUnFollowObserver, label)
    }

    fun setIgToken(dataBody: IgDataBody){
        val setIgTokenObserver: Observer<String> = object  : Observer<String>{
            override fun onComplete() {
                progressStatus.value = true
            }

            override fun onSubscribe(d: Disposable) {
                progressStatus.value = false
                disContainer.add(d)
            }

            override fun onNext(t: String) {
                setIgTokenResponse.value = t
                Log.e("Peter2", "postUnFollow  onNext:  "+t)
            }

            override fun onError(e: Throwable) {
                progressStatus.value = true
                Log.e("Peter2", "postUnFollow  onError:  "+e.message)
            }
        }
        ApiMethods.setIgToken(setIgTokenObserver, dataBody)
    }

    fun igDisconnect(){
        val igDisconnectObserver: Observer<String> = object  : Observer<String>{
            override fun onComplete() {
                progressStatus.value = true
            }

            override fun onSubscribe(d: Disposable) {
                progressStatus.value = false
                disContainer.add(d)
            }

            override fun onNext(t: String) {
                igDisconnectResponse.value = t
                Log.e("Peter2", "igDisconnect  onNext:  "+t)
            }

            override fun onError(e: Throwable) {
                progressStatus.value = true
                Log.e("Peter2", "igDisconnect  onError:  "+e.message)
            }
        }
        ApiMethods.igDisconnect(igDisconnectObserver)
    }

    fun sortMyPhoto(dataBody: SortPhotoDataBody){
        val sortMyPhotoObserver: Observer<SortMyPhoto> = object  : Observer<SortMyPhoto>{
            override fun onComplete() {
                progressStatus.value = true
            }

            override fun onSubscribe(d: Disposable) {
                progressStatus.value = false
                disContainer.add(d)
            }

            override fun onNext(t: SortMyPhoto) {
                sortMyPhotoResponse.value = t
                Log.e("Peter2", "igDisconnect  onNext:  "+t)
            }

            override fun onError(e: Throwable) {
                progressStatus.value = true
                Log.e("Peter2", "igDisconnect  onError:  "+e.message)
            }
        }
        ApiMethods.sortMyPhoto(sortMyPhotoObserver, dataBody)
    }

    fun friendRequest(dataBody: FriendRequest){
        val friendObserver: Observer<String> = object  : Observer<String>{
            override fun onComplete() {
                progressStatus.value = true
            }

            override fun onSubscribe(d: Disposable) {
                progressStatus.value = false
                disContainer.add(d)
            }

            override fun onNext(t: String) {
                friendResponse.value = t
                Log.e("Peter2", "friendRequest  onNext:  "+t)
            }

            override fun onError(e: Throwable) {
                progressStatus.value = true
                Log.e("Peter2", "friendRequest  onError:  "+e.message)
            }
        }
        ApiMethods.friendRequest(friendObserver, dataBody)
    }

    fun friendAgree(dataBody: FriendAgree){
        val friendObserver: Observer<String> = object  : Observer<String>{
            override fun onComplete() {
                progressStatus.value = true
            }

            override fun onSubscribe(d: Disposable) {
                progressStatus.value = false
                disContainer.add(d)
            }

            override fun onNext(t: String) {
                friendResponse.value = t
                Log.e("Peter2", "friendAgree  onNext:  "+t)
            }

            override fun onError(e: Throwable) {
                progressStatus.value = true
                Log.e("Peter2", "friendAgree  onError:  "+e.message)
            }
        }
        ApiMethods.friendAgree(friendObserver, dataBody)
    }

    fun friendRefuse(dataBody: FriendRefuse){
        val friendObserver: Observer<String> = object  : Observer<String>{
            override fun onComplete() {
                progressStatus.value = true
            }

            override fun onSubscribe(d: Disposable) {
                progressStatus.value = false
                disContainer.add(d)
            }

            override fun onNext(t: String) {
                friendResponse.value = t
                Log.e("Peter2", "friendRefuse  onNext:  "+t)
            }

            override fun onError(e: Throwable) {
                progressStatus.value = true
                Log.e("Peter2", "friendRefuse  onError:  "+e.message)
            }
        }
        ApiMethods.friendRefuse(friendObserver, dataBody)
    }

    fun friendCancel(dataBody: FriendCancel){
        val friendObserver: Observer<String> = object  : Observer<String>{
            override fun onComplete() {
                progressStatus.value = true
            }

            override fun onSubscribe(d: Disposable) {
                progressStatus.value = false
                disContainer.add(d)
            }

            override fun onNext(t: String) {
                friendResponse.value = t
                Log.e("Peter2", "friendCancel  onNext:  "+t)
            }

            override fun onError(e: Throwable) {
                progressStatus.value = true
                Log.e("Peter2", "friendCancel  onError:  "+e.message)
            }
        }
        ApiMethods.friendCancel(friendObserver, dataBody)
    }

    fun getWallet(){
        val friendObserver: Observer<Wallet> = object  : Observer<Wallet>{
            override fun onComplete() {
                progressStatus.value = true
            }

            override fun onSubscribe(d: Disposable) {
                progressStatus.value = false
                disContainer.add(d)
            }

            override fun onNext(t: Wallet) {
                wallet.value = t
                Log.e("Peter2", "getWallet  onNext:  "+t)
            }

            override fun onError(e: Throwable) {
                progressStatus.value = true
                Log.e("Peter2", "getWallet  onError:  "+e.message)
            }
        }
        ApiMethods.getWallet(friendObserver)
    }

    fun postInvitation(dataBody: InvitationDataBody){
        val friendObserver: Observer<String> = object  : Observer<String>{
            override fun onComplete() {
                progressStatus.value = true
            }

            override fun onSubscribe(d: Disposable) {
                progressStatus.value = false
                disContainer.add(d)
            }

            override fun onNext(t: String) {
                invitation.value = t
                Log.e("Peter2", "postInvitation  onNext:  "+t)
            }

            override fun onError(e: Throwable) {
                progressStatus.value = true
                Log.e("Peter2", "postInvitation  onError:  "+e.message)
            }
        }
        ApiMethods.postInvitation(friendObserver, dataBody)
    }

}