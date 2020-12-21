package com.illa.joliveapp.viewmodle

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.illa.joliveapp.datamodle.event.detailv2.EventDetailV2
import com.illa.joliveapp.datamodle.event.review.EventReview
import com.illa.joliveapp.datamodle.event.review_member.ReviewMember
import com.illa.joliveapp.network.ApiMethods
import com.illa.joliveapp.tools.SingleLiveEvent
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.json.JSONObject

class EventDetailActivityVM (application: Application) : AndroidViewModel(application){
    private val mContent: Context? = application.applicationContext
    private val disContainer : CompositeDisposable = CompositeDisposable()


    //set
    private val eventDetail: SingleLiveEvent<EventDetailV2> = SingleLiveEvent<EventDetailV2>()
    private val eventReview: MutableLiveData<EventReview> = MutableLiveData<EventReview>()
    private val cancelJoinEventResponse: MutableLiveData<String> = MutableLiveData<String>()
    private val joinEventResponse: MutableLiveData<String> = MutableLiveData<String>()
    private val postEventReviewResponse: SingleLiveEvent<String> = SingleLiveEvent<String>()

    private val progressStatus: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    private val errorMsg: SingleLiveEvent<String> = SingleLiveEvent<String>()

    //get
    fun getEventDetailV2(): LiveData<EventDetailV2> {
        return eventDetail
    }
    fun getEventReview(): LiveData<EventReview> {
        return eventReview
    }


    fun getJoinEventResponse(): LiveData<String>{
        return joinEventResponse
    }

    fun getCancelJoinEventResponse(): LiveData<String>{
        return cancelJoinEventResponse
    }

    fun getPostEventReviewResponse(): LiveData<String>{
        return postEventReviewResponse
    }

    fun getErrorMsg(): LiveData<String> {
        return errorMsg
    }

    fun getProgressStatus(): LiveData<Boolean> {
        return progressStatus
    }


    fun joinEvent(id: String){
        val joinEventObserver = object : Observer<String>{
            override fun onComplete() {
                progressStatus.value = true
            }

            override fun onSubscribe(d: Disposable) {
                progressStatus.value = false

            }

            override fun onNext(t: String) {
                val jsonData = JSONObject(t)

                Log.e("Peter","joinEvent  onNext    $t")
                Log.e("Peter","joinEvent  onNext    ${jsonData.get("code")}")

                if (jsonData.get("code") != 0){
                    Log.e("Peter","joinEvent  onNext    jsonData.get != 0")

//                    errorMsg.value = jsonData.get("data").toString()
                } else {
                    Log.e("Peter","joinEvent  onNext    jsonData.get == 0")

                    joinEventResponse.value = t
                }
            }

            override fun onError(e: Throwable) {
                progressStatus.value = true
                errorMsg.value = e.message
                Log.e("Peter","joinEvent  onError    "+e.message)
            }

        }
        ApiMethods.joinEvent(joinEventObserver, id)
    }


    fun cancelJoinEvent(id: String){
        val cancelJoinEventObserver = object : Observer<String>{
            override fun onComplete() {
                progressStatus.value = true
            }

            override fun onSubscribe(d: Disposable) {
                progressStatus.value = false

            }

            override fun onNext(t: String) {
                cancelJoinEventResponse.value = t
                Log.e("Peter","cancelJoinEvent  onNext    $t")

            }

            override fun onError(e: Throwable) {
                progressStatus.value = true
                cancelJoinEventResponse.value = null
                errorMsg.value = e.message
                Log.e("Peter","cancelJoinEvent  onError    "+e.message)
            }

        }
        ApiMethods.cancelJoinEvent(cancelJoinEventObserver, id)
    }


    fun getEventDetailV2(label: String?){
        val getEventDetailObserver = object : Observer<EventDetailV2>{
            override fun onComplete() {
                progressStatus.value = true
            }

            override fun onSubscribe(d: Disposable) {
                progressStatus.value = false

            }

            override fun onNext(t: EventDetailV2) {

                Log.e("Peter","getEventDetailV2  onNext    $t")
                eventDetail.value = t
            }

            override fun onError(e: Throwable) {
                progressStatus.value = true

                Log.e("Peter","getEventDetailV2  onError    "+e.message)
            }
        }
        ApiMethods.getEventDetailV2(getEventDetailObserver,label)
    }

    fun getReviewList(eventID: String?){
        val getReviewListObserver = object : Observer<EventReview>{
            override fun onComplete() {
                progressStatus.value = true
            }

            override fun onSubscribe(d: Disposable) {
                progressStatus.value = false

            }

            override fun onNext(t: EventReview) {

                eventReview.value = t
                Log.e("Peter","getReviewList  onNext    $t")
            }

            override fun onError(e: Throwable) {
                progressStatus.value = true

                Log.e("Peter","getReviewList  onError    "+e.message)
            }
        }
        ApiMethods.getReviewList(getReviewListObserver,eventID)
    }

    fun postEventReview(dataBody: ReviewMember){
        val eventReviewObserver = object : Observer<String>{
            override fun onComplete() {
                progressStatus.value = true
            }

            override fun onSubscribe(d: Disposable) {
                progressStatus.value = false

            }

            override fun onNext(t: String) {
                Log.e("Peter","postEventReview  onNext    $t")
                val jsonData = JSONObject(t)

                Log.e("Peter","joinEvent  onNext    $t")
                if (jsonData.get("code") != "0"){
                    errorMsg.value = jsonData.get("data").toString()
                }

                getReviewList(dataBody.event_id.toString())
            }

            override fun onError(e: Throwable) {
                progressStatus.value = true
                Log.e("Peter","postEventReview  onError    "+e.message)
            }

        }
        ApiMethods.postEventReview(eventReviewObserver, dataBody)
    }

    fun postEventRollCall(dataBody: ReviewMember){
        val eventReviewObserver = object : Observer<String>{
            override fun onComplete() {
                progressStatus.value = true
            }

            override fun onSubscribe(d: Disposable) {
                progressStatus.value = false

            }

            override fun onNext(t: String) {
                Log.e("Peter","postEventReview  onNext    $t")
                val jsonData = JSONObject(t)

                Log.e("Peter","joinEvent  onNext    $t")
                if (jsonData.get("code") != "0"){
                    errorMsg.value = jsonData.get("data").toString()
                }

                getReviewList(dataBody.event_id.toString())
            }

            override fun onError(e: Throwable) {
                progressStatus.value = true
                Log.e("Peter","postEventReview  onError    "+e.message)
            }

        }
        ApiMethods.postEventRollCall(eventReviewObserver, dataBody)
    }
}