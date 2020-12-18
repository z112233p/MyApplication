package com.example.myapplication.viewmodle

import android.app.Application
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.datamodle.event.event_list.EventList
import com.example.myapplication.datamodle.event.my_events.MyEvents
import com.example.myapplication.datamodle.profile.MyInfo
import com.example.myapplication.datamodle.profile.MyInfoData
import com.example.myapplication.datamodle.profile.MyInfoPhoto
import com.example.myapplication.datamodle.profile.delete_photo.DeleteMyPhoto
import com.example.myapplication.datamodle.profile.delete_photo.response.DeleteMyPhotoResponse
import com.example.myapplication.datamodle.profile.interest.interest
import com.example.myapplication.datamodle.profile.job.job
import com.example.myapplication.datamodle.profile.update.UpdateMtInfo
import com.example.myapplication.datamodle.profile.update.UpdateMyInfoResponse
import com.example.myapplication.datamodle.profile.update_photo.UpdatePhotoResponse
import com.example.myapplication.datamodle.profile.user_info.UserInfo
import com.example.myapplication.network.ApiMethods
import com.example.myapplication.network.ApiService.Companion.create
import com.example.myapplication.tools.SingleLiveEvent
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
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
        val myInfoObserver: Observer<MyInfo> = object  : Observer<MyInfo>{
            override fun onComplete() {
                progressStatus.value = true
            }

            override fun onSubscribe(d: Disposable) {
                progressStatus.value = false
                disContainer.add(d)
            }

            override fun onNext(t: MyInfo) {
                Log.e("Peter2", "getMyInfo  onNext:  "+t.data.user.photos)
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
        val myInfoObserver: Observer<UserInfo> = object  : Observer<UserInfo>{
            override fun onComplete() {
                progressStatus.value = true
            }

            override fun onSubscribe(d: Disposable) {
                progressStatus.value = false
                disContainer.add(d)
            }

            override fun onNext(t: UserInfo) {
                Log.e("Peter2", "getMyInfo  onNext:  "+t.data.user.photos)
                userInfo.value = t
            }

            override fun onError(e: Throwable) {
                Log.e("Peter2", "getMyInfo  onError:  "+e.message)

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

}