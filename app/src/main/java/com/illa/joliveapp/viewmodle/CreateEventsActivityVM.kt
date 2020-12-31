package com.illa.joliveapp.viewmodle

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.illa.joliveapp.datamodle.chat.chatroom_list.ChatRoomList
import com.illa.joliveapp.datamodle.event.detail.EventDetail
import com.illa.joliveapp.datamodle.event.detailv2.EventDetailV2
import com.illa.joliveapp.datamodle.event.event_list.EventList
import com.illa.joliveapp.datamodle.event.list.TypeLists
import com.illa.joliveapp.datamodle.event.review.EventReview
import com.illa.joliveapp.datamodle.event.review_member.ReviewMember
import com.illa.joliveapp.network.ApiMethods
import com.illa.joliveapp.tools.SingleLiveEvent
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import org.json.JSONObject
import java.io.File

class CreateEventsActivityVM (application: Application) : AndroidViewModel(application){
    private val mContent: Context? = application.applicationContext

    private val events: MutableLiveData<EventList> = MutableLiveData<EventList>()
    private val eventDetail: MutableLiveData<EventDetailV2> = MutableLiveData<EventDetailV2>()
    private val eventReview: MutableLiveData<EventReview> = MutableLiveData<EventReview>()
    private val paymentMethod: MutableLiveData<TypeLists> = MutableLiveData<TypeLists>()
    private val currencyType: MutableLiveData<TypeLists> = MutableLiveData<TypeLists>()
    private val eventCategory: MutableLiveData<TypeLists> = SingleLiveEvent<TypeLists>()
    private val errorMsg: SingleLiveEvent<String> = SingleLiveEvent<String>()
    private val chatRoomList: MutableLiveData<ChatRoomList> = MutableLiveData<ChatRoomList>()
    private var createEventResponse: MutableLiveData<String> = MutableLiveData<String>()
    private var updateEventResponse: MutableLiveData<String> = MutableLiveData<String>()
    private val progressStatus: MutableLiveData<Boolean> = MutableLiveData<Boolean>()


    fun getEvents(): LiveData<EventList> {
        return events
    }
    fun getEventDetail(): LiveData<EventDetailV2> {
        return eventDetail
    }
    fun getEventReview(): LiveData<EventReview> {
        return eventReview
    }
    fun getPaymentMethodData(): LiveData<TypeLists> {
        return paymentMethod
    }
    fun getCurrencyTypeData(): LiveData<TypeLists> {
        return currencyType
    }
    fun getEventCategoryData(): LiveData<TypeLists> {
        return eventCategory
    }
    fun getChatRoomListData(): LiveData<ChatRoomList> {
        return chatRoomList
    }
    fun getCreateEventResponse(): LiveData<String> {
        return createEventResponse
    }

    fun getUpdateEventResponse(): LiveData<String> {
        return updateEventResponse
    }

    fun getErrorMsg(): LiveData<String> {
        return errorMsg
    }
    fun getProgressStatus(): LiveData<Boolean> {
        return progressStatus
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
            }

            override fun onError(e: Throwable) {
                progressStatus.value = true
                Log.e("Peter","getEventsApi  onError    "+e.message)
            }

        }
        ApiMethods.getEvents(getEventsObserver,label, eventsCategorysId)
    }

    fun getEventDetail(label: String?){
        val getEventDetailObserver = object : Observer<EventDetailV2>{
            override fun onComplete() {
                progressStatus.value = true
            }

            override fun onSubscribe(d: Disposable) {
                progressStatus.value = false

            }

            override fun onNext(t: EventDetailV2) {
                Log.e("Peter","getEventDetail  onNext    $t")
                eventDetail.value = t
            }

            override fun onError(e: Throwable) {
                progressStatus.value = true
                Log.e("Peter","getEventDetail  onError    "+e.message)
            }
        }
        ApiMethods.getEventDetail(getEventDetailObserver,label)
    }

    fun getEventDetailById(eventId: String?){
        val getEventDetailObserver = object : Observer<EventDetailV2>{
            override fun onComplete() {
                progressStatus.value = true
            }

            override fun onSubscribe(d: Disposable) {
                progressStatus.value = false

            }

            override fun onNext(t: EventDetailV2) {
                Log.e("Peter","getEventDetail  onNext    $t")
                eventDetail.value = t
            }

            override fun onError(e: Throwable) {
                progressStatus.value = true
                Log.e("Peter","getEventDetail  onError    "+e.message)
            }
        }
        ApiMethods.getEventDetailById(getEventDetailObserver,eventId)
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
                if (jsonData.get("code") != "0"){
                    errorMsg.value = jsonData.get("data").toString()
                }
            }

            override fun onError(e: Throwable) {
                progressStatus.value = true
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
                Log.e("Peter","cancelJoinEvent  onNext    $t")

            }

            override fun onError(e: Throwable) {
                progressStatus.value = true
                Log.e("Peter","cancelJoinEvent  onError    "+e.message)
            }

        }
        ApiMethods.cancelJoinEvent(cancelJoinEventObserver, id)
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

    fun getPaymentMethod(){
        val paymentMethodObserver = object : Observer<TypeLists>{
            override fun onComplete() {
                progressStatus.value = true
            }

            override fun onSubscribe(d: Disposable) {
                progressStatus.value = false

            }

            override fun onNext(t: TypeLists) {
                paymentMethod.value = t
                Log.e("Peter","getPaymentMethod  onNext    $t")
            }

            override fun onError(e: Throwable) {
                progressStatus.value = true
                Log.e("Peter","getPaymentMethod  onError    "+e.message)
            }

        }
        ApiMethods.getPaymentMethod(paymentMethodObserver)
    }

    fun getCurrencyType(){
        val currencyTypeObserver = object : Observer<TypeLists>{
            override fun onComplete() {
                progressStatus.value = true
            }

            override fun onSubscribe(d: Disposable) {
                progressStatus.value = false

            }

            override fun onNext(t: TypeLists) {
                currencyType.value = t

                Log.e("Peter","getCurrencyType  onNext    $t")
            }

            override fun onError(e: Throwable) {
                progressStatus.value = true
                Log.e("Peter","getCurrencyType  onError    "+e.message)
            }

        }
        ApiMethods.getCurrencyType(currencyTypeObserver)
    }

    fun getEventCategory(){
        val eventCategoryObserver = object : Observer<TypeLists>{
            override fun onComplete() {
                progressStatus.value = true
            }

            override fun onSubscribe(d: Disposable) {
                progressStatus.value = false

            }

            override fun onNext(t: TypeLists) {
                eventCategory.value = t
                Log.e("Peter","getEventCategory  onNext    $t")
            }

            override fun onError(e: Throwable) {
                progressStatus.value = true
                Log.e("Peter","getEventCategory  onError    "+e.message)
            }

        }
        ApiMethods.getEventCategory(eventCategoryObserver)
    }

    @SuppressLint("ShowToast")
    @RequiresApi(Build.VERSION_CODES.N)
    fun createEvent(
        dataBody: Map<String, String>, file: File) {

        val createEventObserver = object : Observer<String>{
            override fun onComplete() {
                progressStatus.value = true
            }

            override fun onSubscribe(d: Disposable) {
                progressStatus.value = false

            }

            override fun onNext(t: String) {
                createEventResponse.value = t
                Log.e("Peter","createEvent  onNext    $t")

            }

            override fun onError(e: Throwable) {
                progressStatus.value = true
                errorMsg.value = e.message
                Log.e("Peter","createEvent  onError    ${e.localizedMessage}")

            }

        }
        ApiMethods.createEvent(createEventObserver, dataBody, file)
    }

    @SuppressLint("ShowToast")
    @RequiresApi(Build.VERSION_CODES.N)
    fun updateEvent(dataBody: Map<String, String>, file: File?, eventID: String) {
        Log.e("Peter","updateEvent  eventID    $eventID")


        val createEventObserver = object : Observer<String>{
            override fun onComplete() {
                progressStatus.value = true

            }

            override fun onSubscribe(d: Disposable) {
                progressStatus.value = false

            }

            override fun onNext(t: String) {

                Log.e("Peter","updateEvent  onNext    $t")
                val jsonData = JSONObject(t)
                if (jsonData.get("code") == 0){
                    Log.e("Peter","updateEvent  onNext 1   ${jsonData.get("code")}")
                    updateEventResponse.value = t
                    errorMsg.value = "FINISH"

                } else {
                    Log.e("Peter","updateEvent  onNext2    ${jsonData.get("code")}")

                    errorMsg.value = jsonData.get("data").toString()

                }
            }

            override fun onError(e: Throwable) {
                progressStatus.value = true
                errorMsg.value = e.message

                Log.e("Peter","updateEvent  onError    $e")

            }

        }
        ApiMethods.updateEvent(createEventObserver, dataBody, file, eventID)
    }


    fun getChatRoomList(){
        val chatRoomList: Observer<ChatRoomList> = object : Observer<ChatRoomList>{
            override fun onComplete() {
                progressStatus.value = true
            }

            override fun onSubscribe(d: Disposable) {
                progressStatus.value = false
            }

            override fun onError(e: Throwable) {
                progressStatus.value = true

                Log.e("Peter2", "chatRoomList onError:  $e")

            }

            override fun onNext(t: ChatRoomList) {
                chatRoomList.value = t
            }

        }
        ApiMethods.getChatRoomList(chatRoomList)
    }
}